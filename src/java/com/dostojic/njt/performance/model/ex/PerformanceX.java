/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.performance.model.ex;

import com.dostojic.njt.db.dao.TicketDao;
import com.dostojic.njt.model.Seat;
import com.dostojic.njt.model.TicketStatus;
import com.dostojic.njt.model.ext.StageX;
import com.dostojic.njt.model.ext.TicketX;
import com.dostojic.njt.performance.dao.PerformanceDao;
import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.play.dao.PlayDao;
import com.dostojic.njt.play.model.Play;
import com.dostojic.njt.util.BeanUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dostojic
 */
public class PerformanceX extends Performance{
    
    private Play play;
    private StageX stage;
    private List<TicketX> tickets;
    
    public PerformanceX(){}
    
    public PerformanceX(Performance performance){
        BeanUtils.copyProperties(this, performance);
    }
    
    public PerformanceX(long id){
        Performance bean = PerformanceDao.getInstance().loadByPk(id);
        if (bean != null){
            BeanUtils.copyProperties(this, bean);
        }
    }
    
    public Play getPlay() {
        if (play == null && getPlayId() > 0){
            play = PlayDao.getInstance().loadByPk(getPlayId());
        }
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public StageX getStage() {
        if (stage == null && getStageId() > 0){
            stage = new StageX(getStageId());
        }
        return stage;
    }

    public void setStage(StageX stage) {
        this.stage = stage;
    }

    public List<TicketX> getTickets() {
        if (tickets == null){
            tickets = getAllTickets(TicketDao.getInstance().loadListByPerformanceId(getId()), getStage().getSeats());
        }
        
        return tickets;
    }

    public void setTickets(List<TicketX> tickets) {
        this.tickets = tickets;
    }
    
    private List<TicketX> getAllTickets(List<TicketX> exTickets, List<Seat> seats){
        Map<Long,TicketX> ticketsBySeatId = new HashMap<>(seats.size());
        List<TicketX> tmp = new ArrayList<>(seats.size());
        
        exTickets.stream().forEach((t) -> {
            ticketsBySeatId.put(t.getSeatId(), t);
        });
        
        seats.stream().filter((s) -> (!ticketsBySeatId.containsKey(s.getId()))).forEach((s) -> {
            TicketX t = new TicketX();
            t.setSeat(s);
            t.setSeatId(s.getId());
            t.setPerformanceId(getId());
            t.setStatus(TicketStatus.STATUS_FREE);
            ticketsBySeatId.put(s.getId(), t);
        });
        
        ticketsBySeatId.values().stream().forEach(s -> tmp.add(s));
        
        Collections.sort(tmp, (TicketX t1, TicketX t2) -> {
            int res = Integer.compare(t1.getSeat().getRow(), t2.getSeat().getRow());
            
            if (res != 0){
                return res;
            }
            
            return Integer.compare(t1.getSeat().getRow(), t2.getSeat().getRow());
                   
        });

        return tmp;
    }
    
    
}
