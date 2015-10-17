/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.stage;

import com.dostojic.njt.model.Seat;
import com.dostojic.njt.model.Stage;
import com.dostojic.njt.db.dao.SeatDao;
import com.dostojic.njt.db.dao.StageDao;
import com.dostojic.njt.db.util.QueryUtils;
import com.dostojic.njt.so.GenericSO;

/**
 *
 * @author dejan
 */
public class SaveStageWithSeatsOperation extends GenericSO{


    @Override
    protected void executeOperation(Object... args) throws Exception {
        Stage stage = (Stage) args[0];
        Seat[][] seatMatrix = (Seat[][]) args[1];
        StageDao.getInstance().insert(stage);
        long stageId = stage.getId();
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        System.out.println("STAGE ID::: " + stageId);
        for(Seat[] seatArray : seatMatrix){
            for (Seat seat : seatArray){
                seat.setStageId(stageId);
                SeatDao.getInstance().insert(seat);
            }       
        }
        
    }

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
        Stage stage = (Stage) args[0];
        int s = StageDao.getInstance().count("name="+ QueryUtils.stringLiteral(stage.getName()));
        if (s > 0){
            throw new Exception("Već postoji scena sa tim nazivom");
        }
    }
   
}
