/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.play;

import com.dostojic.njt.model.Artist;
import com.dostojic.njt.model.ArtistPlay;
import com.dostojic.njt.db.dao.PlayDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.db.ResultSetProcessor;
import com.dostojic.njt.db.dao.ArtistDao;
import com.dostojic.njt.db.dao.ArtistPlayDao;
import com.dostojic.njt.db.util.QueryUtils;
import com.dostojic.njt.so.GenericSO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dejan
 */
public class GetPlaySO extends GenericSO{

    List<Play> plays;
    

    @Override
    protected void executeOperation(Object... args) throws Exception {
        plays = PlayDao.getInstance().loadAll();
        for (Play play : plays){
            ArtistDao.getInstance().loadList("", null);
            play.setArtistRoles(getArtists(play.getId()));
        }
    }
    
    @Override
    protected void checkPrecondition(Object... args) throws Exception {}
    
    
    public List<Play> getPlays() {
        return plays;
    }
    
    private List<Map<ArtistPlay,Artist>> getArtists(long id){
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(ArtistPlayDao.getInstance().getCommaSepColumnList()).append(" from ").append(ArtistPlayDao.getInstance().getTableName());
        
        final List<Map<ArtistPlay,Artist>> retList = new ArrayList<>();
        System.out.println("SQL>>> " + sql.toString());
        QueryUtils.forEach(sql.toString(), (resultSet) -> {
            
                HashMap<ArtistPlay,Artist> map = new HashMap<>();
                ArtistPlay ap = new ArtistPlay();
                ArtistPlayDao.getInstance().loadFromResultSet(resultSet, ap);
                Artist a = ArtistDao.getInstance().loadByPk(ap.getArtistId());
                map.put(ap, a);
                retList.add(map);
        });
        return retList;
    }


    
}
