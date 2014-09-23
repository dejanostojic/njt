/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.util.login;

import com.dostojic.njt.model.User;
import com.dostojic.njt.util.JsfUtils;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author dostojic
 */
@ManagedBean(name = LoginContext.MANAGED_BEAN_NAME)
@SessionScoped
public class LoginContext {
    
    enum UserRole{
        admin,
        loggedInUser,
        anonymous
    }
    
    public static final String MANAGED_BEAN_NAME = "loginContext";
    private User user;
    
    
    public static LoginContext getCurrent() {
        return JsfUtils.findBean(MANAGED_BEAN_NAME, LoginContext.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    
}
