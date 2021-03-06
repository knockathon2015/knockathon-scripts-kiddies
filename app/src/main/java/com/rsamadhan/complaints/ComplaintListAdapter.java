package com.rsamadhan.complaints;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsamadhan.R;
import com.rsamadhan.comments.CommentsActivity;
import com.rsamadhan.common.PreferenceManager;
import com.rsamadhan.network.NetworkApi;
import com.rsamadhan.network.callbackrequest.PostCommentCallback;
import com.rsamadhan.network.response.CommentResponse;
import com.rsamadhan.network.response.Results;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;


public class ComplaintListAdapter extends RecyclerView.Adapter {

    private List<Results> mComplainList;
    private Context mContext;
    private String mDomainName;
    private ProgressDialog mDialog;
    private String mProbType;


    public ComplaintListAdapter(List<Results> complainList, Context context, String domainName, String probType){
        if(complainList==null){
            mComplainList=new ArrayList<>();
        }else{
            mComplainList =complainList;
        }
        mDomainName=domainName;
        mContext=context;
        mDialog=new ProgressDialog(mContext);
        mProbType = probType;
    }

    public void updateList(ArrayList<Results> results) {
        if (results != null) {
            mComplainList = results;
        }
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {

        public TextView cardDetails;
        public TextView cardHead;
        public ImageView action;
        public ImageView comment;
        public TextView tvHead;


        public CarViewHolder(View itemView) {
            super(itemView);
            tvHead = (TextView) itemView.findViewById(R.id.tv_cv_head);
            cardDetails= (TextView) itemView.findViewById(R.id.tv_cv_detail);
            cardHead= (TextView) itemView.findViewById(R.id.tv_cv_head);
            action= (ImageView) itemView.findViewById(R.id.action);
            comment= (ImageView) itemView.findViewById(R.id.comment);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.complaint_row, parent, false);
        CarViewHolder holder = new CarViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CarViewHolder carViewHolder = (CarViewHolder) holder;

        carViewHolder.tvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCommentsPage(carViewHolder.cardDetails.getText().toString(),mComplainList.get(position).getActivity_id(),mDomainName);
            }
        });
        carViewHolder.cardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCommentsPage(carViewHolder.cardDetails.getText().toString(),mComplainList.get(position).getActivity_id(),mDomainName);
            }
        });
        carViewHolder.cardHead.setText(mComplainList.get(position).getActivity_title());
        carViewHolder.cardDetails.setText(mComplainList.get(position).getActivity_description());



        if(mProbType.equals("personal")){
            if(mComplainList.get(position).getStatus().equals("Close")){
                carViewHolder.action.setImageResource(R.drawable.ic_lock_open_black_24dp);
            }else{
                carViewHolder.action.setImageResource(R.drawable.ic_lock_outline_black_24dp);
            }
        }else{
            if(mComplainList.get(position).getStatus().equals("Close")){
                carViewHolder.cardDetails.setTextColor(Color.RED);
            }else{
                carViewHolder.cardDetails.setTextColor(Color.BLUE);
            }
        }

        carViewHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        carViewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment(mDomainName,mComplainList.get(position));
            }
        });

    }

    private void postComment(final String mDomainName, final Results results) {

        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        View dialogView=LayoutInflater.from(mContext).inflate(R.layout.dialog_create_comment, null, false);
//        EditText name= (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText comment= (EditText) dialogView.findViewById(R.id.edittextComment);
        builder.setView(dialogView).setPositiveButton(R.string.post_comment, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String desc=comment.getText().toString();
                NetworkApi api=new NetworkApi();
                showProgress();
                api.postNewComment(new PostCommentCallback() {
                    @Override
                    public void commentSuccess(CommentResponse o) {
                        hideProgress();
                    }
                    @Override
                    public void commentFail(RetrofitError error) {
                        hideProgress();
                    }
                },results.getActivity_id(),desc,mDomainName, PreferenceManager.getInstance(mContext).getLoginId());
            }
        }).setNegativeButton(R.string.no_txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
    private void showProgress() {
        mDialog.setMessage(mContext.getString(R.string.prog_comment_txt));
        mDialog.show();
    }

    private void hideProgress() {
        mDialog.dismiss();
    }

    private void launchCommentsPage(String v,String s,String domain) {

        Intent intent = new Intent(mContext, CommentsActivity.class);
        intent.putExtra(CommentsActivity.COMMENT_HEAD, v);
        intent.putExtra(CommentsActivity.ACTIVITY_ID,s);
        intent.putExtra(CommentsActivity.DOMAIN,domain);
        mContext.startActivity(intent);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mComplainList.size();
    }
}

