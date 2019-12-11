package com.example.qiang.gson;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.litepal.crud.DataSupport;


public class Note extends DataSupport {

    private int id;

    private String title;

    private String award1;

    private String award2;

    private String award3;

    private String date;

    private int peoplefirst;

    private int peoplesecond;

    private int peoplethird;

    private int totalpeople;


    public String getAward1() {
        return award1;
    }

    public void setAward1(String award1) {
        this.award1 = award1;
    }

    public String getAward2() {
        return award2;
    }

    public void setAward2(String award2) {
        this.award2 = award2;
    }

    public String getAward3() {
        return award3;
    }

    public void setAward3(String award3) {
        this.award3 = award3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPeoplefirst() {
        return peoplefirst;
    }

    public void setPeoplefirst(int peoplefirst) {
        this.peoplefirst = peoplefirst;
    }

    public int getPeoplesecond() {
        return peoplesecond;
    }

    public void setPeoplesecond(int peoplesecond) {
        this.peoplesecond = peoplesecond;
    }

    public int getPeoplethird() {
        return peoplethird;
    }

    public void setPeoplethird(int peoplethird) {
        this.peoplethird = peoplethird;
    }

    public int getTotalpeople() {
        return totalpeople;
    }

    public void setTotalpeople(int totalpeople) {
        this.totalpeople = totalpeople;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", award1='" + award1 + '\'' +
                ", award2='" + award2 + '\'' +
                ", award3='" + award3 + '\'' +
                ", date='" + date + '\'' +
                ", peoplefirst=" + peoplefirst +
                ", peoplesecond=" + peoplesecond +
                ", peoplethird=" + peoplethird +
                ", totalpeople=" + totalpeople +
                '}';
    }
}
