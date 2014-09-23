/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.performance.beans;

import com.dostojic.njt.db.dao.StageDao;
import com.dostojic.njt.db.util.CommonUtils;
import com.dostojic.njt.model.Stage;
import com.dostojic.njt.performance.dao.PerformanceDao;
import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.play.dao.PlayDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.FormBean;
import com.dostojic.njt.util.JsfMessage;
import com.dostojic.njt.util.JsfUtils;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author dostojic
 */
@ManagedBean(name = PerformanceForm.MANAGED_BEAN_NAME)
@ViewScoped
public class PerformanceForm extends FormBean<Performance>{

    public static final String MANAGED_BEAN_NAME = "perfForm";
    
    public static PerformanceForm getInstance() {
        return JsfUtils.findBean(MANAGED_BEAN_NAME);
    }
    
    @Override
    public String getFormUrl() {
        return "/admin/perf/form";
    }

    @Override
    public String getViewUrl() {
        return "/admin/perf/form";
    }

    @Override
    public String getDeleteUrl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getListUrl() {
        return "/admin/perf/index";
    }
    
    @PostConstruct
    public void init(){
        long perfId = JsfUtils.getLongPar("perfId");
        
        
        System.out.println("DEBUG ::: INFO ::: INIT perf FORM! perfId: " + perfId);
        if (perfId > 0){
            load(perfId);
        }else{
            newData();
        }
        
    }
    
    @Override
    public void save() {
        System.out.println("PERF SAVE!!!");
        if (!validate(data)){
            System.out.println("PALI VALIDACIJU");
            return;
        }
        System.out.println("PROSLI VALIDACIJU");
        if (newData){
            PerformanceDao.getInstance().insert(data);
            JsfMessage.info("Uspešno sačuvano izvođenje");
            newData = false;
        }else{
            PerformanceDao.getInstance().update(data);
            JsfMessage.info("Uspešno izmenjeno izvođenje");
        }
    }
    
    
    private boolean validate(Performance data){
        boolean validated = true;
        if (data.getStageId() == 0){
            validated = false;
            JsfMessage.error("Scena je obavezno polje");
        }
        if (data.getPlayId() == 0){
            validated = false;
            JsfMessage.error("Obavezno izaberite predstavu");
        }
        
        if (CommonUtils.isEmpty(data.getStartTime())){
            validated = false;
            JsfMessage.error("Obavezno izaberite datum izvođenja");
        }
        
        if (CommonUtils.isEmpty(data.getStartDate())){
            validated = false;
            JsfMessage.error("Obavezno izaberite vreme izvođenja");
        }
        
        return validated;
    }
    @Override
    public void newData() {
        neww();
    }

    @Override
    public Performance newObject() {
        return new Performance();
    }

    public void cancel(ActionEvent e) {
        cancel();
    }
    
          
    
    public SelectItem[] getPlays(){
        List<Play> list = PlayDao.getInstance().loadAll();
        
        SelectItem[] items = new SelectItem[list.size() + 1];
        items[0] = new SelectItem(0l,"---");
        
        for (int i = 0; i<list.size(); i++){
            Play p = list.get(i);
            items[i + 1] = new SelectItem(p.getId(), p.getTitle());
        }
        
        return items;
    }
    
    public SelectItem[] getStages(){
        List<Stage> list = StageDao.getInstance().loadAll();
        
        SelectItem[] items = new SelectItem[list.size() + 1];
        items[0] = new SelectItem(0l,"---");
        
        for (int i = 0; i<list.size(); i++){
            Stage s = list.get(i);
            items[i + 1] = new SelectItem(s.getId(), s.getName());
        }
        return items;
    }
    
    public java.util.Date getStartDate(){
        return data.getStartDate();
    }
    
    public void setStartDate(java.util.Date date){
        data.setStartDate(new java.sql.Date(date.getTime()));
    }
    
    public java.util.Date getStartTime(){
        return data.getStartTime();
    }
    
    public void setStartTime(java.util.Date time){
        data.setStartTime(new java.sql.Time(time.getTime()));
    }

    @Override
    public void load(long dataId) {
        setData(PerformanceDao.getInstance().loadByPk(dataId));
        if (getData() != null){
            newData = false;
        }
    }

    public void load(Performance data) {
        setData(data);
        if (getData() != null){
            newData = false;
        }
    }
    
    public void delete(Performance performance){
        PerformanceDao.getInstance().delete(performance);
    }
    
    public void delete(long performanceId){
        PerformanceDao.getInstance().deleteByPk(performanceId);
    }
    
}
