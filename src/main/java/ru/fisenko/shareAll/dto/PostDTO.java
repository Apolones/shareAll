package ru.fisenko.shareAll.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class PostDTO {

    @Column(name = "id")
    private String id;
    @Column(name = "data")
    private Date createTime;
    @Column(name = "expired")
    private Date expiredTime;
    @Column(name = "storageData")
    @Size(min = 1, max = Integer.MAX_VALUE)
    private String storageData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getStorageData() {
        return storageData;
    }

    public void setStorageData(String storageData) {
        this.storageData = storageData;
    }


}
