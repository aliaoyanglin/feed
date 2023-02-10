package com.feed.mock;

import com.feed.cache.GraphCache;
import com.feed.cache.TimelineCache;
import com.feed.cache.impl.GraphCacheImpl;
import com.feed.cache.impl.TimelineCacheImpl;
import com.feed.dao.FriendGraphDao;
import com.feed.dao.TimelineDao;
import com.feed.dao.impl.FriendGraphDaoImpl;
import com.feed.dao.impl.TimelineDaoImpl;
import com.feed.model.Timeline;
import com.feed.model.UserTimeline;
import com.feed.service.GraphService;
import com.feed.service.GraphTimelineService;
import com.feed.service.TimelineService;
import com.feed.service.impl.GraphServiceImpl;
import com.feed.service.impl.GraphTimelineServiceImpl;
import com.feed.service.impl.TimelineServiceImpl;

public class TimelineMock {
    private String TimelineMcHost = "10.226.52.80";
    private int TimelineMcPort = 11211;
    private String TimelineDb = "root";
    private TimelineService timelineService;
    private GraphTimelineService graphTimelineService;
    private GraphService graphService;

    public static void main(String[] args) {
        TimelineMock timelineMock = new TimelineMock();

        TimelineService timelineService = timelineMock.buildUserTimelineService();
        GraphTimelineService graphTimelineService = timelineMock.buildGraphTimelineService();
        GraphService graphService = timelineMock.buildGraphService();

        TimelineCache timelineCache = timelineMock.buildTimelineCache(timelineMock.getTimelineMcHost(),timelineMock.getTimelineMcPort());
        GraphCache graphCache = timelineMock.buildGraphCache(timelineMock.getTimelineMcHost(),timelineMock.getTimelineMcPort());

        timelineMock.setTimelineService(timelineService);
        timelineMock.setGraphService(graphService);
        timelineMock.setGraphTimelineService(graphTimelineService);

        timelineService.setTimelineCache(timelineCache);
        graphService.setGraphCache(graphCache);
        graphTimelineService.setTimelineCache(timelineCache);
        graphTimelineService.setGraphService(graphService);
        graphTimelineService.setTimelineService(timelineService);
        graphTimelineService.setTimelineCache(timelineCache);

        //function
//        timelineMock.getUserTimeline();//success
//        timelineMock.getGraphTimeline();//success
    }

    public TimelineService buildUserTimelineService(){
        TimelineDao timelineDao = new TimelineDaoImpl();
        TimelineCache timelineCache = new TimelineCacheImpl();

        TimelineService timelineService = new TimelineServiceImpl();
        timelineService.setTimelineCache(timelineCache);
        timelineService.setTimelineDao(timelineDao);

        return timelineService;
    }
    public GraphTimelineService buildGraphTimelineService(){
        TimelineDao timelineDao = new TimelineDaoImpl();
        TimelineCache timelineCache = new TimelineCacheImpl();

        GraphTimelineService graphTimelineService = new GraphTimelineServiceImpl();
        graphTimelineService.setTimelineCache(timelineCache);
        graphTimelineService.setTimelineDao(timelineDao);

        return graphTimelineService;
    }
    public GraphService buildGraphService(){
        FriendGraphDao friendGraphDao = new FriendGraphDaoImpl();
        GraphCache graphCache = new GraphCacheImpl();

        GraphService graphService = new GraphServiceImpl();
        graphService.setGraphCache(graphCache);
        graphService.setGraphDao(friendGraphDao);

        return graphService;
    }

    public String getTimelineMcHost() {
        return TimelineMcHost;
    }

    public int getTimelineMcPort() {
        return TimelineMcPort;
    }

    public TimelineCache buildTimelineCache(String timelineMcHost,int timelineMcPort){
        return new TimelineCacheImpl(timelineMcHost, timelineMcPort);
    }
    public GraphCache buildGraphCache(String timelineMcHost, int timelineMcPort){
        return new GraphCacheImpl(timelineMcHost, timelineMcPort);
    }

    public void setTimelineService(TimelineService timelineService){
        this.timelineService = timelineService;
    }
    public void setGraphService(GraphService graphService){
        this.graphService = graphService;
    }

    public void setGraphTimelineService(GraphTimelineService graphTimelineService) {
        this.graphTimelineService = graphTimelineService;
    }

    //function
    public void getUserTimeline(){
        long uid = 10366;
        UserTimeline userTimeline = timelineService.getTimeline(uid);
        System.out.println(userTimeline.toJason(userTimeline));
    }

    public void getGraphTimeline(){
        long uid = 10366;
        UserTimeline userTimeline = graphTimelineService.getGraphTimeline(uid);
        for(Timeline timeline:userTimeline.getTimelineList()){
            System.out.println(timeline.toJason(timeline));
        }
    }
}
