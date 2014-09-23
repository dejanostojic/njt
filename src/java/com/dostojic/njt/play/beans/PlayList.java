/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.play.beans;

import com.dostojic.njt.db.dao.PlayDao;
import com.dostojic.njt.performance.dao.PerformanceDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.JsfMessage;
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
    
    public List<Play> getList() {
        return PlayDao.getInstance().loadAll();
    }
    
    public void newPlay(ActionEvent event){
        PlayForm.getInstance().neww();
    }
    
    public void editPlay(Play p){
        PlayForm playForm = PlayForm.getInstance();
        playForm.load(p);
        playForm.gotoForm();
    }
    
    public void deletePlay(Play p){
        int count = PerformanceDao.getInstance().count("play_id="+p.getId());
        if (count > 0){
            JsfMessage.error("Postoje izvodjenja za ovu predstavu (" + p.getTitle() + "). Možete obrisati predstave samo koje nemaju izvođenja.");
        }else{
            PlayDao.getInstance().deleteByPk(p.getId());
        }
    }
    
    
    
}
