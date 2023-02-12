package com.feed.service.impl;

import com.feed.cache.GraphCache;
import com.feed.dao.FollowingGraphDao;
import com.feed.dao.FriendGraphDao;
import com.feed.model.Attention;
import com.feed.model.Friend;
import com.feed.service.GraphService;

import java.util.List;

public class GraphServiceImpl implements GraphService {
    private GraphCache graphCache;
    private FriendGraphDao friendGraphDao;
    private FollowingGraphDao followingGraphDao;

    @Override
    public void addFriend(Friend friend) {
        //add to db
        friendGraphDao.addFriend(friend);

        //add to cache
        graphCache.addFriend(friend);
    }

    @Override
    public void deleteFriend(Friend friend) {
        graphCache.deleteFriend(friend);
        friendGraphDao.deleteFriend(friend);
    }

    @Override
    public List<Attention> getFriends(long uid) {
        if(graphCache.getFriends(uid)!=null) {
            System.out.println("1");
            return graphCache.getFriends(uid).getAttention();
        }
        else{
            Friend friend = friendGraphDao.getFriends(uid);
            System.out.println("2");
            graphCache.addFriend(friend);
            return friend.getAttention();
        }
    }

    @Override
    public void addFollowing(Friend friend) {
        //add to db
        followingGraphDao.addFollowing(friend);

        //add to cache此时不向cache中存入，在下次读取时再存入，以保证数据的一致性
        //原因是此时的key为从friend.getUid()存入的from_uid,而实际上要存入的key为to_uid.
//        graphCache.addFriend(friend);
    }

    @Override
    public void deleteFollowing(Friend friend) {
            followingGraphDao.deleteFollowing(friend);
            graphCache.deleteFollowing(friend);
    }

    @Override
    public List<Attention> getFollowings(long uid) {
        if (graphCache.getFollowing(uid) != null) {
            System.out.println("1");
            return graphCache.getFollowing(uid).getAttention();
        } else {
            Friend friend = followingGraphDao.getFollowings(uid);
            System.out.println("2");
            graphCache.addFollowing(friend);
            return friend.getAttention();
        }
    }




    @Override
    public void setGraphCache(GraphCache graphCache) {
        this.graphCache = graphCache;
    }

    @Override
    public void setGraphDao(FriendGraphDao friendGraphDao) {
        this.friendGraphDao = friendGraphDao;
    }

    public FriendGraphDao getFriendGraphDao() {
        return friendGraphDao;
    }

    public FollowingGraphDao getFollowingGraphDao() {
        return followingGraphDao;
    }

    @Override
    public void setFollowingGraphDao(FollowingGraphDao followingGraphDao) {
        this.followingGraphDao = followingGraphDao;
    }

    public GraphCache getGraphCache() {
        return graphCache;
    }

    public FriendGraphDao getGraphDao() {
        return friendGraphDao;
    }
}
