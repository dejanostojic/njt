/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.app.filter;

import com.dostojic.njt.app.login.LoginContext;
import com.dostojic.njt.util.JsfUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dostojic
 */
public class AuthorizationFilter implements Filter, java.io.Serializable{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String cPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        System.out.println("AUTHORIZATION FILTER!! cpath: " + cPath);
        System.out.println("AUTHORIZATION FILTER!! starts with: " + cPath.startsWith("/admin/loginForm"));
        LoginContext lContext =(LoginContext) httpRequest.getSession().getAttribute(LoginContext.MANAGED_BEAN_NAME);
        
        if ((lContext == null || !lContext.isLoggedIn()) && (cPath.startsWith("/admin/"))
                && !(cPath.startsWith("/admin/loginForm"))) {
            if ("GET".equals(httpRequest.getMethod())) {
                if (cPath.length() < 8) {
                    ((HttpServletResponse) response).sendRedirect("/admin/loginForm.xhtml");
                } else {
                    String uri = httpRequest.getRequestURI(); 
                    String qs = httpRequest.getQueryString();
                    try {
                        String url = httpRequest.getRequestURI()+(qs == null ? "" : "?"+qs);
                        uri = URLEncoder.encode(url,"UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex);
                    }
                    
                    ((HttpServletResponse) response).sendRedirect("/admin/loginForm.xhtml?back=" + uri);
                }
            } else {
                httpRequest.getRequestDispatcher("/admin/loginForm.xhtml").forward(request, response);
            }
        } else {
            System.out.println("chain!!!");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        
    }
    
}
