/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.util;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dostojic
 */
public class JsfUtils {
    
    
    public static Object getValue(String expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, expression, Object.class);
    }
    
    public static <T extends Object> T getValue(String expression, Class<? extends T> targetClass){
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, expression, targetClass);
    }
    
    public static <T> T findBean(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
    }
    
    public static <T> T findBean(String beanName, Class<T> targetClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        return (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", targetClass);
    }
    
    public static HttpServletRequest getHttpServletReqest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static String getParameter(String name) {
        return getHttpServletReqest().getParameter(name);
    }
    
    public static long getLongPar(String name) {
        return getLongPar(name,0);
    }
    
    public static long getLongPar(String name, long dflt) {
        long value = dflt;
        try {
            value = Long.parseLong(getParameter(name));
        } catch(NumberFormatException e) {}
        return value;
    }
    
    public static void navigateRedirect(String viewId) {
        navigateRedirect(viewId, null);
    }
    
    public static void redirectToUrl(String url) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            externalContext.redirect(url);
        } catch (IOException e) {
            throw new FacesException(e);
        }
        facesContext.responseComplete();
    }
    
    public static void navigateRedirect(String viewId, String queryString) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String redirectPath = viewHandler.getActionURL(facesContext,viewId);
        if (queryString != null) {
            redirectPath = redirectPath + '?' + queryString;
        }
        try {
            externalContext.redirect(externalContext.encodeActionURL(redirectPath));
        } catch (IOException e) {
            throw new FacesException(e);
        }
        facesContext.responseComplete();
    }
    
    
    public static void navigateForward(String viewId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(facesContext, viewId);
        facesContext.setViewRoot(viewRoot);
        facesContext.renderResponse();
    }    
}
