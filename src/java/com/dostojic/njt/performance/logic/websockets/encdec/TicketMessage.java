/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.performance.logic.websockets.encdec;

/**
 *
 * @author dostojic
 */
public class TicketMessage {
    
    public enum Status{
        Inserted,
        AllreadyThere,
        New,
        Freed,
        Bought,
        OnOpen,
        OnClose;
    }
    
    private long perfId;
    private short status;
    private long seatId;
    private String addedStatus;
    
    public TicketMessage(){}
    
    public TicketMessage(long perfId, short status, long seatId, String addedStatus) {
        this.perfId = perfId;
        this.status = status;
        this.seatId = seatId;
        this.addedStatus = addedStatus;
    }
    
    
    public long getPerfId() {
        return perfId;
    }

    public void setPerfId(long perfId) {
        this.perfId = perfId;
    }

    public long getSeatId() {
        return seatId;
    }

    public void setSeatId(long seatId) {
        this.seatId = seatId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getAddedStatus() {
        return addedStatus;
    }

    public void setAddedStatus(String addedStatus) {
        this.addedStatus = addedStatus;
    }

    public TicketMessage copy(TicketMessage t){
        return new TicketMessage(t.getPerfId(), t.getStatus(), t.getSeatId(), t.getAddedStatus());
    }
    
    
}
