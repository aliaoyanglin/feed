package com.feed.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class UserTimeline {
    long uid;
    List<Timeline> timelineList;

    public UserTimeline(long uid, List<Timeline> timelineList) {
        this.uid = uid;
        this.timelineList = timelineList;
    }

    public UserTimeline() {
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<Timeline> getTimelineList() {
        return timelineList;
    }

    public void setTimelineList(List<Timeline> timelineList) {
        this.timelineList = timelineList;
    }

    public String toJason(UserTimeline userTimeline){return JSON.toJSONString(userTimeline);}

    public static UserTimeline parseJason(String userTimelineJason){return JSON.parseObject(userTimelineJason, UserTimeline.class);}
}
