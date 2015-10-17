/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.ticket;

import com.dostojic.njt.model.Ticket;
import com.dostojic.njt.db.dao.TicketDao;
import com.dostojic.njt.so.GenericSO;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dejan
 */
public class SaveTicketsSO extends GenericSO{

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        Set<Ticket> tickets = (Set<Ticket>) args[0];
        Iterator<Ticket> it = tickets.iterator();
        while (it.hasNext()){
            TicketDao.getInstance().updateOrInsert(it.next());
        }
    }
    
}
