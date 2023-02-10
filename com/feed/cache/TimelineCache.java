package com.feed.cache;

import com.feed.model.UserTimeline;

import java.sql.Timestamp;

public interface TimelineCache {
    void setTimeline(long uid, long id, Timestamp created);
    void deleteTimeline(long uid, long id, Timestamp created);
    UserTimeline getTimeline(long id);

    UserTimeline getGraphTimeline(long uid);
    void setGraphTimeline(UserTimeline graphTimeline);
}
