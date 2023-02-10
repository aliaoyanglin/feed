package com.feed.model;

import java.util.Set;

public class Follower {
    private long uid;
    private Set<Long> followers;

    public Follower(long uid, Set<Long> followers) {
        this.uid = uid;
        this.followers = followers;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Set<Long> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Long> followers) {
        this.followers = followers;
    }


}
