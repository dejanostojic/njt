/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.util;

/**
 *
 * @author dejan
 */
public class WrappedException extends RuntimeException {
    
    public WrappedException(Exception e) {
        super(e);
    }
    
    @Override
    public Exception getCause() {
        return (Exception) super.getCause();
    }
}
