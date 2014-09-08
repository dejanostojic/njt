/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 *
 * @author dejan
 */

public class Database {
    
    final private static ThreadLocal<Database> dbTLocal = new ThreadLocal<>();
    
    public static Database getCurrent() {
        Database instance = dbTLocal.get();
        if(instance == null) {
            instance = new Database();
            dbTLocal.set(instance);
        }
        return instance;
    }
    
    public static void clear() {
        try {
            Database instance = (Database) dbTLocal.get();
            if(instance != null) {
                if(instance.connection != null)
                    instance.connection.close();
                dbTLocal.set(null);
            }
        } catch (SQLException e){ throw new RuntimeException(e);}
    }
    
    public DataSource getDataSource() {
        try {
            InitialContext ic=new InitialContext();
            Context envCtx = (Context) ic.lookup("java:comp/env");
            DataSource dataSource=(DataSource)envCtx.lookup("jdbc/njtDB");
            return dataSource;
        } catch (NamingException e){ throw new RuntimeException(e);}
    }
    
    private Connection connection = null;
    
    
    public  Connection getConnection() {
        try {
            if(connection == null) {
                connection = getDataSource().getConnection();
            }
            
            return connection;
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    
    public Statement createStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    
    public PreparedStatement perepareStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    
    public void beginTransaction()  {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    
    public boolean isInTransaction() {
        try {
            return ! getConnection().getAutoCommit();
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    public void rollback() {
        try {
            if(isInTransaction()) {
                getConnection().rollback();
                getConnection().setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    
    public void commit()  {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }

}
