/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.model.ext;

import com.dostojic.njt.db.dao.SeatDao;
import com.dostojic.njt.model.Seat;
import com.dostojic.njt.model.Ticket;
import com.dostojic.njt.model.TicketStatus;

/**
 *
 * @author dejan
 */
public class TicketX extends Ticket{
    
    private Seat seat;

    public Seat getSeat() {
        if (seat == null && getSeatId() > 0){
            seat = SeatDao.getInstance().loadByPk(getSeatId());
        }
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
    public boolean isFree(){
        return getStatus() == TicketStatus.STATUS_FREE;
    }
    
    public boolean isReserved(){
        return getStatus() == TicketStatus.STATUS_RESERVED;
    }
    
    public boolean isSold(){
        return getStatus() == TicketStatus.STATUS_SOLD;
    }
    
    public boolean isInStore(){
        return getStatus() == TicketStatus.STATUS_IN_STORE;
    }
    
    public String getStatusText(){
        return TicketStatus.getText.get(getStatus());
    }
    
}
