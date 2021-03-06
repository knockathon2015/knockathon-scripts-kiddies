package com.rsamadhan.comments;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rsamadhan.R;
import com.rsamadhan.network.response.Results;

import java.util.List;


public class CommentsListAdapter extends RecyclerView.Adapter {

    private List<Results> mCommentList;
    private Context mContext;

    public CommentsListAdapter(List<Results> commentList,Context context){
        mCommentList=commentList;
        mContext=context;
    }

    public class CarViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public TextView commentView;
        public CarViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            commentView= (TextView) itemView.findViewById(R.id.tv_comment_view);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.comment_row_view, parent,false);
        CarViewHolder holder=new CarViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CarViewHolder carViewHolder= (CarViewHolder) holder;
        carViewHolder.commentView.setText(mCommentList.get(position).getDescription());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}
