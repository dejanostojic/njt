/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.util;


import com.dostojic.njt.db.Database;
import com.dostojic.njt.db.ResultSetProcessor;
import com.dostojic.njt.db.WrappedSQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author dejan
 */
public class QueryUtils {
    
    public QueryUtils() {
    }
    
    public static String stringLiteral(String value) {
    if (value == null) {
        return "NULL";
    }
        return mySqlStringLiteral(value);
    }

    public static String mySqlStringLiteral(String value) {
        StringBuilder sql = new StringBuilder();
        sql.append('\'');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\0':
                    sql.append("\\0");
                    break;
                case '\n':
                    sql.append("\\n");
                    break;
                case '\r':
                    sql.append("\\r");
                    break;
                case '\\':
                    sql.append("\\\\");
                    break;
                case '\"':
                    sql.append("\\\"");
                    break;
                case '\'':
                    sql.append("\\\'");
                    break;
                case (char) 26:
                    sql.append("\\z");
                    break;
                default:
                    sql.append(c);
            }
        }
        sql.append('\'');
        return sql.toString();
    }
    
    public static String mySqlLikeLiteral(String value) {
        return mySqlStringLiteral("%" + value + "%");
    }
    
    public static void forEach(String sql, ResultSetProcessor closure) {
        try {
            Connection conn = Database.getCurrent().getConnection();
            Statement s = conn.createStatement();
            try (ResultSet r = s.executeQuery(sql)) {
                while (r.next()) {
                    closure.process(r);
                }
            } finally {
                s.close();
            }
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
    
    public static int exec(String sql) {
        try {
            Connection conn = Database.getCurrent().getConnection();
            try (Statement s = conn.createStatement()) {
                return s.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new WrappedSQLException(e);
        }
    }
}
