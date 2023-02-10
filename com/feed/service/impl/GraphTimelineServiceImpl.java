package com.feed.service.impl;

import com.feed.cache.TimelineCache;
import com.feed.dao.TimelineDao;
import com.feed.model.Attention;
import com.feed.model.Timeline;
import com.feed.model.UserTimeline;
import com.feed.service.GraphService;
import com.feed.service.GraphTimelineService;
import com.feed.service.TimelineService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphTimelineServiceImpl implements GraphTimelineService {
    private TimelineCache timelineCache;
    private GraphService graphService;
    private TimelineDao timelineDao;
    private TimelineService timelineService;

    @Override
    public void setTimelineCache(TimelineCache timelineCache) {
        this.timelineCache = timelineCache;
    }

    @Override
    public void setTimelineDao(TimelineDao timelineDao) {
        this.timelineDao = timelineDao;
    }

    @Override
    public void setGraphService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Override
    public void setTimelineService(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @Override
    public UserTimeline getGraphTimeline(long uid) {
        List<Attention> attentionList = graphService.getFriends(uid);
        List<Timeline> timelineList = new ArrayList<>();
        for (Attention attention : attentionList) {
            timelineList.addAll(timelineService.getTimeline(attention.getUid()).getTimelineList());
        }
        timelineList.sort(Comparator.comparing(Timeline::getId));
        UserTimeline graphTimeline = new UserTimeline(uid, timelineList);
        timelineCache.setGraphTimeline(graphTimeline);
        return graphTimeline;
    }
}
