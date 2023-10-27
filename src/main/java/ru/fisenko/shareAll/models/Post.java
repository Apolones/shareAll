package ru.fisenko.shareAll.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;


@Entity
@Table(name = "posts_db")
public class Post {
    @Id
    @Column(name = "id")
    private String id;
    @NotNull
    @Column(name = "path_data")
    private String pathData;
    @NotNull
    @Column(name = "create_time")
    private Date createTime;
    @NotNull
    @Column(name = "expired_time")
    private Date expiredTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person person;


    public Post(String pathData, Date createTime, Date expiredTime, String id) {
        this.pathData = pathData;
        this.createTime = createTime;
        this.expiredTime = expiredTime;
        this.id = id;
    }

    public Post() {

    }

    public String getPathData() {
        return pathData;
    }

    public void setPathData(String s) {
        this.pathData = s;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date data) {
        this.createTime = data;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expired) {
        this.expiredTime = expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
