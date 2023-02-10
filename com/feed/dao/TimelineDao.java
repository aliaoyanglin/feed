package com.feed.dao;

import com.feed.model.Timeline;

import java.util.Date;
import java.util.List;

public interface TimelineDao {
    void setTimeline(long uid, long id, Date created);
    List<Timeline> getTimeline(long uid);
    void deleteTimeline(long id);
}
