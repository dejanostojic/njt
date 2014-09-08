/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.play.model.Play;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dejan
 */
public class PlayDao extends GenericDao<Play>{

    @Override
    protected Play newDto() {
        return new Play();
    }

    @Override
    protected void setPrimaryKey(Play dto, long id) {
        dto.setId(id);
    }
    
    @Override
    protected void config() {
        setTableName("play");
        setAutoIncrement(true);
        
        add(new ColumnMapper("id") {

            @Override
            public boolean isPrimaryKey() {
               return true; //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void resultSetToDto(ResultSet rs, Play dto, int index) throws SQLException {
                dto.setId(rs.getLong(index));
            }

            @Override
            public void dtoToParam(Play dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getId());
            }
        });
        
        add(new ColumnMapper("title") {
            @Override
            public void resultSetToDto(ResultSet rs, Play dto, int index) throws SQLException {
                dto.setTitle(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(Play dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getTitle());
            }
        });

        add(new ColumnMapper("about") {
            @Override
            public void resultSetToDto(ResultSet rs, Play dto, int index) throws SQLException {
                dto.setAbout(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(Play dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getAbout());
            }
        });
    }
    
    private static PlayDao instance;
    
    public static PlayDao getInstance(){
        if (instance == null){
            instance = new PlayDao();
        }
        return instance;
    }
    
}
