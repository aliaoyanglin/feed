package com.feed.cache;

import com.feed.model.Friend;

public interface GraphCache {
    void addFriend(Friend friend);
    void deleteFriend(Friend friend);
    Friend getFriends(long id);

    void addFollowing(Friend friend);
    void deleteFollowing(Friend friend);
    Friend getFollowing(long id);
}
