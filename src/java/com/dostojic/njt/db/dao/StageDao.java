/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.dao;

import com.dostojic.njt.model.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dejan
 */
public class StageDao extends GenericDao<Stage>{

    @Override
    protected Stage newDto() {
        return new Stage();
    }

    @Override
    protected void setPrimaryKey(Stage dto, long id) {
        dto.setId(id);
    }
    
    

    @Override
    protected void config() {
        setTableName("stage");
        setAutoIncrement(true);
        
        add(new ColumnMapper("id") {

            @Override
            public boolean isPrimaryKey() {
                return true;
            }
            
            @Override
            public void resultSetToDto(ResultSet rs, Stage stage, int index) throws SQLException {
                stage.setId(rs.getLong(index));
            }
            
            @Override
            public void dtoToParam(Stage stage, PreparedStatement st, int paramIndex) throws SQLException {
                st.setLong(paramIndex, stage.getId());
            }
        });
        
        add(new ColumnMapper("name") {
            @Override
            public void resultSetToDto(ResultSet rs, Stage stage, int index) throws SQLException {
                stage.setName(rs.getString(index));
            }
            
            @Override
            public void dtoToParam(Stage stage, PreparedStatement statement, int paramIndex) throws SQLException {
                statement.setString(paramIndex, stage.getName());
            }
        });
        
    }
    
    private StageDao(){}
    
    private static StageDao instance;

    public static StageDao getInstance() {
        if (instance == null){
            instance = new StageDao();
        }
        return instance;
    }
    
    
    
}
