/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.model;

import java.io.Serializable;
import com.dostojic.njt.annotation.*;
/**
 *
 * @author dejan
 */
@Table(name = "artist", autoIncrement = true)
public class Artist implements Serializable{
    @PrimaryKey
    @Column(name = "id", isPrimaryKey = true)
    private long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "kind")
    private String kind;
    
    @Column(name = "body")
    private String body;

    
    public final static String KIND_DIRECTOR = "director";
    public final static String KIND_ACTOR = "actor";
    public final static String KIND_SET_DESIGNER= "set designer";
    public final static String KIND_COSTUME_DESIGNER= "costume designer";
    
    public Artist(){
        
    }
    
    public Artist(long id){
        this.id = id;
    }
    
    public Artist(String firstName, String lastName, String kind, String body) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.kind = kind;
        this.body = body;
    }

    public Artist(long id, String firstName, String lastName, String kind, String body) {
        this(firstName, lastName, kind, body);
        this.id = id;
    }
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + ", " + kind;
    }
    
    
}
