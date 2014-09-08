/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.db.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author dejan
 */
public class DBProperties {
    Properties properties;
    
    private static DBProperties instance;
    private DBProperties() throws IOException{
        properties=new Properties();
        properties.load(new FileInputStream("db.properties"));
    }
    public static DBProperties getInstance() throws IOException{
        if(instance==null){
            instance=new DBProperties();
        }
        return instance;
    }
    
    public String getURL(){
        return properties.getProperty(properties.getProperty("current_db")+"_"+"url");
    }
    
    public String getUser(){
        return properties.getProperty(properties.getProperty("current_db")+"_"+"user");
    }
    
    public String getPassword(){
        return properties.getProperty(properties.getProperty("current_db")+"_"+"password");
    }
    
    public String getDriver(){
        return properties.getProperty(properties.getProperty("current_db")+"_"+"driver");
    }
}
