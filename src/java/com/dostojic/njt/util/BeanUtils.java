/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.util;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dostojic
 */
public class BeanUtils implements java.io.Serializable{
    
    
    public static <T> void copyProperties(T dest, T orig) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
