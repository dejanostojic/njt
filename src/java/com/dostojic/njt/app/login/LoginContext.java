/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.app.login;

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
public class LoginContext implements java.io.Serializable{
    
    enum UserRole{
        admin,
        cashier,
        anonymous
    }
    
    public static final String MANAGED_BEAN_NAME = "loginContext";
//    private static ThreadLocal<LoginContext> lcTLocal = new ThreadLocal<>();
    private User user;
    private UserRole role;
    /*
    public static LoginContext getInstance(){
        LoginContext instance = lcTLocal.get();
        if(instance == null) {
            System.out.println("DEBUG ::: INFO ::: setting anon");
            instance = anon();
            lcTLocal.set(instance);
        }
        return instance;
    }
    */
    
    public static LoginContext getInstance(){
        LoginContext lc = JsfUtils.findBean(MANAGED_BEAN_NAME,LoginContext.class);
        if (lc == null){
            lc = anon();
            JsfUtils.setSessionObject(lc, MANAGED_BEAN_NAME);
        }
        return lc;
    }
    
    public LoginContext(){
        setRole(UserRole.anonymous);
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void setRole(UserRole role) {
        this.role = role;
    }
    
    public void logIn(User u){
        
        if (u != null){
            System.out.println("logging in: " + u.getUserName());
            user =  u;
            role = UserRole.valueOf(u.getKind());
        }else{
        System.out.println("USER IS NULL");
        }
    }
    
    public void logOut(){
        JsfUtils.getHttpServletReqest().setAttribute(MANAGED_BEAN_NAME, anon());
    }
    
    private static LoginContext anon(){
        LoginContext lc = new LoginContext();
        lc.setRole(UserRole.anonymous);
        return lc;
    }
    
    public boolean isAdmin(){
        return role == UserRole.admin;
    }
    
    
    public boolean isCashier(){
        return role == UserRole.cashier;
    }
    
    public boolean isLoggedIn(){
        System.out.println("debug ::: info ::: role name: " + role.name());
        System.out.println("DEBUG ::: INFO ::: is logged in: " +  (role == UserRole.admin || role == UserRole.cashier));
        return isAdmin() || isCashier();
    }
    
    public String getUserName(){
        if (user == null){
            return UserRole.anonymous.name();
        }
        
        return user.getUserName();
    }
    
    
}
