/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.login;

import com.dostojic.njt.app.login.LoginContext;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dostojic
 */
public class LogoutServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        LoginContext.getInstance().logOut();
        
        req.getSession().setAttribute(LoginContext.MANAGED_BEAN_NAME, LoginContext.anon());

        resp.sendRedirect("/admin/loginForm.xhtml");
    }
    
}
