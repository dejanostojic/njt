/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.performance.logic.websockets.encdec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author dostojic
 */
public class TicketEncoder implements Encoder.Text<TicketMessage>{

    @Override
    public String encode(TicketMessage message) throws EncodeException {
        JSONObject obj = new JSONObject();
        try {
            obj.put("status", message.getStatus());
            obj.put("seatId", message.getSeatId());
            obj.put("pefId", message.getPerfId());
            obj.put("addedStatus", message.getAddedStatus());
        } catch (JSONException ex) {
            Logger.getLogger(TicketEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj.toString();
    }

    @Override
    public void init(EndpointConfig config) {
        System.out.println("ENCODER INIT");
    }

    @Override
    public void destroy() {
        System.out.println("ENCODER DESTROYED");
    }
    
}
