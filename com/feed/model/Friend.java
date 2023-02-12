package com.feed.model;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Friend {
    private long uid;
    private List<Attention> attentionList;

    public Friend(long uid, List<Attention> attention) {
        this.uid = uid;
        this.attentionList = attention;
    }

    public Friend() {
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<Attention> getAttention() {
        return attentionList;
    }

    public void setAttention(List<Attention> attentionList) {
        this.attentionList = attentionList;
    }

    public String toJason(){
        return JSON.toJSONString(this);
    }

    public static Friend parseJason(String userJason){
        return JSON.parseObject(userJason, Friend.class);
    }

}
