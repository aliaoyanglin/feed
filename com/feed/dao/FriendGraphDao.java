package com.feed.dao;

import com.feed.model.Friend;

public interface FriendGraphDao {
    void addFriend(Friend friend);
    void deleteFriend(Friend friend);
    Friend getFriends(long uid);
}
