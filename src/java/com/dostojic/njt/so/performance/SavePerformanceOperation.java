/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.performance;

import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.model.Seat;
import com.dostojic.njt.model.Ticket;
import com.dostojic.njt.db.dao.PerformanceDao;
import com.dostojic.njt.db.dao.SeatDao;
import com.dostojic.njt.db.dao.TicketDao;
import com.dostojic.njt.so.GenericSO;
import java.util.List;

/**
 *
 * @author dejan
 */
public class SavePerformanceOperation extends GenericSO{

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        Performance perf = (Performance) args[0];
        PerformanceDao.getInstance().insert(perf);
        List<Seat> seats = SeatDao.getInstance().loadByStageId(perf.getStageId());
        for (Seat s : seats){
            Ticket t = new Ticket();
            t.setSeatId(s.getId());
            t.setPerformanceId(perf.getId());
            TicketDao.getInstance().insert(t);
        }
    }
    
}
