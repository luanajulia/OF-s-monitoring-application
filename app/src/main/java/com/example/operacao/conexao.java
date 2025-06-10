package com.example.operacao;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexao {
    String classes = "net.sourceforge.jtds.jdbc.Driver";

    protected static  String ip = "";

    protected static String port = "1433";

    protected static String db = "comunidade";

    protected static String un = "";

    protected static String password = "";

    public Connection CONN(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {
            Class.forName(classes);
            String conUrl = "jdbc:jtds:sqlserver://"+ip+":"+port+";"+ db;
            conn = DriverManager.getConnection(conUrl, un, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;


    }
}
