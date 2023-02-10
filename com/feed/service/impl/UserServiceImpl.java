package com.feed.service.impl;

import com.feed.cache.UserCache;
import com.feed.dao.UserDao;
import com.feed.model.User;
import com.feed.service.UserService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserCache userCache;
    private UserDao userDao;

    @Override
    public User addUser(String name, String address, String phone) {
//         insert to db
        long uid = userDao.createUser(name, address, phone);
        User user = userDao.getUser(uid);

//         set to cache
        userCache.setUser(user);
        return user;
    }

    @Override
    public void deleteUser(long uid) {
        userDao.deleteUser(uid);
        userCache.deleteUser(uid);
    }

    @Override
    public void getAllUsers(List<Long> uids) {
        userDao.getUsers(uids);
    }

    @Override
    public List<User> getUsers(long[] uids) {
        // get from cache
        Map<Long, User> usersFrmCache = userCache.getUsers(uids);

        // find users who are not in cache
        List<Long> missedUids = new LinkedList<>();
//        if (usersFrmCache == null) {
//            for (long uid : uids)
//                missedUids.add(uid);
//        } else
        for (long uid : uids) {
            if (usersFrmCache.get(uid) != null)
                continue;
            missedUids.add(uid);
        }

        // get missed users from db
        Map<Long, User> usersFrmDb = userDao.getUsers(missedUids);

        List<User> users = new ArrayList<>();
        for (long uid : uids) {
            User u = usersFrmCache.get(uid);
            if (u == null) {
                u = usersFrmDb.get(uid);
//                System.out.println(u.toJason(u) + " 1");
            }
            if (u != null) {
                users.add(u);
            }
        }
        return users;
    }

    @Override
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void setCache(UserCache userCache) {
        this.userCache = userCache;
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
