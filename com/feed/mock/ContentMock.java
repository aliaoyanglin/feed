package com.feed.mock;

import com.feed.cache.ContentCache;
import com.feed.cache.TimelineCache;
import com.feed.cache.UserCache;
import com.feed.cache.impl.ContentCacheImpl;
import com.feed.cache.impl.TimelineCacheImpl;
import com.feed.cache.impl.UserCacheImpl;
import com.feed.dao.ContentDao;
import com.feed.dao.TimelineDao;
import com.feed.dao.UserDao;
import com.feed.dao.impl.ContentDaoImpl;
import com.feed.dao.impl.TimelineDaoImpl;
import com.feed.dao.impl.UserDaoImpl;
import com.feed.model.Content;
import com.feed.service.ContentService;
import com.feed.service.GraphService;
import com.feed.service.UserService;
import com.feed.service.impl.ContentServiceImpl;
import com.feed.service.impl.UserServiceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ContentMock {
    private String contentMcHost = "10.226.52.80";
    private int contentMcPort = 11211;
    private String contentDb = "root";

    private ContentService contentService;

    public String getContentMcHost() {
        return contentMcHost;
    }

    public int getContentMcPort() {
        return contentMcPort;
    }

    public String getContentDb() {
        return contentDb;
    }

    public ContentService getContentService() {
        return contentService;
    }

    public static void main(String[] args) {
        ContentMock contentMock = new ContentMock();
        ContentService contentService = contentMock.buildContentService();
        ContentCache contentCache = contentMock.buildContentCache(contentMock.getContentMcHost(), contentMock.getContentMcPort());
        TimelineCache timelineCache = contentMock.buildTimelineCache(contentMock.getContentMcHost(), contentMock.getContentMcPort());
        contentMock.setContentService(contentService);
        contentService.setContentCache(contentCache);
        contentService.setTimelineCache(timelineCache);

        //content function test

//        contentMock.addContent();//success
        contentMock.getAllContent();//success
//        contentMock.deleteContent();//success
//        contentMock.forwardContent();//success
    }

    public ContentService buildContentService() {
        ContentDao contentDao = new ContentDaoImpl();
        TimelineDao timelineDao = new TimelineDaoImpl();
        ContentCache contentCache = new ContentCacheImpl(contentMcHost, contentMcPort);
        TimelineCache timelineCache = new TimelineCacheImpl(contentMcHost, contentMcPort);

        ContentService contentService = new ContentServiceImpl();
        contentService.setContentDao(contentDao);
        contentService.setTimelineDao(timelineDao);
        contentService.setContentCache(contentCache);
        contentService.setTimelineCache(timelineCache);
        return contentService;
    }

    public ContentCache buildContentCache(String ContentMcHost, int contentMcPort){
        return new ContentCacheImpl(contentMcHost, contentMcPort);
    }
    public TimelineCache buildTimelineCache(String ContentMcHost, int contentMcPort){
        return new TimelineCacheImpl(contentMcHost, contentMcPort);
    }

    public void setContentService(ContentService contentService){this.contentService = contentService;}

    //function
    public void addContent(){
        for(int i = 10200;i < 10400;i++){
            for(int k = 0;k<1;k++)
            addContent(i);
        }
    }

    public void addContent(long uid){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        contentService.addContent(-1, uid, "textTxt:"+uid+"", timestamp);
    }

    public void getContent(long id){
        contentService.getContent(id);
    }

    public void getAllContent(){
        for(int i = 0;i<40945;i++) {
            Content content = contentService.getContent(i);
            System.out.println(content.toJason(content));
        }
    }

    public void deleteContent(){
        List<Long> fids = new ArrayList<>();
        for(int i = 40001;i<40945;i+=1){
            fids.add((long) i);
        }
        contentService.deleteContents(fids);
    }

    public void forwardContent(){
        contentService.forwardContent(10200, 20002);
    }
}
