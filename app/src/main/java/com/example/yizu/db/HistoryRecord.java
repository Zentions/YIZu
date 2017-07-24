package com.example.yizu.db;

import android.webkit.DateSorter;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by q on 2017/7/20.
 */

public class HistoryRecord extends DataSupport{
    private String record;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
