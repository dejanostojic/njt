/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.performance;

import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.model.Stage;
import com.dostojic.njt.db.dao.PerformanceDao;
import com.dostojic.njt.so.GenericSO;
import java.util.List;

/**
 *
 * @author dostojic
 */
public class GetPerformancesForPlayOnStage extends GenericSO{
    
    private List<Performance> performances;

    public List<Performance> getPerformances() {
        return performances;
    }
    
    @Override
    protected void checkPrecondition(Object... args) throws Exception {
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        Play p = (Play) args[0];
        Stage s = (Stage) args[1];
        
            String where = "play_id=" + p.getId() + " and stage_id=" + s.getId();
        
        performances = PerformanceDao.getInstance().loadList(where, "start_date desc, start_time desc");
    }
}
