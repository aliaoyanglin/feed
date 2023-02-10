package com.feed.dao;

import com.feed.model.Friend;

public interface FollowingGraphDao {
    void addFollowing(Friend friend);
    void deleteFollowing(Friend friend);
    Friend getFollowings(long uid);
}
