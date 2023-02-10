package com.feed.service;

import com.feed.cache.TimelineCache;
import com.feed.dao.TimelineDao;
import com.feed.model.UserTimeline;

public interface GraphTimelineService {
    void setTimelineCache(TimelineCache timelineCache);
    void setTimelineDao(TimelineDao timelineDao);
    void setGraphService(GraphService graphService);
    void setTimelineService(TimelineService timelineService);

    UserTimeline getGraphTimeline(long uid);


}
