package com.feed.dao;

import com.feed.model.Content;

import java.sql.Timestamp;

public interface ContentDao {
    long addContent(long fatherId, long uid, String txt, Timestamp created);
    void deleteContents(long id);
    Content getContent(long id);
}
