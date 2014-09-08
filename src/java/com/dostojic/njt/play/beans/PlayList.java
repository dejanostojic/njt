/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.play.beans;

import com.dostojic.njt.db.dao.PlayDao;
import com.dostojic.njt.db.util.PaginationHelper;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.BaseListFilter;
import com.dostojic.njt.util.JsfUtils;
import javax.faces.event.ActionEvent;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author dostojic
 */
@ManagedBean(name=PlayList.MANAGED_BEAN_NAME)
@RequestScoped
public class PlayList implements java.io.Serializable{
    public static final String MANAGED_BEAN_NAME = "playList";
    
    public PlayList getInstance(){
        return JsfUtils.findBean(MANAGED_BEAN_NAME);
    }
    
    private PaginationHelper pagination;
    private Filter filter;
    
    
    public List<Play> getList() {
        return PlayDao.getInstance().loadAll();
    }
    
    private class Filter extends BaseListFilter{
        
        public Filter(){
            String[] columns = {"title","about"};
            setOrderByColumns(columns);
            defaultOrder = "title";
        }
    }
    
    public void newPlay(ActionEvent event){
        PlayForm playForm = PlayForm.getInstance();
        playForm.setBackUrl("/admin/play");
        playForm.newData();
        playForm.gotoForm();
    }
    
    public void editPlay(Play p){
        System.out.println("EDIT PLAY!!! Title: " + p.getTitle());
        PlayForm playForm = PlayForm.getInstance();
        System.out.println("LOAD");
        playForm.load(p);
        System.out.println("load gotov");
        playForm.gotoForm();
        System.out.println("go to form");
    }
    
    public void deletePlay(Play p){
        PlayForm.getInstance().deletePlay(p.getId());
    }
    
    
    
}
