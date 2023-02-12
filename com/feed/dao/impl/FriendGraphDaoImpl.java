package com.feed.dao.impl;

import com.feed.dao.FriendGraphDao;
import com.feed.model.Attention;
import com.feed.model.Friend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendGraphDaoImpl implements FriendGraphDao {
    // db client
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" +
            "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String dbUser = "root";
    private static final String dbPass = "02@violetc1210";
    private Connection connection = null;
    private Statement statement = null;

    @Override
    public void addFriend(Friend friend) {
        try {
//            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("increase a friend!");
            statement = connection.createStatement();
            String sql;
            for (int i = 0;i < friend.getAttention().size(); i++) {
                sql = "INSERT INTO friend VALUES ('" + friend.getUid() + "', '" + friend.getAttention().get(i).getUid() + "','" + friend.getAttention().get(i).getCreated() + "')";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
            }

            statement.close();
//            connection.close();

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
        System.out.println("after addFri:"+friend.toJason());
    }

    @Override
    public void deleteFriend(Friend friend) {
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("increase users!");
            statement = connection.createStatement();

            String sql;
            //delete a friend
            for (int i = 0; friend.getAttention() != null && i < friend.getAttention().size(); i++) {
                sql = "DELETE FROM friend WHERE to_uid='" + friend.getAttention().get(i).getUid() + "' and from_uid='" + friend.getUid() + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                System.out.println("deleted friend: id : " + friend.getAttention().get(i).getUid());
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
    public Friend getFriends(long uid) {
        List<Attention> friendSwap = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("research friends!");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT to_uid,created_date FROM friend WHERE from_uid = '" + uid + "'";

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                Attention attention = new Attention();
                attention.setUid(rs.getInt("to_uid"));
                attention.setCreated(rs.getTimestamp("created_date"));
                friendSwap.add(attention);
                System.out.println("get the uid:"+attention.getUid());
            }

            rs.close();
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