/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.artist;

import com.dostojic.njt.db.dao.ArtistDao;
import com.dostojic.njt.model.Artist;
import com.dostojic.njt.so.GenericSO;
import java.util.List;

/**
 *
 * @author dejan
 */
public class GetArtistsSO extends GenericSO{
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }
    
    @Override
    protected void executeOperation(Object... args) throws Exception {
        artists = ArtistDao.getInstance().loadAll();
    }

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
    }

}
