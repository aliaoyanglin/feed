package com.feed.service;

import com.feed.cache.GraphCache;
import com.feed.dao.FollowingGraphDao;
import com.feed.dao.FriendGraphDao;
import com.feed.model.Attention;
import com.feed.model.Friend;

import java.util.List;

public interface GraphService {
    void addFriend(Friend friend);//关注好友
    void deleteFriend(Friend friend);//取关好友
    List<Attention> getFriends(long uid);

    void addFollowing(Friend friend);
    void deleteFollowing(Friend friend);
    List<Attention> getFollowings(long uid);

    void setGraphCache(GraphCache graphCache);
    void setGraphDao(FriendGraphDao friendGraphDao);
    void setFollowingGraphDao(FollowingGraphDao followingGraphDao);
}
