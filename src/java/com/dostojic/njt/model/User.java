/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.model;

import com.dostojic.njt.annotation.Column;
import com.dostojic.njt.annotation.PrimaryKey;
import com.dostojic.njt.annotation.Table;
import java.io.Serializable;

/**
 *
 * @author dejan
 */
@Table(name = "user", autoIncrement = true)
public class User implements Serializable{

    @PrimaryKey
    @Column(name = "id", isPrimaryKey = true)
    private long id;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    /*
    private String kindId;
    private String typeId;
    private KindUser kind;
    
    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    
    public KindUser getKind() {
        return kind;
    }

    public void setKind(KindUser kind) {
        this.kind = kind;
    }
    
    */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}