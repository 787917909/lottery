package com.example.qiang.entity;

public class Theme {

    private int id;

    private int imageid;

    private String theme;

    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", imageid=" + imageid +
                ", theme='" + theme + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
