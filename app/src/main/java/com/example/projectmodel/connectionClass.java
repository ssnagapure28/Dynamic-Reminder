package com.example.projectmodel;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class connectionClass {
    protected static String db="ToDo_Users_Database";
    //protected static String db="sampledb";
    protected static String ip="demo-database.c5mg6ogyu1pi.us-east-2.rds.amazonaws.com";
    //protected static String ip="10.0.2.2";
    protected static String port="3306";
    protected static String username="admin";
    //protected static String username="root";
    protected static String password="Mobilecloud";
    //protected static String password="Utki@141099";
    public Connection CONN()  {
        Connection conn=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString="jdbc:mysql://"+ip+":"+port+"/"+db;
            conn= DriverManager.getConnection(connectionString,username,password);
        }catch(Exception e)
        {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
        }

      return conn;
    }
}
