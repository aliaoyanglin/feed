package com.feed.cache.impl;

import com.feed.cache.UserCache;
import com.feed.mock.UserMock;
import com.feed.model.User;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class UserCacheImpl  implements UserCache {
    private MemcachedClient mcc;
    private String userMcHost;
    private int userMcPort;

    public UserCacheImpl(String userMcHost, int userMcPort) {
        this.userMcHost = userMcHost;
        this.userMcPort = userMcPort;
        try {
            this.mcc = new MemcachedClient(new InetSocketAddress(userMcHost,userMcPort));
        }catch (IOException e){
            System.out.println(String.format("connect to mc: %s:%d falied: e", userMcHost, userMcPort, e));
            throw new UnsupportedOperationException("mc no available - " + userMcHost + ":" + userMcPort);
        }
    }

    public UserCacheImpl() {
    }


    @Override
    public void setUser(User user) {
        mcc.set(""+user.getUid()+"", 3600, user.toJason(user));
        System.out.println("the key in cache is -" + user.getUid());
    }

    @Override
    public User getUser(long uid) {
        if (mcc.get(String.valueOf(uid)) != null)
            return User.parseJason(String.valueOf(mcc.get(String.valueOf(uid))));
        else return null;
    }

    @Override
    public void deleteUser(long uid) {
        mcc.delete(String.valueOf(uid));
    }

    @Override
    public Map<Long, User> getUsers(long[] uids) {
        Map<Long, User> userMap = new HashMap<>();
        for (long uid : uids) {
            userMap.put(uid, User.parseJason(String.valueOf(mcc.get(String.valueOf(uid)))));
        }
        return userMap;
    }
}
