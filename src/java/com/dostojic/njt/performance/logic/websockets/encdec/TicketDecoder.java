/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.performance.logic.websockets.encdec;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author dostojic
 */
public class TicketDecoder implements Decoder.Text<TicketMessage>{

    @Override
    public TicketMessage decode(String arg0) throws DecodeException {
        TicketMessage tm = new TicketMessage();
        try {
            JSONObject jo = new JSONObject(arg0);
            tm.setStatus((short) jo.getInt("status"));
            tm.setSeatId(jo.getLong("seatId"));
            tm.setPerfId(jo.getLong("perfId"));
        } catch (JSONException ex) {
            Logger.getLogger(TicketDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tm;
    }

    @Override
    public boolean willDecode(String arg0) {
        try {
            new JSONObject(arg0);
            System.out.println("OD KLIJENTA STIGLO: " + arg0);
            return true;
        } catch (JSONException ex) {
            Logger.getLogger(TicketDecoder.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("INIT TICKET DECODER");
    }

    @Override
    public void destroy() {
        System.out.println("destroy ticket DECODER");
    }
    
}
