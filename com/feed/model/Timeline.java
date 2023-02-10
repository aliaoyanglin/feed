package com.feed.model;

import com.alibaba.fastjson.JSON;

import java.sql.Timestamp;

public class Timeline {
    long uid;
    long id;
    Timestamp created;

    public Timeline() {

    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timeline(long uid, long id, Timestamp created) {
        this.uid = uid;
        this.id = id;
        this.created = created;
    }

    public String toJason(Timeline timeline){return JSON.toJSONString(timeline);}

    public Timeline parseJason(String timelineJason){return JSON.parseObject(timelineJason, Timeline.class);}
}
