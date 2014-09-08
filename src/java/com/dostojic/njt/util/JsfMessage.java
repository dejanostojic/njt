/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author dostojic
 */
public class JsfMessage implements java.io.Serializable {
    
    public static void message(String summary) {
        message(summary, FacesMessage.SEVERITY_ERROR);
    }
    
    public static void message(UIComponent commponent, String summary) {
        message(commponent, summary, FacesMessage.SEVERITY_ERROR);
    }
    
    public static void info(String summary) {
        message(summary, FacesMessage.SEVERITY_INFO);
    }
    
    public static void info(UIComponent commponent, String summary) {
        message(commponent, summary, FacesMessage.SEVERITY_INFO);
    }
    
    public static void warn(String summary) {
        message(summary, FacesMessage.SEVERITY_WARN);
    }
    public static void warn(UIComponent component, String summary) {
        message(component, summary, FacesMessage.SEVERITY_WARN);
    }
    
    public static void error(String summary) {
        message(summary, FacesMessage.SEVERITY_ERROR);
    }
    public static void error(UIComponent component, String summary) {
        message(component, summary, FacesMessage.SEVERITY_ERROR);
    }
    
    public static void message(String summary, FacesMessage.Severity severity) {
        message(null, summary, severity);
    }
    
    public static void message(UIComponent commponent, String summary, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(severity);
        msg.setSummary(summary);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(commponent == null ? null : commponent.getClientId(context), msg);
    }
}
