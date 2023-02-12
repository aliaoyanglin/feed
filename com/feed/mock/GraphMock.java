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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GraphMock {
    private final String graphMcHost = "10.226.52.80";
    private final int graphMcPort = 11211;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL0 = "jdbc:mysql://localhost:3306/" + "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_URL1 = "jdbc:mysql://localhost:3306/" + "follow_0?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_URL2 = "jdbc:mysql://localhost:3306/" + "follow_1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_URL3 = "jdbc:mysql://localhost:3306/" + "follow_2?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_URL4 = "jdbc:mysql://localhost:3306/" + "follow_3?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_URL5 = "jdbc:mysql://localhost:3306/" + "fri_0?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_URL6 = "jdbc:mysql://localhost:3306/" + "fri_1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String dbUser = "root";
    private final String dbPass = "02@violetc1210";
    private final int tablesPerDbFol = 2;
    private int tablesPerDbFri = 4;
    private List<Connection> dbConnectionsFol = new ArrayList<>();
    private List<Connection> dbConnectionsFri = new ArrayList<>();

    private GraphService graphService;

    public static void main(String[] args) throws SQLException {
        GraphMock graphMock = new GraphMock();
        GraphService graphService = graphMock.buildService();
        GraphCache graphCache = graphMock.buildCache(graphMock.getGraphMcHost(), graphMock.getGraphMcPort());
        FollowingGraphDao followingGraphDao = graphMock.buildFollowingGraphDao(JDBC_DRIVER, graphMock.getTablesPerDbFol(), graphMock.getDbConnectionsFol());
//        FriendGraphDao friendGraphDao = graphMock.buildFriendGraphDao(JDBC_DRIVER, graphMock.getTablesPerDbFol(), graphMock.dbConnectionsFol);
        graphMock.setGraphService(graphService);
        graphService.setGraphCache(graphCache);
        graphService.setFollowingGraphDao(followingGraphDao);

        //graph function check
        graphMock.addFriend();//success
//        graphMock.getFriend();//success
//        graphMock.getFollowing();//success
//        graphMock.deleteFriend();//success

    }

    public int getTablesPerDbFol() {
        return 2;
    }

    public int getTablesPerDbFri() {
        return tablesPerDbFri;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public List<Connection> getDbConnectionsFol() throws SQLException {
        List<Connection> dbConnectionsFol = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            dbConnectionsFol.add(DriverManager.getConnection(DB_URL1, dbUser, dbPass));
            dbConnectionsFol.add(DriverManager.getConnection(DB_URL2, dbUser, dbPass));
            dbConnectionsFol.add(DriverManager.getConnection(DB_URL3, dbUser, dbPass));
            dbConnectionsFol.add(DriverManager.getConnection(DB_URL4, dbUser, dbPass));
            this.dbConnectionsFol = dbConnectionsFol;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dbConnectionsFol;
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

    public FollowingGraphDao buildFollowingGraphDao(String JDBC_DRIVER, int tablesPerDB, List<Connection> dbConnections){
        return new FollowingGraphDaoImpl(JDBC_DRIVER, tablesPerDbFol, dbConnectionsFol);
    }
//    public FollowingGraphDao buildFriendGraphDao(String JDBC_DRIVER, int tablesPerDB, List<Connection> dbConnections){
//        return new FriendGraphDaoImpl(JDBC_DRIVER, tablesPerDbFri, dbConnectionsFri);
//    }

    public void setGraphService(GraphService graphService){
        this.graphService = graphService;
    }

    //function
    public void addFriend() {
        int startUid = 10201;
        int endUid = 10300;
        int endFriUid = endUid;
        int step = 1;
        System.out.println("Add friend...");
        for (long uid = startUid; uid < endUid; uid++) {
            List<Attention> attentionList = new ArrayList<>();
            for (long friUid = uid + 1; friUid < endFriUid; friUid += step) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Attention attention = new Attention(friUid, timestamp);
                attentionList.add(attention);
                System.out.println("build:"+uid+"---"+friUid);
            }
            System.out.println("after build:"+uid+"---"+attentionList);
            Friend friend = new Friend(uid, attentionList);
            System.out.println("2 build:"+uid+"---"+attentionList);
            graphService.addFriend(friend);
            System.out.println("3 build:"+uid+"---"+attentionList);
            System.out.println("will add following:"+friend.toJason());
            graphService.addFollowing(friend);

        }
    }


    public void getFriend(){
        long uid = 10220;
        List<Attention> attentionList = graphService.getFriends(uid);
        for(Attention attention : attentionList) {
            System.out.println("uid:"+uid+" friendId:"+attention.toJason());
        }
    }

    public void getFollowing(){
        long uid = 10241;
        List<Attention> attentionList = graphService.getFollowings(uid);
        for(Attention attention : attentionList) {
            System.out.println("uid:"+uid+" followingId:"+attention.toJason());
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
