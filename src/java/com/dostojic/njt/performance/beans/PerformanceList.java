/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.performance.beans;

import com.dostojic.njt.performance.dao.PerformanceDao;
import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.performance.model.ex.PerformanceX;
import com.dostojic.njt.util.JsfUtils;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
/**
 *
 * @author dostojic
 */
@ManagedBean(name = PerformanceList.MANAGED_BEAN_NAME)
@RequestScoped
public class PerformanceList implements java.io.Serializable{
    
    public static final String MANAGED_BEAN_NAME = "perfList";
    
    public PerformanceList getInstance(){
        return JsfUtils.findBean(MANAGED_BEAN_NAME);
    }
    
    public List<PerformanceX> getList(){
        return PerformanceDao.getInstance().laodAll(PerformanceX.class);
    }
    
    public void newData(){
        PerformanceForm form = PerformanceForm.getInstance();
        form.neww();
        form.gotoForm();
    }
    
    public void editData(Performance p){
        PerformanceForm form = PerformanceForm.getInstance();
        form.load(p);
        form.setBackUrl(form.getListUrl());
        form.gotoForm();
    }
    
    public void deleteData(Performance p){
        PerformanceDao.getInstance().delete(p);
    }
    
}
