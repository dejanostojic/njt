package com.dostojic.njt.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dostojic
 */
public abstract class FormBean <T> implements java.io.Serializable {
    protected T data;
    private String backUrl;
    protected boolean newData;
    
    public abstract String getFormUrl();
    public abstract String getViewUrl();
    public abstract String getDeleteUrl();
    public abstract String getListUrl();
    
    protected boolean goBack() {
        if (backUrl != null && !backUrl.isEmpty()) {
            JsfUtils.navigateRedirect(backUrl, getBackUrlParams());
            return true;
        }
        return false;
    }
    
    protected String getBackUrlParams() {
        
        assert backUrl!=null : "backUrl je null!";
        
        String ret = "";
        int index = backUrl.indexOf("?");
        if (index != -1) {
            ret = backUrl.substring(index+1);
        }
        return ret;
    }
    
    public void gotoForm() {
        System.out.println("navigate redirect: " + getFormUrl());
        JsfUtils.navigateRedirect(getFormUrl());
    }
    
    public void gotoView() {
        JsfUtils.navigateRedirect(getViewUrl());
    }
    
    public void gotoDelete() {
        JsfUtils.navigateRedirect(getDeleteUrl());
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public abstract void save();
    public abstract void newData();
    public abstract T newObject();
    
    public void cancel(){
        goBack();
    }
    
    public void load(long dataId) {
        throw new UnsupportedOperationException();
    }
    
    public boolean isEdit() {
        return !newData;
    }
    
    public void neww() {
            data = newObject();
            newData = true;
            setBackUrl(getListUrl());
    }
}