/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.performance.beans;

import com.dostojic.njt.model.TicketStatus;
import com.dostojic.njt.model.ext.TicketX;
import com.dostojic.njt.performance.logic.websockets.encdec.TicketMessage;
import com.dostojic.njt.performance.model.ex.PerformanceX;
import com.dostojic.njt.util.JsfMessage;
import com.dostojic.njt.util.JsfUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author dostojic
 */
@ManagedBean(name = "buyTicket")
@ViewScoped
public class BuyTicket implements java.io.Serializable{
    
    public static final String MANAGED_BEAN_NAME = "buyTicket";
    
    private List<TicketX> selectedTickets;
    private List<TicketX> tickets;
    private PerformanceX perf;
    private long perfId;

    
    @PostConstruct
    public void init(){
        System.out.println("POST CONSTRUCT!!! ");

        perfId = JsfUtils.getLongPar("perfId");
        perf = new PerformanceX(perfId);
        System.out.println("DEBUG :: INFO CONSTRUCTOR :: Performace id in buyTicket: " + perfId);
        selectedTickets = new ArrayList<>();
    }

    public PerformanceX getPerf() {
        return perf;
    }

    public void setPerf(PerformanceX perf) {
        this.perf = perf;
    }

    public void setPerfId(long perfId) {
        this.perfId = perfId;
    }

    public long getPerfId() {
        return perfId;
    }
    
    
    
    public List<TicketX> getSelectedTickets() {
        return selectedTickets;
    }

    public void setSelectedTickets(List<TicketX> selectedTickets) {
        this.selectedTickets = selectedTickets;
    }
    
    public void addToStock(TicketX t){
        selectedTickets.add(t);
        Iterator<TicketX> it = getTickets().iterator();
        while (it.hasNext()){
            TicketX next = it.next();
            if (next.getSeatId() == t.getSeatId()){
                next.setStatus(TicketStatus.STATUS_IN_STORE);
                break;
            }
        }
        JsfMessage.info("Dodata je karta za sedište u " + t.getSeat().getRow() + ". redu i " + t.getSeat().getColumn() + ". koloni");
    }
    
    
    public void removeFromStock(TicketX t){
        boolean remove = selectedTickets.remove(t);
        
        Iterator<TicketX> it = getTickets().iterator();
        while (it.hasNext()){
            TicketX next = it.next();
            if (next.getSeatId() == t.getSeatId()){
                next.setStatus(TicketStatus.STATUS_FREE);
                break;
            }
        }
        System.out.println("REMOVED: " + remove);
        JsfMessage.info("Izbrisana je karta za sedište u " + t.getSeat().getRow() + ". redu i " + t.getSeat().getColumn() + ". koloni");
    }
    
    public void makeUnavailable(TicketX t){
        selectedTickets.remove(t);
        
        Iterator<TicketX> it = getTickets().iterator();
        while (it.hasNext()){
            TicketX next = it.next();
            if (next.getSeatId() == t.getSeatId()){
                next.setStatus(TicketStatus.STATUS_RESERVED);
                break;
            }
        }
        JsfMessage.info("Izbrisana je karta za sedište u " + t.getSeat().getRow() + ". redu i " + t.getSeat().getColumn() + ". koloni");
    }

    public List<TicketX> getTickets() {
        if (tickets == null){
            tickets = perf.getTickets();
        }
        return tickets;
    }
    
    public short getStatusCodeFree(){
        return TicketStatus.STATUS_FREE;
    }
    
    public short getStatusCodeInStore(){
        return TicketStatus.STATUS_IN_STORE;
    }
    
    public short getStatusCodeReserved(){
        return TicketStatus.STATUS_RESERVED;
    }
    
    public short getStatusCodeSold(){
        return TicketStatus.STATUS_SOLD;
    }
    
    public String getMsgStAllreadyThere(){
        return TicketMessage.Status.AllreadyThere.name();
    }
    
    public String getMsgStNew(){
        return TicketMessage.Status.New.name();
    }
    
    public String getMsgStInserted(){
        return TicketMessage.Status.Inserted.name();
    }
    
    public boolean showFreeTicket(long seatId){
         for (TicketX t : getTickets()){
             if (t.getSeatId() == seatId){
                 System.out.println("SEAT: " + seatId + " status: " + t.getStatus());
             }
             if (t.getSeatId() == seatId && t.getStatus() == TicketStatus.STATUS_FREE){
                 
                 return true;
             }
         }
        return false;
    }
    
}
