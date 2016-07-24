package com.matcha.jjbros.matchaapp.db;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by jylee on 2016-07-09.
 */
public class DBConnection {

    public static Connection getConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/matcha";
            Properties props = new Properties();
            props.setProperty("user","postgres");
            props.setProperty("password","admin123");
            props.setProperty("ssl","true");
            conn = DriverManager.getConnection(url, props);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(2);
        }
        return conn;
    }

    public static void Close(Connection con){
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void Close(Statement stmt){
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void Close(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean isConnected(Connection con){
        boolean validConnection = true;
        try {
            if(con==null||con.isClosed())
                validConnection = false;
        } catch (SQLException e) {
            validConnection = false;
            e.printStackTrace();
        }
        return validConnection;
    }
    public static void Commit(Connection con){
        try {
            if(isConnected(con)){
                con.commit();
                Log.d("JdbcTemplate.Commit", "DB Successfully Committed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void Rollback(Connection con){
        try {
            if(isConnected(con)){
                con.rollback();
                Log.d("JdbcTemplate.rollback", "DB Successfully Rollbacked!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
