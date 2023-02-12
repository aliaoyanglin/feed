package com.feed.model;

import com.alibaba.fastjson.JSON;

import javax.lang.model.type.NullType;
import java.sql.Timestamp;


public class Attention {
    private long uid;
    private Timestamp created;

    public Attention(long uid, Timestamp created) {
        this.uid = uid;
        this.created = created;
    }

    public Attention() {

    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o == null){
            return false;
        }

        if(getClass() != o.getClass()){
            return false;
        }
        Attention attention = (Attention) o;
        if(created != attention.created){
            return false;
        }
        else {
            if(uid!=attention.uid){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public String toJason(){return JSON.toJSONString(this);}

    public Attention parseJason(String attentionJason){return JSON.parseObject(attentionJason, Attention.class);}

    @Override
    public String toString() {
        return this.toJason();
    }
}

