/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.play.model;

import com.dostojic.njt.model.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 *
 * @author dejan
 */
public class Play implements Serializable{

    private long id;
    private String title;
    private String about;
    private List<Artist> artstList;
    private List<Map<ArtistPlay,Artist>> artistRoles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Artist> getArtstList() {
        return artstList;
    }

    public void setArtstList(List<Artist> artstList) {
        this.artstList = artstList;
    }

    public List<Map<ArtistPlay, Artist>> getArtistRoles() {
        return artistRoles;
    }

    public void setArtistRoles(List<Map<ArtistPlay, Artist>> artistRoles) {
        this.artistRoles = artistRoles;
    }
    
    public List<Artist> getActorsList(){
        
     /*   StringBuilder sql = new StringBuilder();
        sql.append("select artist_id,role from artist_play where play_id=").append(id);
        sql.append(" and kind=").append(QueryUtils.stringLiteral(ArtistPlay.KIND_ACTOR));
        QueryUtils.forEach(sql.toString(), new ResultSetClosure() {

            @Override
            public void process(ResultSet resultSet) throws SQLException {
                
            }
        });
//        ArtistPlayDao.getInstance().loadList(where, title);
        */
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
    
    
    
}
