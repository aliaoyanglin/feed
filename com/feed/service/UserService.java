package com.feed.service;

import com.feed.cache.UserCache;
import com.feed.dao.UserDao;
import com.feed.model.User;

import java.util.List;

public interface UserService {
    User addUser(String name, String address, String phone);
    void deleteUser(long uid);
    void getAllUsers(List<Long> uids);
    List<User> getUsers(long[] uids);

    void setUserDao(UserDao userDao);

    void setCache(UserCache userCache);
}
