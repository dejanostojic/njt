/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.model.ext;

import com.dostojic.njt.db.dao.SeatDao;
import com.dostojic.njt.db.dao.StageDao;
import com.dostojic.njt.model.Seat;
import com.dostojic.njt.model.Stage;
import com.dostojic.njt.util.BeanUtils;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

/**
 *
 * @author dostojic
 */
public class StageX extends Stage{
    
    
    List<Seat> seats;
    
    public StageX(Stage s){
        BeanUtils.copyProperties(this, s);
    }
    
    public StageX(long id){
        Stage stage = StageDao.getInstance().loadByPk(id);
        if (stage != null){
            BeanUtils.copyProperties(this, stage);
        }
    }
    
    public List<Seat> getSeats() {
        if(seats == null){
            seats = SeatDao.getInstance().loadByStageId(getId());
        }
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
    
    public int getNumberOfColumns(){
        return getMax((Seat s) ->  s.getColumn());
    }
    
    public int getNumberOfRows(){
        return getMax((Seat s) -> s.getRow());
    }
    
    private int getMax(ToIntFunction<Seat> f){
        return getSeats().stream().mapToInt(s -> f.applyAsInt(s)).max().getAsInt();
    }
    
    
    
    
}
