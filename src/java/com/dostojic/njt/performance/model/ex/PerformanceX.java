/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.performance.model.ex;

import com.dostojic.njt.db.dao.StageDao;
import com.dostojic.njt.model.Stage;
import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.play.dao.PlayDao;
import com.dostojic.njt.play.model.Play;

/**
 *
 * @author dostojic
 */
public class PerformanceX extends Performance{
    private Play play;
    private Stage stage;

    public Play getPlay() {
        if (play == null && getPlayId() > 0){
            play = PlayDao.getInstance().loadByPk(getPlayId());
        }
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public Stage getStage() {
        if (stage == null && getStageId() > 0){
            stage = StageDao.getInstance().loadByPk(getStageId());
        }
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
}
