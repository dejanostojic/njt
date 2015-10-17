/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.play;

import com.dostojic.njt.db.dao.ArtistPlayDao;
import com.dostojic.njt.db.dao.PlayDao;
import com.dostojic.njt.model.ArtistPlay;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.model.ext.ArtistPlayX;
import com.dostojic.njt.so.GenericSO;
import java.util.List;

/**
 *
 * @author dejan
 */
public class SavePlayAndArtistsSO extends GenericSO{


    @Override
    protected void checkPrecondition(Object... args) throws Exception {
        
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        Play play = (Play) args[0];
        PlayDao.getInstance().updateOrInsert(play);
        long playId = play.getId();
        List<ArtistPlayX> artistPlayList = (List<ArtistPlayX>) args[1];
        for (ArtistPlay ap: artistPlayList){
            ap.setPlayId(playId);
            ArtistPlayDao.getInstance().updateOrInsert(ap);
        }
    }
    
}
