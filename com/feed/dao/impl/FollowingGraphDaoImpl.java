package com.feed.dao.impl;

import com.feed.dao.FollowingGraphDao;
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
    private String JDBC_DRIVER;
//    private Connection connection = null;

    public FollowingGraphDaoImpl(String JDBC_DRIVER, int tablesPerDB, List<Connection> dbConnections) {
        this.JDBC_DRIVER = JDBC_DRIVER;
        this.tablesPerDB = tablesPerDB;
        this.dbConnections = dbConnections;
    }
    public FollowingGraphDaoImpl() {}

    @Override
    public void addFollowing(Friend friend) {
        try {
            int allTablesCount = this.tablesPerDB * this.dbConnections.size();

            long fromUid = friend.getUid();
            System.out.println("add a following:" + fromUid);
            for (Attention toAttention: friend.getAttention()) {
                long toUid = toAttention.getUid();
                int tableIdx = (int)(toUid % allTablesCount);
                int dbIdx = tableIdx / this.tablesPerDB;

                System.out.println("Insert following:" + fromUid + "---" + toUid);
                Connection connection = dbConnections.get(dbIdx);

                String sql = "INSERT INTO following_"+tableIdx+" VALUES ('" + fromUid + "', '"
                        + toUid + "', '" + toAttention.getCreated() + "')";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();

            }

        } catch (SQLException sqlE1) {
            sqlE1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFollowing(Friend friend) {
        try {
            Class.forName(JDBC_DRIVER);

            int allTablesCount = this.tablesPerDB * this.dbConnections.size();
            int tableIdx = (int)(friend.getUid() % allTablesCount);
            int dbIdx = tableIdx / this.dbConnections.size();

            System.out.println("increase users!");

            Connection connection = this.dbConnections.get(dbIdx);
            String sql;
            for (int i = 0; friend.getAttention() != null && i < friend.getAttention().size(); i++) {
                sql = "DELETE FROM following_"+tableIdx+" WHERE from_uid='"
                        + friend.getAttention().get(i).getUid() + "' and to_uid='" + friend.getUid() + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                System.out.println("deleted id : " + friend.getAttention().get(i).getUid());
            }

            connection.close();

        } catch (SQLException sqlE1) {
            sqlE1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for(Connection connection : dbConnections) {
                    if (connection != null) connection.close();
                }
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }

    @Override
    public Friend getFollowings(long uid) {
        List<Attention> friendSwap = new ArrayList<>();
        try {
            int allTablesCount = this.tablesPerDB * this.dbConnections.size();
            int tableIdx = (int) (uid % allTablesCount);
            int dbIdx = tableIdx / this.dbConnections.size();

            Class.forName(JDBC_DRIVER);

            System.out.println("get a following!");
            Connection connection = this.dbConnections.get(dbIdx);
            String sql;
            sql = "SELECT to_uid,created_date FROM following_" + dbIdx + " WHERE from_uid = '" + uid + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                Attention attention = new Attention();
                attention.setUid(rs.getInt("from_uid"));
                attention.setCreated(rs.getTimestamp("created"));
                friendSwap.add(attention);
                System.out.println("get the uid:" + attention.getUid());
            }

            preparedStatement.close();
            connection.close();

        } catch (SQLException sqlE1) {
            sqlE1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                for (Connection connection : dbConnections) {
                    if (connection != null) connection.close();
                }
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }

        return new Friend(uid, friendSwap);
    }
}
