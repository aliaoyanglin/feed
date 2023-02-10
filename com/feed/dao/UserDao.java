package com.feed.dao;

import com.feed.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    long createUser(String name, String address, String phone);
    User getUser(long uid);
    void deleteUser(long uid);
    Map<Long, User> getUsers(List<Long> uids);
}
