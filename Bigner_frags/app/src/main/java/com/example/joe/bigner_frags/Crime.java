package com.example.joe.bigner_frags;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by joe on 8/9/2016.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mStringDate;


    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        mStringDate = DateFormat.getDateInstance().format(mDate);
        return mStringDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public String toString(){
        return mTitle;
    }
}
