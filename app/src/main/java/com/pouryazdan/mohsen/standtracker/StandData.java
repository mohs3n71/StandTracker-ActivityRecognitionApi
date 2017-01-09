package com.pouryazdan.mohsen.standtracker;

/**
 * Created by Mohsen on 1/8/2017.
 */

public class StandData {
    private int _id;
    private String _date;
    private int _count;

    public StandData() {
    }


    public StandData(int id, String date, int count) {
        this._id = id;
        this._date = date;
        this._count = count;
    }

    public StandData(String date, int count) {
        this._date = date;
        this._count = count;
    }

    public int get_id() {
        return _id;
    }

    public String get_date() {
        return _date;
    }

    public int get_count() {
        return _count;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public void set_date(String date) {
        this._date = date;
    }

    public void set_count(int count) {
        this._count = count;
    }


}
