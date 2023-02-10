package com.feed.cache.impl;

import com.feed.cache.ContentCache;
import com.feed.model.Content;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class ContentCacheImpl implements ContentCache {
    private MemcachedClient mcc;
    private String userMcHost;
    private int userMcPort;

    public ContentCacheImpl(String userMcHost, int userMcPort) {
        this.userMcHost = userMcHost;
        this.userMcPort = userMcPort;
        try {
            this.mcc = new MemcachedClient(new InetSocketAddress(userMcHost,userMcPort));
        }catch (IOException e){
            System.out.println(String.format("connect to mc: %s:%d falied: e", userMcHost, userMcPort, e));
            throw new UnsupportedOperationException("mc no available - " + userMcHost + ":" + userMcPort);
        }
    }

    @Override
    public void addContent(Content content) {
        mcc.set("content"+content.getId()+"",3600, content.toJason(content));
    }

    @Override
    public void deleteContent(List<Long> ids) {
        for(Long id:ids){
            mcc.delete("content"+String.valueOf(id));
        }
    }

    @Override
    public Content getContent(long id) {
        return Content.parseJason(String.valueOf(mcc.get("content"+String.valueOf(id))));
    }
}
