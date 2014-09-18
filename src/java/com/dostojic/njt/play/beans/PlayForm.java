/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.play.beans;

import com.dostojic.njt.db.util.CommonUtils;
import com.dostojic.njt.play.dao.PlayDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.FormBean;
import com.dostojic.njt.util.JsfMessage;
import javax.faces.bean.ManagedBean;
import com.dostojic.njt.util.JsfUtils;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author dostojic
 */
@ManagedBean(name = PlayForm.MANAGED_BEAN_NAME)
@ViewScoped
public class PlayForm extends FormBean<Play>{

    public static final String MANAGED_BEAN_NAME = "playForm";
    private UIComponent titleComp;
    private UIComponent aboutComp;
    
    public static PlayForm getInstance(){
        return JsfUtils.findBean(MANAGED_BEAN_NAME);
    }
    
    @PostConstruct
    public void init(){
        
        long playId = JsfUtils.getLongPar("playId");
        
        System.out.println("DEBUG ::: INFO ::: INIT PLAY FORM! playId: " + playId);
        if (playId > 0){
            load(playId);
        }else{
            newData();
        }
    }
    
    @Override
    public String getFormUrl() {
        return "/admin/play/form";
    }

    @Override
    public String getViewUrl() {
        return "/admin/play/index";
    }

    @Override
    public String getDeleteUrl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getListUrl() {
        return "/admin/play/index";
    }
    
    

    @Override
    public void save() {
        boolean required = false;
        if (CommonUtils.isEmpty(data.getTitle())){
            JsfMessage.error(titleComp, "Naslov je obavezno polje");
            required = true;
        }
        
        if (CommonUtils.isEmpty(data.getAbout())){
            JsfMessage.error(aboutComp, "O predstavi je obavezno polje");
            required = true;
        }
        
        if (required){
            return;
        }
        
        if (newData){
            PlayDao.getInstance().insert(data);
            JsfMessage.info("Uspešno sačuvana predstava: " + data.getTitle());
        }else{
            PlayDao.getInstance().update(data);
            JsfMessage.info("Uspešno promenjena predstava: " + data.getTitle());
        }
    }

    @Override
    public void load(long dataId) {
        data = PlayDao.getInstance().loadByPk(dataId);
        newData = false;
    }
    
    public void load(Play play) {
        data = play;
        newData = false;
    }

    @Override
    public void newData() {
        data = new Play();
        newData = true;
    }

    public UIComponent getTitleComp() {
        return titleComp;
    }

    public void setTitleComp(UIComponent titleComp) {
        this.titleComp = titleComp;
    }

    public UIComponent getAboutComp() {
        return aboutComp;
    }

    public void setAboutComp(UIComponent aboutComp) {
        this.aboutComp = aboutComp;
    }

    public void deletePlay(long playId){
        PlayDao.getInstance().deleteByPk(playId);
    }

    @Override
    public Play newObject() {
        return new Play();
    }
    
}
