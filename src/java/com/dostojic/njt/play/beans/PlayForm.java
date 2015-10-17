/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.play.beans;

import com.dostojic.njt.db.Database;
import com.dostojic.njt.db.WrappedSQLException;
import com.dostojic.njt.db.dao.ArtistDao;
import com.dostojic.njt.db.dao.ArtistPlayDao;
import com.dostojic.njt.db.util.CommonUtils;
import com.dostojic.njt.model.Artist;
import com.dostojic.njt.model.ArtistPlay;
import com.dostojic.njt.model.ext.ArtistPlayX;
import com.dostojic.njt.performance.dao.PerformanceDao;
import com.dostojic.njt.play.dao.PlayDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.FormBean;
import com.dostojic.njt.util.JsfMessage;
import javax.faces.bean.ManagedBean;
import com.dostojic.njt.util.JsfUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
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
    private List<ArtistPlayX> artists;
    private List<Artist> allArtists;
       
    public static PlayForm getInstance(){
        return JsfUtils.findBean(MANAGED_BEAN_NAME);
    }

    public List<ArtistPlayX> getArtists() {
        return artists;
    }

    public List<Artist> getAllArtists() {
        return allArtists;
    }

    
    public void setArtists(List<ArtistPlayX> artists) {
        this.artists = artists;
    }
    
    @PostConstruct
    public void init(){
        
        long playId = JsfUtils.getLongPar("playId");
        allArtists = ArtistDao.getInstance().loadAll();
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
        for (ArtistPlay ap : artists){
            System.out.println("ARTIST play: " + ap.getArtistId() + " " + ap.getRole());
        }
        if (newData){
            try{
                Database.getCurrent().beginTransaction();
                PlayDao.getInstance().insert(data);
                for (ArtistPlay ap : artists){
                    ap.setPlayId(data.getId());
                    ArtistPlayDao.getInstance().insert(ap);
                }
                Database.getCurrent().commit();
                JsfMessage.info("Uspešno sačuvana predstava: " + data.getTitle());
                newData = false;
            }catch(WrappedSQLException ex){
                ex.printStackTrace();
                JsfMessage.error(ex.getMessage());
                Database.getCurrent().rollback();
            }
            
        }else{
             try{
            Database.getCurrent().beginTransaction();
            PlayDao.getInstance().update(data);
            ArtistPlayDao.getInstance().delete("play_id=" + data.getId());
            for (ArtistPlay ap : artists){
                ap.setPlayId(data.getId());
                ArtistPlayDao.getInstance().insert(ap);
            }
            Database.getCurrent().commit();
            JsfMessage.info("Uspešno promenjena predstava: " + data.getTitle());
             }catch(WrappedSQLException ex){ex.printStackTrace();
                JsfMessage.error(ex.getMessage());
                Database.getCurrent().rollback();
            }
        }
    }

    @Override
    public void load(long dataId) {
        data = PlayDao.getInstance().loadByPk(dataId);
        List<ArtistPlayX> artistPlayList = ArtistPlayDao.getInstance().loadList("play_id=" + data.getId(), null, ArtistPlayX.class);
        if (!artistPlayList.isEmpty()){
            StringBuilder where = new StringBuilder("id in (0");

            for (ArtistPlay ap : artistPlayList){
                where.append(",").append(ap.getArtistId());
            }
            
            where.append(")");

            List<Artist> artistsInPlay = ArtistDao.getInstance().loadList(where.toString(), "");
            
            for (ArtistPlayX ap : artistPlayList){
                for (Artist a : artistsInPlay){
                    if (ap.getArtistId() == a.getId()){
                        ap.setArtist(a);
                        break;
                    }
                }
            }
            

            
        }
        artists = artistPlayList;
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
        artists = new ArrayList<>();
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
        int count = PerformanceDao.getInstance().count("where play_id="+playId);
        if (count > 0){
            JsfMessage.error("Postoje izvodjenja za ovu predstavu. Možete obrisati predstave samo koje nemaju izvođenja.");
        }else{
            JsfMessage.error("Možete obrisati.");
//            PlayDao.getInstance().deleteByPk(playId);
        }
    }

    @Override
    public Play newObject() {
        return new Play();
    }
    
     public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
//        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
    }
     
     public void artistChanged(Artist a){
         
     }
     
     public void addNewArtist(){
         artists.add(new ArtistPlayX());
     }
     
     public void removeArtist(ArtistPlayX artistRole){
         artists.remove(artistRole);
     }
    
}
