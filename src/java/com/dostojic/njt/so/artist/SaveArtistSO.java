/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.artist;

import com.dostojic.njt.db.dao.ArtistDao;
import com.dostojic.njt.model.Artist;
import com.dostojic.njt.so.GenericSO;

/**
 *
 * @author dejan
 */
public class SaveArtistSO extends GenericSO{

    @Override
    protected void checkPrecondition(Object... o) throws Exception {
        
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        ArtistDao.getInstance().insert((Artist) args[0]);
    }
    
}
