/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.performance.model.Performance;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dejan
 */
public class PerformanceDao extends GenericDao<Performance>{

    @Override
    protected Performance newDto() {
        return new Performance();
    }

    @Override
    protected void setPrimaryKey(Performance dto, long id) {
        dto.setId(id);
    }
    
    

    @Override
    protected void config() {
        setTableName("performance");
        setAutoIncrement(true);
        
        add(new ColumnMapper("id") {
            
            @Override
            public boolean isPrimaryKey() {
               return true; //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public void resultSetToDto(ResultSet rs, Performance dto, int index) throws SQLException {
                dto.setId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Performance dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getId());
            }
        });
        
        add(new ColumnMapper("play_id") {
            
            @Override
            public void resultSetToDto(ResultSet rs, Performance dto, int index) throws SQLException {
                dto.setPlayId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Performance dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getPlayId());
            }
        });
        
        add(new ColumnMapper("start_time") {
            @Override
            public void resultSetToDto(ResultSet rs, Performance performance, int index) throws SQLException {
                performance.setStartTime(rs.getTime(index));
            }
            
            @Override
            public void dtoToParam(Performance dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setTime(paramIndex, dto.getStartTime());
            }
        });
        
        add(new ColumnMapper("start_date") {
            @Override
            public void resultSetToDto(ResultSet rs, Performance dto, int index) throws SQLException {
                dto.setStartDate(rs.getDate(index));
            }
            
            @Override
            public void dtoToParam(Performance dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setDate(paramIndex, dto.getStartDate());
            }
        });     
        
        add(new ColumnMapper("stage_id") {
            
            @Override
            public void resultSetToDto(ResultSet rs, Performance dto, int index) throws SQLException {
                dto.setStageId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Performance dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setLong(paramIndex, dto.getStageId());
            }
        });
        
        add(new ColumnMapper("price") {
            
            @Override
            public void resultSetToDto(ResultSet rs, Performance dto, int index) throws SQLException {
                dto.setPrice(rs.getDouble(index));
            }
            
            @Override
            public void dtoToParam(Performance dto, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setDouble(paramIndex, dto.getPrice());
            }
        });
    }

    private PerformanceDao() {}
    
    private static PerformanceDao instance;

    public static PerformanceDao getInstance() {
        if (instance == null){
            instance = new PerformanceDao();
        }
        return instance;
    }
    
    
    
}
