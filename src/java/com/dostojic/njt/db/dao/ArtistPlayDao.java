/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.model.ArtistPlay;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dejan
 */
public class ArtistPlayDao extends GenericDao<ArtistPlay>{

    @Override
    protected ArtistPlay newDto() {
        return new ArtistPlay();
    }

    @Override
    protected void setPrimaryKey(ArtistPlay dto, long id) {
        dto.setId(id);
    }
    

    @Override
    protected void config() {
        setTableName("artist_play");
        setAutoIncrement(true);
        
        add(new ColumnMapper("id") {

            @Override
            public boolean isPrimaryKey() {
               return true; //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void resultSetToDto(ResultSet rs, ArtistPlay dto, int index) throws SQLException {
                dto.setId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(ArtistPlay dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getId());
            }
        });
        
        add(new ColumnMapper("artist_id") {
            @Override
            public void resultSetToDto(ResultSet rs, ArtistPlay dto, int index) throws SQLException {
                dto.setArtistId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(ArtistPlay dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getArtistId());
            }
        });
        
        add(new ColumnMapper("play_id") {
            @Override
            public void resultSetToDto(ResultSet rs, ArtistPlay dto, int index) throws SQLException {
                dto.setPlayId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(ArtistPlay dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getPlayId());
            }
        });
        
        add(new ColumnMapper("kind") {
            @Override
            public void resultSetToDto(ResultSet rs, ArtistPlay dto, int index) throws SQLException {
                dto.setKind(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(ArtistPlay dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getKind());
            }
        });
        
        add(new ColumnMapper("role") {
            @Override
            public void resultSetToDto(ResultSet rs, ArtistPlay dto, int index) throws SQLException {
                dto.setRole(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(ArtistPlay dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getRole());
            }
        });
    }
    
    private ArtistPlayDao(){
        
    }
    
    private static ArtistPlayDao instance;
    
    public static ArtistPlayDao getInstance(){
        if (instance == null){
            instance = new ArtistPlayDao();
        }
        return instance;
    }
}