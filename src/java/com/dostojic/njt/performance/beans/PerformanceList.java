/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.performance.beans;

import com.dostojic.njt.db.dao.StageDao;
import com.dostojic.njt.db.dao.TicketDao;
import com.dostojic.njt.db.util.QueryUtils;
import com.dostojic.njt.model.Stage;
import com.dostojic.njt.performance.dao.PerformanceDao;
import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.performance.model.ex.PerformanceX;
import com.dostojic.njt.play.dao.PlayDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.JsfMessage;
import com.dostojic.njt.util.JsfUtils;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
/**
 *
 * @author dostojic
 */
@ManagedBean(name = PerformanceList.MANAGED_BEAN_NAME)
@ViewScoped
public class PerformanceList implements java.io.Serializable{
    
    public static final String MANAGED_BEAN_NAME = "perfList";
    
    // filters
    private long stageId;
    private int minPrice = 0;
    private int maxPrice = -1;
    private long playId;

    public long getStageId() {
        return stageId;
    }

    public void setStageId(long stageId) {
        this.stageId = stageId;
    }

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }
    
    
    @PostConstruct
    public void init(){
        Performance p = PerformanceDao.getInstance().loadFirst("", "price desc");
        Performance pMin = PerformanceDao.getInstance().loadFirst("", "price asc");
        if (p != null){
            maxPrice = (int) p.getPrice();
        }
        if (pMin != null){
            minPrice = (int) pMin.getPrice();
        }
    }
    
    public List<PerformanceX> getList(){
        return PerformanceDao.getInstance().loadList(sqlStringCondition(), "id desc", PerformanceX.class);
    }
    
    private String sqlStringCondition(){
        StringBuilder where = new StringBuilder("true");
        
        if (playId > 0){
            where.append(" and play_id=").append(playId);
        }
        if (stageId > 0){
            where.append(" and stage_id=").append(stageId);
        }
        if (minPrice > 0){
            where.append(" and price>=").append(minPrice);
        }
        if (maxPrice > 0){
            where.append(" and price<=").append(maxPrice);
        }
        
        return where.toString();
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
        int count = TicketDao.getInstance().count("performance_id="+p.getId());
        if (count > 0){
            JsfMessage.error("Postoje karte za izvođenje ove predstve. Možete obrisati samo izvođenja bez prodatih karti.");
        }else{
            PerformanceDao.getInstance().delete(p);
        }
        
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
    
}
