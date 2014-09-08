/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.model.ext;

import com.dostojic.njt.model.Seat;
import com.dostojic.njt.model.Ticket;

/**
 *
 * @author dejan
 */
public class TicketX extends Ticket{
    
    private Seat seat;

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
}
