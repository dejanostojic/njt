/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dejan
 */
public class UserDao extends GenericDao<User>{

    @Override
    protected User newDto() {
        return new User();
    }

    @Override
    protected void setPrimaryKey(User dto, long id) {
        dto.setId(id);
    }
    

    @Override
    protected void config() {
        setTableName("user");
        setAutoIncrement(true);
        
        add(new ColumnMapper("id") {

            @Override
            public boolean isPrimaryKey() { return true; }
            
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getId());
            }
        });
        
        add(new ColumnMapper("user_name") {
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setUserName(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getUserName());
            }
        });
        
        add(new ColumnMapper("password") {
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setPassword(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getPassword());
            }
        });
        
        add(new ColumnMapper("first_name") {
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setFirstName(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getFirstName());
            }
        });
        add(new ColumnMapper("last_name") {
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setLastName(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getLastName());
            }
        });
        
        add(new ColumnMapper("email") {
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setEmail(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getEmail());
            }
        });
        
        add(new ColumnMapper("kind") {
            @Override
            public void resultSetToDto(ResultSet rs, User dto, int index) throws SQLException {
                dto.setKind(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(User dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getKind());
            }
        });
        
    }
    
    public User loadByUsername(String userName){
        return loadFirst("user_name='" + userName + "'");
    }
    
    private static UserDao instance;
    
    public static UserDao getInstance(){
        if (instance == null){
            instance = new UserDao();
        }
        return instance;
    }
    
    
}
