package com.feed.service.impl;

import com.feed.cache.TimelineCache;
import com.feed.dao.TimelineDao;
import com.feed.model.Timeline;
import com.feed.model.UserTimeline;
import com.feed.service.TimelineService;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

public class TimelineServiceImpl implements TimelineService {
    private TimelineDao timelineDao;
    private TimelineCache timelineCache;

    @Override
    public void setTimelineDao(TimelineDao timelineDao) {
        this.timelineDao = timelineDao;
    }

    @Override
    public void setTimelineCache(TimelineCache timelineCache) {
        this.timelineCache = timelineCache;
    }

    @Override
    public void setTimeline(long uid, long id, Timestamp created) {
        timelineDao.setTimeline(uid, id, created);
        timelineCache.setTimeline(uid, id, created);
    }

    @Override
    public UserTimeline getTimeline(long uid) {
        if(timelineCache.getTimeline(uid)!=null)
            return timelineCache.getTimeline(uid);
        else {
            List<Timeline> timelineList = timelineDao.getTimeline(uid);
            for(Timeline timeline:timelineList){
                timelineCache.setTimeline(timeline.getUid(), timeline.getId(), timeline.getCreated());
            }
            timelineList.sort(Comparator.comparing(Timeline::getId).reversed());
            return new UserTimeline(uid, timelineList);
        }
    }

    @Override
    public void deleteTimeline(long uid, long id, Timestamp created) {
        timelineDao.deleteTimeline(id);
        timelineCache.deleteTimeline(uid, id, created);
    }
}
