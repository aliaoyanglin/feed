package com.feed.service;

import com.feed.cache.ContentCache;
import com.feed.cache.TimelineCache;
import com.feed.dao.ContentDao;
import com.feed.dao.TimelineDao;
import com.feed.model.Content;

import java.sql.Timestamp;
import java.util.List;

public interface ContentService {
    void addContent(long fatherId, long uid, String txt, Timestamp created);
    void deleteContents(List<Long> ids);
    Content getContent(long id);
    void forwardContent(long id, long fatherId);
    void setTimeline(long uid, long id, Timestamp created);

    void setContentCache(ContentCache contentCache);
    void setContentDao(ContentDao contentDao);
    void setTimelineCache(TimelineCache timelineCache);
    void setTimelineDao(TimelineDao timelineDao);
}
