package com.feed.dao.impl;

import com.feed.dao.ContentDao;
import com.feed.model.Content;

import java.sql.*;

public class ContentDaoImpl implements ContentDao {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" +
            "feedsys?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String dbUser = "root";
    private static final String dbPass = "02@violetc1210";
    private Connection connection = null;
    private Statement statement = null;

    @Override
    public long addContent(long fatherId, long uid, String txt, Timestamp created) {
        int id = 0;
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL,dbUser,dbPass);

            System.out.println("increase a content!");
            statement = connection.createStatement();
            String sql;
            sql = "INSERT INTO feed_content (fatherId, uid, content, created) VALUES ('"+fatherId+"', '"+uid+"', '"+txt+"', '"+created+"')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int n = preparedStatement.executeUpdate();
            if(n == 1) System.out.println("insert successfully!");
            if(n == 0) System.out.println("insert failed!");
            if(n==1){
                sql = "SELECT id FROM feed_content where uid='"+uid+"'";
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    id = rs.getInt("id");
                }
                rs.close();
            }
            statement.close();
            connection.close();
            System.out.println("inserted ID:" + id);
            return id;

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
    public void deleteContents(long id) {
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("delete a  Content!");
            statement = connection.createStatement();
            String sql;
            sql = "DELETE from feed_content where id = '"+id+"'";
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
    public Content getContent(long id) {
        Content content = new Content();
        content.setId(id);
        try {
            Class.forName(JDBC_DRIVER);

            //start connecting to feedsys.
            System.out.println("connect to feedsys!");
            connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);

            System.out.println("get a Content!");
            statement = connection.createStatement();
            String sql;
            sql = "SELECT * from feed_content WHERE id="+id+"";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeQuery();

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                content.setFatherId(rs.getInt("fatherId"));
                content.setUid(rs.getInt("Uid"));
                content.setContent(rs.getString("content"));
                content.setCreated(rs.getTimestamp("created"));
            }

            preparedStatement.close();
            statement.close();
            connection.close();
            return content;

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
        return content;
    }
}
