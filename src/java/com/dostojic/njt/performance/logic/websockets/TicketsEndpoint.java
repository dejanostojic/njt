/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.performance.logic.websockets;

import com.dostojic.njt.model.TicketStatus;
import com.dostojic.njt.performance.logic.websockets.encdec.TicketDecoder;
import com.dostojic.njt.performance.logic.websockets.encdec.TicketEncoder;
import com.dostojic.njt.performance.logic.websockets.encdec.TicketMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.primefaces.json.JSONObject;

/**
 *
 * @author dostojic
 */
@ServerEndpoint(
        value = "/dost/tickets/{performanceId}",
        encoders = {TicketEncoder.class},
        decoders = {TicketDecoder.class}
)
public class TicketsEndpoint {

    private final static ConcurrentHashMap<Long,ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>>> perfSessMap; // perfId -> (sesija -> setSeats)
    
    static {
        perfSessMap = new ConcurrentHashMap<>();
    }
    
    @OnMessage
    public void messageReceiver(Session session, TicketMessage message, @PathParam("performanceId") long perfId) {
        System.out.println("Received message! session id: " + session.getId() + ", pid:" + message.getPerfId() + ", seatId: " + message.getSeatId() + ", status: " + TicketStatus.getTextStatus(message.getStatus()));
        addSeat(session, message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("performanceId") long perfId) {
        System.out.println("onOpen: session id: " + session.getId() + ", perfId: " + perfId);
        
//        ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> get = perfSessMap.get(perfId);
        
        if (perfSessMap.get(perfId) == null){
            perfSessMap.put(perfId, new ConcurrentHashMap<>());
        }
        perfSessMap.get(perfId).put(session, ConcurrentHashMap.newKeySet());
//        
//        get.put(session, ConcurrentHashMap.newKeySet());
//        perfSessMap.putIfAbsent(perfId, perfSessMap.getOrDefault(perfId, new ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>>(){{
//            put(session, ConcurrentHashMap.newKeySet());
//        }}));
        
        
        StringBuilder tics = new StringBuilder("{");

        
        perfSessMap.get(perfId).entrySet().forEach((Map.Entry<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> e) -> {
            e.getKey();
            tics.append("\"").append("ses_").append(e.getKey().getId()).append("\"").append(":").append("[0");
            e.getValue().forEach((Long l) -> {
                tics.append(l).append(",");
            });
            tics.setCharAt(tics.length() - 1, ']');
            tics.append(",");
        });
        tics.setCharAt(tics.length() - 1, '}');
        

        
        try {
            System.out.println("TIC JSON: " + tics.toString());
            session.getBasicRemote().sendText(tics.toString());
        } catch (IOException ex) {
            Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("NUMBER OF SESSIONS FOR PERF: " + perfId+ ": " + perfSessMap.get(perfId).keySet().size());
        
        
        System.out.println("onOpen: Number of distinct performances: " + perfSessMap.size());
    }

    @OnClose
    public void onClose(Session session, @PathParam("performanceId") long perfId) {
        System.out.println("onClose: session id: " + session.getId() + ", perfId: " + perfId);
        ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> sessTicketsMap = perfSessMap.get(perfId);
        if (sessTicketsMap != null){
            sessTicketsMap.remove(session);
        }
    }

    @OnError
    public void onError(Throwable t){
        Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.WARNING, t.getMessage(), t);
    }
    
    private void addSeat(Session ses, TicketMessage mes){
        
        if (mes != null){
            boolean existed = true;
            ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> mainMap = perfSessMap.get(new Long(mes.getPerfId()));
            if (mainMap != null){
                ConcurrentHashMap.KeySetView<Long, Boolean> ticketsSet = mainMap.getOrDefault(ses, ConcurrentHashMap.newKeySet());
                if (!checkSeatExists(mes.getSeatId(), ses, mainMap)){
                    ticketsSet.add(mes.getSeatId());
                    mainMap.put(ses, ticketsSet);
                    existed = false;
                    System.out.println("NEW TICKET!");
                }
            }
            System.out.println("TICKET IS ADDED Y/N: " + !existed);
            sendMessage(ses, mes, !existed);
        }
        
    }
    
    private void sendMessage(Session sender, TicketMessage mess, boolean isAdded) {
        try {
            if (isAdded) {
                System.out.println("New seat is reserved! Sending to all.");
                System.out.println("NUMBER OF SESSIONS FOR PERF: " + mess.getPerfId() + ": " + perfSessMap.get(mess.getPerfId()).keySet().size());
                perfSessMap.get(mess.getPerfId()).keySet().forEach((Session rec) -> {
                    try {
                    System.out.println(rec.getId() + (rec.isOpen() ? ". open" : ". closed" ));
                    if (rec.isOpen()) {
                        if (rec.equals(sender)) {
                            System.out.println(rec.getId() + ". Is sender! Sending");
                            
                            
                                rec.getBasicRemote().sendObject(new TicketMessage(mess.getPerfId(), mess.getStatus(), mess.getSeatId(), TicketMessage.Status.Inserted.name()));
                        }else {
                            System.out.println(rec.getId() + ". Sending!");
                            rec.getBasicRemote().sendObject(new TicketMessage(mess.getPerfId(), mess.getStatus(), mess.getSeatId(), TicketMessage.Status.New.name()));
                        }
                        } 
                    }
                     catch (IOException | EncodeException ex) {
                                Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                            }
                });
                
                /*
                Iterator<Map.Entry<Session, ConcurrentHashMap.KeySetView<Long, Boolean>>> it = perfSessMap.get(mess.getPerfId()).entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> next = it.next();
                    Session rec = next.getKey();
                    System.out.println(rec.getId() + (rec.isOpen() ? ". open" : ". closed" ));
                    if (rec.isOpen()) {
                        if (rec.equals(sender)) {
                            System.out.println(rec.getId() + ". Is sender! Sending");
                            
                            rec.getBasicRemote().sendObject(new TicketMessage(mess.getPerfId(), mess.getStatus(), mess.getSeatId(), TicketMessage.Status.Inserted.name()));
                        } else {
                            System.out.println(rec.getId() + ". Sending!");
                            rec.getBasicRemote().sendObject(new TicketMessage(mess.getPerfId(), mess.getStatus(), mess.getSeatId(), TicketMessage.Status.New.name()));
                        }
                    }
                }*/
            }else{
                System.out.println("AllreadyThere! Sending back info.");
                if (sender.isOpen()){
                    System.out.println(sender.getId() + (sender.isOpen() ? ". open" : ". closed" ));
                    mess.setAddedStatus(TicketMessage.Status.AllreadyThere.name());
                    sender.getBasicRemote().sendObject(mess);
                }
            }
        } catch (EncodeException | IOException e) {
            Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private boolean checkSeatExists(long seatId, Session session, ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> sessSeats){
        final boolean[] arr = new boolean[1];
        arr[0] = false;
        sessSeats.forEachEntry(1, (Map.Entry<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> e) -> {
            e.getValue().forEach((Long t) -> {
                if (t.longValue() == seatId && !e.getKey().equals(session)){  // ovde ce verovatno sad sa statusima
                    arr[0] = true;
                    System.out.println("THIS SESSION HAD ALREADY INSERTED THIS SEAT!");
                }
            });
        });
        if (!arr[0]){
            System.out.println("NEW SEAT!!!");
        }
        return arr[0];
    }
    
    

}
