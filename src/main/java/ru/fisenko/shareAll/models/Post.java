package ru.fisenko.shareAll.models;

import java.util.Date;

public class Post {
    private int id;
    private String s;
    private Date data;
    private Date expired;
    private String url;


    public Post(int id, String s, Date data, Date expired, String url) {
        this.id = id;
        this.s = s;
        this.data = data;
        this.expired = expired;
        this.url = url;
    }

    public Post() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
