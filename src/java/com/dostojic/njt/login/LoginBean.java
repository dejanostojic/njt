/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.login;

import com.dostojic.njt.db.dao.UserDao;
import com.dostojic.njt.model.User;
import com.dostojic.njt.util.JsfMessage;
import com.dostojic.njt.util.JsfUtils;
import com.dostojic.njt.app.login.LoginContext;
import com.dostojic.njt.db.util.CommonUtils;
import java.util.List;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

/**
 *
 * @author dostojic
 */
@ManagedBean
@ViewScoped
public class LoginBean implements java.io.Serializable{
    private String username;
    private String password;
    private String back;
    
    public LoginBean() throws ServletException {
        String backPar = JsfUtils.getParameter("back");
        String logout = JsfUtils.getParameter("logout");
        back = backPar != null ? backPar : "";

        if ("true".equals(logout)) {
            JsfUtils.getHttpServletReqest().logout();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
            String baseUrl = viewHandler.getActionURL(facesContext, facesContext.getViewRoot().getViewId());
            JsfUtils.navigateRedirect(baseUrl, "back=" + back);
        }
    }
    
    public void doLogin() {
        
        if (CommonUtils.isEmpty(username) || CommonUtils.isEmpty(password)) {
            System.out.println("not logged in");
            JsfMessage.error("Obavezni podaci");
            return;
        }else{
            User user = UserDao.getInstance().loadByUsername(username);

            if (user == null){
                System.out.println("not logged in");
                JsfMessage.error("Pogrešno korisničko ime ili lozinka");
            }else if (password.equals(user.getPassword())){
                System.out.println("username.equals(user.getPassword(): " + password.equals(user.getPassword()));

                System.out.println("logged in");
                LoginContext.getInstance().logIn(user);
                System.out.println("redirect!");
                JsfUtils.redirectToUrl(CommonUtils.isEmpty(back) ? "/" : back);

            }
        
        }
        
    }

    public String getPassword() {
        System.out.println("get pass" + password);
        return password;
    }

    public void setPassword(String password) {
        System.out.println("set pass: " + password);
        this.password = password;
    }

    public String getUsername() {
        System.out.println("get username: " + username);
        return username;
    }

    public void setUsername(String username) {
        System.out.println("set username: " + username);
        this.username = username;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
    
    public List<User> getUserList(){
        return UserDao.getInstance().loadAll();
    }
    
}
