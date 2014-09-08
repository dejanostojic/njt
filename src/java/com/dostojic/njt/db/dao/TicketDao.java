/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.model.Ticket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dejan
 */
public class TicketDao extends GenericDao<Ticket>{

    @Override
    protected Ticket newDto() {
        return new Ticket();
    }

    @Override
    protected void setPrimaryKey(Ticket dto, long id) {
        dto.setId(id);
    }

    @Override
    protected void config() {
        setTableName("ticket");
        
        add(new ColumnMapper("id") {

            @Override
            public boolean isPrimaryKey() {
                return true;
            }
            
            @Override
            public void resultSetToDto(ResultSet rs, Ticket ticket, int index) throws SQLException {
                ticket.setId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Ticket ticket, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, ticket.getId());
            }
        });
        
        add(new ColumnMapper("seat_id") {

            @Override
            public void resultSetToDto(ResultSet rs, Ticket ticket, int index) throws SQLException {
                ticket.setSeatId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Ticket ticket, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, ticket.getSeatId());
            }
        });
        
        add(new ColumnMapper("performance_id") {

            @Override
            public void resultSetToDto(ResultSet rs, Ticket ticket, int index) throws SQLException {
                ticket.setPerformanceId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Ticket ticket, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, ticket.getPerformanceId());
            }
        });
    
        add(new ColumnMapper("owner_name") {

            @Override
            public void resultSetToDto(ResultSet rs, Ticket ticket, int index) throws SQLException {
                ticket.setOwnerName(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(Ticket ticket, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, ticket.getOwnerName());
            }
        });
        
        add(new ColumnMapper("price") {
            @Override
            public void resultSetToDto(ResultSet rs, Ticket ticket, int index) throws SQLException {
                ticket.setPrice(rs.getDouble(index));
            }
            
            @Override
            public void dtoToParam(Ticket ticket, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setDouble(paramIndex, ticket.getPrice());
            }
        });
        
        add(new ColumnMapper("status") {
            @Override
            public void resultSetToDto(ResultSet rs, Ticket ticket, int index) throws SQLException {
                ticket.setStatus(rs.getInt(index));
            }
            
            @Override
            public void dtoToParam(Ticket ticket, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setInt(paramIndex, ticket.getStatus());
            }
        });
    }
    
    
    private TicketDao(){}
    
    private static TicketDao instance;

    public static TicketDao getInstance() {
        if (instance == null){
            instance = new TicketDao();
        }
        return instance;
    }
    
    
    
}
