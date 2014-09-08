/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.model.Artist;

/**
 *
 * @author dejan
 */

public class ArtistDao extends GenericDao<Artist> {

    public ArtistDao() {
    }

    @Override
    protected Artist newDto() {
        return new Artist();
    }

    @Override
    protected void setPrimaryKey(Artist dto, long id) {
        dto.setId(id);
    }
    
    
    @Override
    protected void config() {
        
    }
    /*
    
    @Override
    protected void config() {
        setTableName("artist");
        setAutoIncrement(true);
        
        add(new Field("id") {
            @Override
            public boolean isPrimaryKey() {
                return true;
            }

            @Override
            public void resultSetToDto(ResultSet rs, Artist dto, int index) throws SQLException {
                dto.setId(rs.getLong(index));
            }

            @Override
            public void dtoToParam(Artist dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getId());
            }
        });
        add(new Field("first_name") {
            @Override
            public void resultSetToDto(ResultSet rs, Artist dto, int index) throws SQLException {
                dto.setFirstName(rs.getString(index));
            }

            @Override
            public void dtoToParam(Artist dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getFirstName());
            }
        });
        add(new Field("last_name") {
            @Override
            public void resultSetToDto(ResultSet rs, Artist dto, int index) throws SQLException {
                dto.setLastName(rs.getString(index));
            }

            @Override
            public void dtoToParam(Artist dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getLastName());
            }
        });
        add(new Field("kind") {
            @Override
            public void resultSetToDto(ResultSet rs, Artist dto, int index) throws SQLException {
                dto.setKind(rs.getString(index));
            }

            @Override
            public void dtoToParam(Artist dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getKind());
            }
        });
        add(new Field("body") {
            @Override
            public void resultSetToDto(ResultSet rs, Artist dto, int index) throws SQLException {
                dto.setBody(rs.getString(index));
            }

            @Override
            public void dtoToParam(Artist dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, dto.getBody());
            }
        });
        
    }
    
    */
    private static ArtistDao instance;

    public static ArtistDao getInstance() {
        if (instance == null) {
            instance = new ArtistDao();
        }
        return instance;
    }
    
}
