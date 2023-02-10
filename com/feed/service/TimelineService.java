package com.feed.service;

import com.feed.cache.GraphCache;
import com.feed.cache.TimelineCache;
import com.feed.dao.TimelineDao;
import com.feed.model.UserTimeline;

import java.sql.Timestamp;
import java.util.Date;

public interface TimelineService {
    void setTimelineDao(TimelineDao timelineDao);
    void setTimelineCache(TimelineCache timelineCache);

    void setTimeline(long uid, long id, Timestamp created);//uid = userId. id = feedId.
    UserTimeline getTimeline(long uid);
    void deleteTimeline(long uid, long id, Timestamp created);
}
