package com.feed.service.impl;

import com.feed.cache.ContentCache;
import com.feed.cache.TimelineCache;
import com.feed.dao.ContentDao;
import com.feed.dao.TimelineDao;
import com.feed.model.Content;
import com.feed.service.ContentService;
import com.feed.service.TimelineService;

import java.sql.Timestamp;
import java.util.List;

public class ContentServiceImpl implements ContentService {
    private ContentCache contentCache;
    private ContentDao contentDao;
    private TimelineCache timelineCache;
    private TimelineDao timelineDao;

    @Override
    public void setTimelineCache(TimelineCache timelineCache) {
        this.timelineCache = timelineCache;
    }

    @Override
    public void setTimelineDao(TimelineDao timelineDao) {
        this.timelineDao = timelineDao;
    }

    @Override
    public void setContentCache(ContentCache contentCache) {
        this.contentCache = contentCache;
    }

    @Override
    public void setContentDao(ContentDao contentDao) {
        this.contentDao = contentDao;
    }

    @Override
    public void setTimeline(long uid, long id, Timestamp created) {
        timelineDao.setTimeline(uid, id, created);
        timelineCache.setTimeline(uid, id, created);
    }

    //function
    @Override
    public void addContent(long fatherId, long uid, String txt, Timestamp created) {
        long id = contentDao.addContent(fatherId, uid, txt, created);
        Content content = contentDao.getContent(id);

        setTimeline(uid,id,created);
        contentCache.addContent(content);
    }

    @Override
    public void deleteContents(List<Long> ids) {
        for(Long id:ids){
            Content content = getContent(id);
            contentDao.deleteContents(id);
            timelineCache.deleteTimeline(content.getUid(),content.getId(),content.getCreated());
            timelineDao.deleteTimeline(id);
        }
        contentCache.deleteContent(ids);
    }

    @Override
    public Content getContent(long id) {
        Content content = contentCache.getContent(id);
        if(content == null) {
            content = contentDao.getContent(id);

            contentCache.addContent(content);
        }
        return content;
    }

    @Override
    public void forwardContent(long id, long fatherId) {
        Content content = getContent(fatherId);
        if(content.getFatherId() != -1)
            addContent(fatherId, id, content.getContent(), content.getCreated());
    }


}
