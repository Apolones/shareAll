package ru.fisenko.shareAll.models;


import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "posts_db")
public class Post {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "text")
    private String s;
    @Column(name = "data")
    private Date data;
    @Column(name = "expired")
    private Date expired;
    @Column(name = "user_id")
    private int user_id;


    public Post(String s, Date data, Date expired, String id) {
        this.s = s;
        this.data = data;
        this.expired = expired;
        this.id = id;
    }

    public Post() {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
