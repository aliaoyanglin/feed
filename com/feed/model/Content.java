package com.feed.model;

import com.alibaba.fastjson.JSON;

import java.sql.Timestamp;
import java.util.Date;

public class Content {
    private long fatherId;
    private long id;
    private long uid;
    private String content;
    private Timestamp created;

    public Content(long fatherId, long id, long uid, String content, Timestamp created) {
        this.fatherId = fatherId;
        this.id = id;
        this.uid = uid;
        this.content = content;
        this.created = created;
    }

    public Content() {
    }

    public long getFatherId() {
        return fatherId;
    }

    public void setFatherId(long fatherId) {
        this.fatherId = fatherId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String toJason(Content content){return JSON.toJSONString(content);}

    public static Content parseJason(String userJason){return JSON.parseObject(userJason, Content.class);}
}
