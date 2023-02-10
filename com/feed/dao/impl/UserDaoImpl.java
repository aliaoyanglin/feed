package com.feed.dao.impl;

import com.feed.dao.UserDao;
import com.feed.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {
    // db client
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" +
            "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String dbUser = "root";
    private static final String dbPass = "02@violetc1210";
    private Connection connection = null;
    private Statement statement = null;

    @Override
    public long createUser(String name, String address, String phone) {
        int id = 0;
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL,dbUser,dbPass);

            System.out.println("increase users!");
            statement = connection.createStatement();
            String sql;
            sql = "INSERT INTO user (`name`, city, phone) VALUES ('"+name+"','"+address+"','"+phone+"')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int n = preparedStatement.executeUpdate();
            if(n == 1) System.out.println("insert successfully!");
            if(n == 0) System.out.println("insert failed!");
            if(n==1){
                sql = "SELECT id FROM user where city='"+address+"'";
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    id = rs.getInt("id");
                    System.out.println("inserted ID:" + id);
                }
                rs.close();
            }
            statement.close();
            connection.close();

        }catch (SQLException sqlE1){
            sqlE1.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(statement!=null) statement.close();
            }catch (SQLException sqlE2){
            }
        }
        try {
            if(connection!=null) connection.close();
        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }

        return id;
    }

    @Override
    public User getUser(long uid) {
        User user = new User();
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("get a user!");
            statement = connection.createStatement();
            String sql;

            sql = "SELECT * FROM user where id ='" + uid + "'";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                user.setUid(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAddress(rs.getString("city"));
                user.setPhone(rs.getString("phone"));
            }
            System.out.println(user.toJason(user));
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

        return user;
    }

    @Override
    public void deleteUser(long uid) {
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("delete a  user!");
            statement = connection.createStatement();
            String sql;
            sql = "DELETE from user where id = '"+uid+"'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();//delete completed


            preparedStatement.close();
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
    public Map<Long, User> getUsers(List<Long> uids) {
        Map<Long, User> userMapser = new HashMap<>();
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("get users!");
            statement = connection.createStatement();

            // 构建多uid查询的sql
            StringBuilder sqlBuilder = new StringBuilder(30 + uids.size() * 10);
            sqlBuilder.append("SELECT * FROM `user` WHERE FIND_IN_SET(id,'");
            for (long uid : uids) {
                sqlBuilder.append(uid).append(",");
            }
            sqlBuilder.replace(sqlBuilder.length() - 1, sqlBuilder.length(), "'");
            sqlBuilder.append(")");
            System.out.println("sql:" + sqlBuilder);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());

            ResultSet rs = preparedStatement.executeQuery(sqlBuilder.toString());
            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("id"));
                user.setAddress(rs.getString("city"));
                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                userMapser.put(user.getUid(), user);
//                System.out.println(userMapser.get(user.getUid()).toJason(userMapser.get(user.getUid()))+"  2");
            }
            rs.close();

            statement.close();
            connection.close();

            return userMapser;
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

        return userMapser;
    }
}
