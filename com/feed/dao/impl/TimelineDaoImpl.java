package com.feed.dao.impl;

import com.feed.dao.TimelineDao;
import com.feed.model.Timeline;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TimelineDaoImpl implements TimelineDao {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" +
            "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String dbUser = "root";
    private static final String dbPass = "02@violetc1210";
    private Connection connection = null;
    private Statement statement = null;

    @Override
    public void setTimeline(long uid, long id, Date created) {
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL,dbUser,dbPass);

            System.out.println("increase timeline!");
            statement = connection.createStatement();
            String sql;
            sql = "INSERT INTO timeline VALUES ('"+id+"','"+uid+"','"+created+"')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int n = preparedStatement.executeUpdate();
            if(n == 1) System.out.println("insert successfully!");
            if(n == 0) System.out.println("insert failed!");
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

    }

    @Override
    public List<Timeline> getTimeline(long uid) {
        List<Timeline> timelineList = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("get a timeline!");
            statement = connection.createStatement();
            String sql;

            sql = "SELECT * FROM timeline where uid ='" + uid + "'";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Timeline timeline = new Timeline();
                timeline.setId(rs.getInt("fid"));
                timeline.setUid(rs.getInt("uid"));
                timeline.setCreated(rs.getTimestamp("created"));
                timelineList.add(timeline);
            }
            rs.close();
            timelineList.sort(Comparator.comparing(Timeline::getId).reversed());
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
        return timelineList;
    }

    @Override
    public void deleteTimeline(long id) {
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("delete a  user!");
            statement = connection.createStatement();
            String sql;
            sql = "DELETE from timeline where fid = '"+id+"'";
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
}
