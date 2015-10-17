/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.util;

import java.io.IOException;
import java.util.Map;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContextWrapper;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        T bean = null;
        try{
            bean = (T) context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", targetClass);
        }catch(ELException e){
            System.out.println("DEBUG ::: INFO ::: BEAN JE NULL: " + e.getMessage());
        }catch(NullPointerException npe){
            System.out.println("NullPointerException: " + npe.getMessage());
        }catch(Exception e){
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        return bean;
    }
    
    public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response){
        // Get current FacesContext.
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Check current FacesContext.
        if (facesContext == null) {

            // Create new Lifecycle.
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

            // Create new FacesContext.
            FacesContextFactory contextFactory  = (FacesContextFactory)
                FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            facesContext = contextFactory.getFacesContext(
                request.getSession().getServletContext(), request, response, lifecycle);

            // Create new View.
            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(
                facesContext, "");
            facesContext.setViewRoot(view);                

            // Set current FacesContext.
            FacesContextWrapper.setCurrentInstance(facesContext);
        }

        return facesContext;
    }

    // Wrap the protected FacesContext.setCurrentInstance() in a inner class.
    private static abstract class FacesContextWrapper extends FacesContext {
        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
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
    
    public static void setSessionObject(Object bean, String name){
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        System.out.println("currentInstance: " + currentInstance);
        ExternalContext externalContext = currentInstance.getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put(name, bean);
//        FacesContext.getCurrentInstance().getExternalContext().gets
    }
}
