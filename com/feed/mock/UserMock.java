package com.feed.mock;

import com.feed.cache.UserCache;
import com.feed.cache.impl.UserCacheImpl;
import com.feed.dao.UserDao;
import com.feed.dao.impl.UserDaoImpl;
import com.feed.model.User;
import com.feed.service.UserService;
import com.feed.service.impl.UserServiceImpl;

import java.util.*;

public class UserMock {
    private String userMcHost = "10.226.52.80";
    private int userMcPort = 11211;
    private String userDb = "root";
    private UserService userService;

    public static void main(String[] args) {

        UserMock mock = new UserMock();
        UserService userService = mock.buildUserService();
        UserCache userCache = mock.buildUserCache(mock.getUserMcHost(), mock.getUserMcPort());
        mock.setUserService(userService);
        userService.setCache(userCache);

        //user function check
//        mock.createUsers(); //success!
//        mock.getUsers(getUsersLong(10200L, 10400L));//success!
//        mock.deleteUsers(); //success!
//        mock.getAllUsers(getUsersList(10200L, 10400L));//success!


    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getUserMcHost() {
        return userMcHost;
    }

    public int getUserMcPort() {
        return userMcPort;
    }

    public UserService buildUserService() {
        UserDao userDao = new UserDaoImpl();
        UserCache userCache = new UserCacheImpl();

        UserService userService = new UserServiceImpl();
        userService.setUserDao(userDao);
        userService.setCache(userCache);
        return userService;
    }

    public UserCache buildUserCache(String userMcHost, int userMcPort){
        return new UserCacheImpl(userMcHost, userMcPort);
    }

    public void createUsers() {
        for (int i = 10399; i < 104002; i++) {
            String name = "user_" + i;
            String address = "address_" + i;
            String phone = "138000" + i;
            userService.addUser(name, address, phone);
        }
    }

    public void deleteUsers(){
        for(int i = 10000;i<10200;i++){
            userService.deleteUser(i);
        }
    }

    public static List<Long> getUsersList(Long startId, Long endId){
        List<Long> uids = new LinkedList<>();

        for(Long i = startId;i.intValue()<endId;i++)
            uids.add(i);
        return uids;
    }

    public static long[] getUsersLong(Long startId, Long endId){
        long[] uids = new long[endId.intValue()-startId.intValue()];

        for(Long i = startId;i.intValue()<endId;i++)
            uids[i.intValue() - startId.intValue()] = i;
        return uids;
    }

    private void getUsers(Long startId, Long endId) {

    }

    public void getAllUsers(List<Long> uids){
        userService.getAllUsers(uids);
    }

    public void getUsers(long[] uids) {
        List<User> uidLists = userService.getUsers(uids);
        for (User uidList : uidLists) {
            System.out.println(uidList.toJason(uidList));
        }
    }
}
