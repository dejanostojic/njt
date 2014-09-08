/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author dejan
 */
@FunctionalInterface
public interface ResultSetProcessor {
    public void process(ResultSet resultSet) throws SQLException;
}
