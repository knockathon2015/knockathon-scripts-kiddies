package com.rsamadhan.network.response;

import com.rsamadhan.network.response.Results;

import java.util.ArrayList;

/**
 * Created by Prathmesh on 12-09-2015.
 */
public class ComplaintListData {
    public ArrayList<Results> results;

    public ArrayList<Results> getResults() {
        return results;
    }

    public void setResults(ArrayList<Results> results) {
        this.results = results;
    }
}
