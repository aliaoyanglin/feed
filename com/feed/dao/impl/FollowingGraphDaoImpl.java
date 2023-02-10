package com.feed.dao.impl;

import com.feed.dao.FollowingGraphDao;
import com.feed.dao.FriendGraphDao;
import com.feed.model.Attention;
import com.feed.model.Friend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝分库分表逻辑:全部数据分2个库,每个库2个表;
 */
public class FollowingGraphDaoImpl implements FollowingGraphDao {
    // db client
//    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost:3306/" +
//            "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//
//    private static final String dbUser = "root";
//    private static final String dbPass = "02@violetc1210";
    private List<Connection> dbConnections = null;
    private int tablesPerDB;
    private Statement statement = null;

    public FollowingGraphDaoImpl(int tablesPerDB, List<Connection> dbConnections) {
        this.tablesPerDB = tablesPerDB;
        this.dbConnections = dbConnections;
    }

    @Override
    public void addFollowing(Friend friend) {
        try {
//            Class.forName(JDBC_DRIVER);
//
//            //start connecting to feedsys.
//            System.out.println("connect to feedsys!");
//            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);
            int allTablesCount = this.tablesPerDB * this.dbConnections.size();
            int tableIdx = (int)(friend.getUid() % allTablesCount);
            int dbIdx = tableIdx / this.dbConnections.size();

            System.out.println("add a following!");
            Connection connection = this.dbConnections.get(dbIdx);
            statement = connection.createStatement();
            String sql;
            for (int i = 0; friend.getAttention() != null && i < friend.getAttention().size(); i++) {
                sql = "INSERT INTO following VALUES (?, ?, ?)";
//                sql = "INSERT INTO following VALUES ('" + friend.getAttention().get(i).getUid() + "', '" + friend.getUid() + "', '" + friend.getAttention().get(i).getCreated() + "')";
                PreparedStatement preparedStatement = connection.pre
                preparedStatement.executeUpdate();
            }

            statement.close();
            connection.close();

        } catch (SQLException sqlE1) {
            sqlE1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException sqlE2) {
            }
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    @Override
    public void deleteFollowing(Friend friend) {
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("increase users!");
            statement = connection.createStatement();

            String sql;
            for (int i = 0; friend.getAttention() != null && i < friend.getAttention().size(); i++) {
                sql = "DELETE FROM following WHERE from_uid='" + friend.getAttention().get(i).getUid() + "' and to_uid='" + friend.getUid() + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                System.out.println("deleted id : " + friend.getAttention().get(i).getUid());
            }

            statement.close();
            connection.close();

        } catch (SQLException sqlE1) {
            sqlE1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException sqlE2) {
            }
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    @Override
    public Friend getFollowings(long uid) {
        List<Attention> friendSwap = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("get a following!");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT to_uid,created_date FROM following WHERE from_uid = '" + uid + "'";

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                Attention attention = new Attention();
                attention.setUid(rs.getInt("to_uid"));
                attention.setCreated(rs.getTimestamp("created_date"));
                friendSwap.add(attention);
                System.out.println("get the uid:"+attention.getUid());
            }

            statement.close();
            connection.close();

        } catch (SQLException sqlE1) {
            sqlE1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException sqlE2) {
            }
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return new Friend(uid, friendSwap);
    }
}
