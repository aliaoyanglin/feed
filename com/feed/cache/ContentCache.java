package com.feed.cache;

import com.feed.model.Content;

import java.util.List;

public interface ContentCache {
    void addContent(Content content);
    void deleteContent(List<Long> ids);
    Content getContent(long id);
}
