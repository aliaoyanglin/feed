package com.feed.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class User {

    private long uid;
    private String name;
    private String address;
    private String phone;

    public User(long uid, String name, String address, String phone) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public User() {
    }
    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toJason(User user){
        return JSON.toJSONString(user);
    }

    public static User parseJason(String userJason){
        return JSON.parseObject(userJason, User.class);
    }
}