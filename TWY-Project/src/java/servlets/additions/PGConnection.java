/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets.additions;

import java.sql.*;

/**
 *
 * @author irit
 */
public class PGConnection {
    
    String pg_user;
    String pg_pass;
    String host;
    String port;
    String db_name;
    Connection db_connect;
    
    public PGConnection(String host, String port, String db_name) {
        this.host = host;
        this.port = port;
        this.db_name = db_name;
        db_connect = null;
    }
    
    
    public boolean Connect(String name, String pass) {
        pg_user = name;
        pg_pass = pass;
        
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Cannot load postgresql JDBC driver");
            e.printStackTrace();
            return false;
        }
        try {
            String connect_url = "jdbc:postgresql://" + host + ":" + port + "/" + db_name;
            db_connect = DriverManager.getConnection(connect_url, pg_user, pg_pass);
            if (db_connect == null)
                return false;
            return true;
        }
        catch (SQLException e) {
            System.out.println("Cannot connect to database.");
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    /*
        Возвращает результат SELECT-запроса
    */
    public ResultSet SendSelectQuery(String query) {

        try {
            if (db_connect == null) {
                throw new SQLException("Connection is no defined");
            }
            Statement statement = db_connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                                             ResultSet.CONCUR_READ_ONLY);
            return statement.executeQuery(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public boolean SendInsertQuery(String query) {
        try {
            if (db_connect == null) {
                throw new SQLException("Connection is no defined");
            }
            Statement statement = db_connect.createStatement();
            statement.execute(query);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean checkConnection() {
        if (db_connect == null)
            return false;
        return true;
    }
}
