package com.feed.model;

public class Counter {
    private long uid;
    private long feedCount;
    private long friendCount;
    private long followerCount;

    public Counter(long uid, long feedCount, long friendCount, long followerCount) {
        this.uid = uid;
        this.feedCount = feedCount;
        this.friendCount = friendCount;
        this.followerCount = followerCount;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(long feedCount) {
        this.feedCount = feedCount;
    }

    public long getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(long friendCount) {
        this.friendCount = friendCount;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(long followerCount) {
        this.followerCount = followerCount;
    }
}
