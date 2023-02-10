package com.feed.mock;

import com.feed.cache.GraphCache;
import com.feed.cache.impl.GraphCacheImpl;
import com.feed.dao.FollowingGraphDao;
import com.feed.dao.FriendGraphDao;
import com.feed.dao.impl.FollowingGraphDaoImpl;
import com.feed.dao.impl.FriendGraphDaoImpl;
import com.feed.model.Attention;
import com.feed.model.Friend;
import com.feed.service.GraphService;
import com.feed.service.impl.GraphServiceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GraphMock {
    private String graphMcHost = "10.226.52.80";
    private int graphMcPort = 11211;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" +
            "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String dbUser = "root";
    private static final String dbPass = "02@violetc1210";

    private GraphService graphService;

    public static void main(String[] args) {
        GraphMock graphMock = new GraphMock();
        GraphService graphService = graphMock.buildService();
        GraphCache graphCache = graphMock.buildCache(graphMock.getGraphMcHost(), graphMock.getGraphMcPort());
        graphMock.setGraphService(graphService);
        graphService.setGraphCache(graphCache);

        //graph function check
//        graphMock.addFriend();//success
//        graphMock.getFriend();//success
//        graphMock.getFollowing();//success
//        graphMock.deleteFriend();//success

    }

    public String getGraphMcHost() {
        return graphMcHost;
    }

    public int getGraphMcPort() {
        return graphMcPort;
    }

    public GraphService getGraphService() {
        return graphService;
    }

    public GraphService buildService(){
        GraphCache graphCache = new GraphCacheImpl();
        FollowingGraphDao followingGraphDao = new FollowingGraphDaoImpl();
        FriendGraphDao friendGraphDao = new FriendGraphDaoImpl();

        GraphService graphService = new GraphServiceImpl();
        graphService.setGraphDao(friendGraphDao);
        graphService.setFollowingGraphDao(followingGraphDao);
        graphService.setGraphCache(graphCache);
        return graphService;
    }

    public GraphCache buildCache(String graphMcHost, int graphMcPort){
        return new GraphCacheImpl(graphMcHost, graphMcPort);
    }

    public void setGraphService(GraphService graphService){
        this.graphService = graphService;
    }

    //function
    public void addFriend(){
        for(long id = 10200;id<10400;id++) {
            for(long uid = id+1;uid<10400;uid+=20) {
                List<Attention> attentionList = new ArrayList<>();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Attention attention = new Attention(uid, timestamp);
                attentionList.add(attention);
                Friend friend = new Friend(id, attentionList);
                graphService.addFriend(friend);
                graphService.addFollowing(friend);
            }
        }
    }

    public void getFriend(){
        long uid = 10220;
        List<Attention> attentionList = graphService.getFriends(uid);
        for(Attention attention : attentionList) {
            System.out.println("uid:"+uid+" friendId:"+attention.toJason(attention));
        }
    }
    public void getFollowing(){
        long uid = 10241;
        List<Attention> attentionList = graphService.getFollowings(uid);
        for(Attention attention : attentionList) {
            System.out.println("uid:"+uid+" followingId:"+attention.toJason(attention));
        }
    }

    public void deleteFriend(){
        long uid = 10220;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Attention attention = new Attention(10241,timestamp);
        List<Attention> attentionList = new ArrayList<>();
        attentionList.add(attention);
        graphService.deleteFriend(new Friend(uid,attentionList));
        graphService.deleteFollowing(new Friend(uid,attentionList));
    }
}
