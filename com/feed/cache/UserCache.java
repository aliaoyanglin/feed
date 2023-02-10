package com.feed.cache;

import com.feed.model.User;

import java.util.Map;

public interface UserCache {
    void setUser(User user);

    User getUser(long uid);

    void deleteUser(long uid);

    Map<Long, User> getUsers(long[] uids);
}
