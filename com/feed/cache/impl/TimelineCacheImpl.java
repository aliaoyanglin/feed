package com.feed.cache.impl;

import com.feed.cache.TimelineCache;
import com.feed.model.Timeline;
import com.feed.model.UserTimeline;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimelineCacheImpl implements TimelineCache {
    private MemcachedClient mcc;
    private String userMcHost;
    private int userMcPort;

    public TimelineCacheImpl(String userMcHost, int userMcPort) {
        this.userMcHost = userMcHost;
        this.userMcPort = userMcPort;
        try {
            this.mcc = new MemcachedClient(new InetSocketAddress(userMcHost, userMcPort));
        } catch (IOException e) {
            System.out.println(String.format("connect to mc: %s:%d failed: %s", userMcHost, userMcPort, e));
            throw new UnsupportedOperationException("mc no available - " + userMcHost + ":" + userMcPort);
        }
    }

    public TimelineCacheImpl() {
    }

    @Override
    public void setTimeline(long uid, long id, Timestamp created) {
        if (getTimeline(uid) == null) {
            UserTimeline userTimeline = new UserTimeline();
            List<Timeline> timelineList = new ArrayList<>();
            Timeline timeline = new Timeline(uid, id, created);
            timelineList.add(timeline);
            userTimeline.setUid(uid);
            userTimeline.setTimelineList(timelineList);
            mcc.set("" + uid + "", 3600, userTimeline.toJason(userTimeline));
        } else {
            UserTimeline userTimeline = getTimeline(uid);
            List<Timeline> timelineList = userTimeline.getTimelineList();
            Timeline timeline = new Timeline(uid, id, created);
            timelineList.add(timeline);
            userTimeline.setUid(uid);
            userTimeline.setTimelineList(timelineList);
            timelineList = userTimeline.getTimelineList();
            timelineList.sort(Comparator.comparing(Timeline::getId).reversed());//以id为基准进行降序排序
            mcc.set("timeline" + uid + "", 3600, userTimeline.toJason(userTimeline));
        }
    }

    @Override
    public UserTimeline getTimeline(long uid) {
        return UserTimeline.parseJason(String.valueOf(mcc.get("timeline"+ String.valueOf(uid))));
    }

    @Override
    public void deleteTimeline(long uid, long id, Timestamp created) {
        if (getTimeline((uid)) != null) {
            {
                UserTimeline userTimeline = getTimeline(uid);
                List<Timeline> timelineList = userTimeline.getTimelineList();
                Timeline timeline = new Timeline(uid, id, created);
                for(Timeline timeline1 : timelineList)
                    for(int i = timelineList.size()-1;i>=0;i--)
                        if(timeline1.getId() == id)
                            timelineList.remove(i);
                userTimeline.setTimelineList(timelineList);
                mcc.set("timeline" + uid + "", 3600, userTimeline.toJason(userTimeline));
            }
        }
    }

    //GraphTimeline

    @Override
    public UserTimeline getGraphTimeline(long uid) {
        return UserTimeline.parseJason(String.valueOf(mcc.get("timeline"+ uid)));
    }

    @Override
    public void setGraphTimeline(UserTimeline graphTimeline) {
        mcc.set("timeline"+graphTimeline.getUid()+"", 3600, graphTimeline.toJason(graphTimeline));
    }
}
