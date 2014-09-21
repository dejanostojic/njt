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
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

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
        processTicket(session, message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("performanceId") long perfId) {
        System.out.println("onOpen: session id: " + session.getId() + ", perfId: " + perfId);
        session.setMaxIdleTimeout(300000); // 5 minuts
        
        perfSessMap.putIfAbsent(perfId, new ConcurrentHashMap<>());
        perfSessMap.get(perfId).put(session, ConcurrentHashMap.newKeySet());

        StringBuilder ticIds = new StringBuilder("");
        for (Session s : perfSessMap.get(perfId).keySet()){
            ticIds.append(getCommaSepTics(perfId, s)).append(",");
        }
        ticIds.deleteCharAt(ticIds.length() - 1);
        
        
        String tics = "{\"addedStatus\":\"" + TicketMessage.Status.OnOpen.name() + "\",\"list\":[" + ticIds.toString() + "]," + "\"sessionId\":\"" + session.getId() + "\"}";

        
        try {
            System.out.println("TIC JSON: " + tics);
            session.getBasicRemote().sendText(tics);
        } catch (IOException ex) {
            Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("NUMBER OF SESSIONS FOR PERF: " + perfId+ ": " + perfSessMap.get(perfId).keySet().size());
        
        
        System.out.println("onOpen: Number of distinct performances: " + perfSessMap.size());
        
//        ExecutorService executor = Executors.newSingleThreadExecutor();
    }

    @OnClose
    public void onClose(Session session, @PathParam("performanceId") long perfId, CloseReason reason) {
        System.out.println("onClose: session id: " + session.getId() + ", perfId: " + perfId + " closeReason phrase: " + reason.getReasonPhrase() + ", closeReason code:" + reason.getCloseCode());
        ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> sessTicketsMap = perfSessMap.get(perfId);
        if (sessTicketsMap != null){
            String tics = "{\"addedStatus\":\"" + TicketMessage.Status.OnClose.name() + "\",\"list\":[" + getCommaSepTics(perfId, session) + "]" + "}";
            
            sessTicketsMap.remove(session);
            
            sendListMessage(tics, session.getOpenSessions(), session);
        }
    }

    @OnError
    public void onError(Throwable t){
        Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.WARNING, t.getMessage(), t);
    }
    
    private void sendListMessage(String message, Set<Session> rcps, Session sender){
        if (rcps != null){
            rcps.forEach((Session rec) -> {
                if (rec!=null && rec.isOpen() && !rec.equals(sender)) {
                    try {
                        rec.getBasicRemote().sendText(message);
                    } catch (IOException ex) {
                        Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }
    
    private void processTicket(Session ses, TicketMessage mes){
        if (mes != null){
            boolean status = false;
            if (mes.getStatus() == TicketStatus.STATUS_IN_STORE || mes.getStatus() == TicketStatus.STATUS_FREE){ // if seat is in store than release it
                if (mes.getStatus() == TicketStatus.STATUS_IN_STORE){
                    status = releaseSeat(ses, mes);
                }else{
                    status = addSeat(ses, mes);
                }
                sendMessage(ses, mes, status);
            }else if (mes.getStatus() == TicketStatus.STATUS_SOLD){ // if user triggered bought action than inform about all tickets
                String tics = "{\"addedStatus\":\"" + TicketMessage.Status.Bought.name() + "\",\"list\":[" + getCommaSepTics(mes.getPerfId(), ses) + "]}";
                ConcurrentHashMap.KeySetView<Long, Boolean> remove = perfSessMap.get(mes.getPerfId()).remove(ses);
                sendListMessage(tics, ses.getOpenSessions(), ses);
            }
            
        }
    }
    
    private boolean releaseSeat(Session ses, TicketMessage mes){
        return perfSessMap.getOrDefault(mes.getPerfId(), new ConcurrentHashMap<>()).getOrDefault(ses, ConcurrentHashMap.newKeySet()).remove(mes.getSeatId());
        
    }
    
    private boolean addSeat(Session ses, TicketMessage mes){
            boolean existed = true;
            ConcurrentHashMap<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> mainMap = perfSessMap.get(mes.getPerfId());
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
            return !existed;
    }
    
    private void sendMessage(Session sender, TicketMessage mess, boolean addedRemoved) {
        try {
            if (addedRemoved) {
                System.out.println("Seat is " + (mess.getStatus() == TicketStatus.STATUS_IN_STORE ? "FREED" : "ADDED IN STORE") + "! Sending to all.");
//                System.out.println("NUMBER OF SESSIONS FOR PERF: " + mess.getPerfId() + ": " + perfSessMap.get(mess.getPerfId()).keySet().size());
                perfSessMap.get(mess.getPerfId()).keySet().forEach((Session rec) -> {
                    try {
                        System.out.println(rec.getId() + (rec.isOpen() ? ". open" : ". closed"));
                        synchronized(rec){
                            if (rec.isOpen()) {
                                if (rec.equals(sender)) {
                                    String status = mess.getStatus() == TicketStatus.STATUS_IN_STORE ? TicketMessage.Status.Freed.name() : TicketMessage.Status.Inserted.name();
                                    System.out.println(rec.getId() + ". Is sender! Sending");
                                    rec.getBasicRemote().sendObject(new TicketMessage(mess.getPerfId(), mess.getStatus(), mess.getSeatId(), status));
                                } else {
                                    String status = mess.getStatus() == TicketStatus.STATUS_IN_STORE ? TicketMessage.Status.Freed.name() : TicketMessage.Status.New.name();
                                    System.out.println(rec.getId() + ". Sending!");
                                    rec.getBasicRemote().sendObject(new TicketMessage(mess.getPerfId(), mess.getStatus(), mess.getSeatId(), status));
                                }
                            }
                        }
                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } else {
                System.out.println("AllreadyThere! Sending back info.");
                if (sender.isOpen()) {
                    System.out.println(sender.getId() + (sender.isOpen() ? ". open" : ". closed"));
                    mess.setAddedStatus(mess.getStatus() == TicketStatus.STATUS_IN_STORE ? TicketMessage.Status.Freed.name() : TicketMessage.Status.AllreadyThere.name());
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
                }
            });
        });
        if (!arr[0]){
            System.out.println("NEW SEAT!!!");
        }
        return arr[0];
    }
    
    public static Set<Long> getTicketsForSession(long perfId, String sessionId){
        /* For some reason bean doesnt work when is lambdas is used!!!
        Optional<ConcurrentHashMap.KeySetView<Long, Boolean>> map = perfSessMap.get(perfId).entrySet().stream().findFirst().filter((Map.Entry<Session, ConcurrentHashMap.KeySetView<Long,Boolean>> e) -> {
        return e.getKey().getId().equals(sessionId);
        }).map((Map.Entry<Session, ConcurrentHashMap.KeySetView<Long,Boolean>> e)->{
        return e.getValue();
        });
        if (map.isPresent()){
        return map.get();
        }
         */
        Set<Map.Entry<Session, ConcurrentHashMap.KeySetView<Long, Boolean>>> entrySet = perfSessMap.get(perfId).entrySet();
        
        for (Map.Entry<Session, ConcurrentHashMap.KeySetView<Long, Boolean>> e : entrySet) {
            if (e.getKey().getId() != null && e.getKey().getId().equals(sessionId)){
                return e.getValue();
            }
        }
        
        return null;
        
    }
    
    public static void informTicketsSold(long perfId, String sessId){
        for(Session ses: perfSessMap.get(perfId).keySet()){
            if (ses.getId().equals(sessId)){
                
                ConcurrentHashMap.KeySetView<Long, Boolean> ticketsSet = perfSessMap.get(perfId).remove(ses); // remove tickets from map and return
                if (ticketsSet != null){
                    StringBuilder tics = new StringBuilder("0");
                    for (Long l : ticketsSet){
                           tics.append(",").append(l);
                    }

                    String message = "{\"addedStatus\":\"" + TicketMessage.Status.Bought.name() + "\",\"list\":[" +  tics.toString() + "]}"; // create json message
                    System.out.println("message inform: " + message);
                    Set<Session> rcps = ses.getOpenSessions();
                    if (rcps != null){
                        for (Session rec : rcps){
                            if (rec != null && rec.isOpen()) { // if session open than send message
                                try {
                                    rec.getBasicRemote().sendText(message);
                                } catch (IOException ex) {
                                    Logger.getLogger(TicketsEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public String getCommaSepTics(long perfId, Session session){
        final StringBuilder tics = new StringBuilder("0");
        for (Long l : getTicketsForSession(perfId, session.getId())){
               tics.append(",").append(l);
        }
        return tics.toString();
    }
    

}
