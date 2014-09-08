/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.model.Seat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author dejan
 */
public class SeatDao extends GenericDao<Seat>{

    @Override
    protected Seat newDto() {
        return new Seat();
    }

    @Override
    protected void setPrimaryKey(Seat dto, long id) {
        dto.setId(id);
    }
    
    

    @Override
    protected void config() {
        setTableName("seat");
        setAutoIncrement(true);
        
        add(new ColumnMapper("id") {

            @Override
            public boolean isPrimaryKey() {
                return true;
            }
            
            @Override
            public void resultSetToDto(ResultSet rs, Seat seat, int index) throws SQLException {
                seat.setId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Seat seat, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex,  seat.getId());
            }
        });
        
        add(new ColumnMapper("seat_row"){

            @Override
            public void resultSetToDto(ResultSet rs, Seat seat, int index) throws SQLException {
                seat.setRow(rs.getInt(index));
            }

            @Override
            public void dtoToParam(Seat seat, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setInt(paramIndex,seat.getRow());
            }
        });
        
        add(new ColumnMapper("seat_column"){

            @Override
            public void resultSetToDto(ResultSet rs, Seat seat, int index) throws SQLException {
                seat.setColumn(rs.getInt(index));
            }

            @Override
            public void dtoToParam(Seat seat, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setInt(paramIndex,seat.getColumn());
            }
        });
        
        add(new ColumnMapper("kind") {
            @Override
            public void resultSetToDto(ResultSet rs, Seat seat, int index) throws SQLException {
                seat.setKind(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(Seat seat, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, seat.getKind());
            }
        });
        
        add(new ColumnMapper("stage_id") {
            @Override
            public void resultSetToDto(ResultSet rs, Seat seat, int index) throws SQLException {
                seat.setStageId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Seat seat, PreparedStatement statement, int paramIndex) throws SQLException {
               statement.setLong(paramIndex, seat.getStageId());
            }
        });
    }
    
    private static SeatDao instance;

    public static SeatDao getInstance() {
        if (instance == null){
            instance = new SeatDao();
        }
        return instance;
    }
    
    public  List<Seat> loadByStageId(long stageId){
        return loadList("stage_id="+stageId, null);
    }
    
    
    
}
