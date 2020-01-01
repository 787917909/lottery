package com.example.qiang.gson;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Winners extends DataSupport {

    private int id;

    private int noteid;

    private String winjson;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteid() {
        return noteid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public String getWinjson() {
        return winjson;
    }

    public void setWinjson(String winjson) {
        this.winjson = winjson;
    }

    @Override
    public String toString() {
        return "Winners{" +
                "id=" + id +
                ", noteid=" + noteid +
                ", winjson='" + winjson + '\'' +
                '}';
    }
}
