/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dejan
 */
public interface TicketStatus {
    public static final short STATUS_FREE = 0;
    public static final short STATUS_RESERVED = 1;
    public static final short STATUS_SOLD = 2;
    public static final short STATUS_IN_STORE = 3;
    
    public static final Map<Short, String> getText = new HashMap<Short, String>() {
        {
            put(STATUS_FREE, "Slobodna");
            put(STATUS_RESERVED, "Rezervisana");
            put(STATUS_SOLD, "Prodana");
            put(STATUS_IN_STORE, "Izabrana");
        }
    };
}
