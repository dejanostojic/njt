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
public class UpdateArtistSO extends GenericSO{

    @Override
    protected void executeOperation(Object... args) throws Exception {
        ArtistDao.getInstance().update((Artist) args[0]);
    }

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
    }
    
}
