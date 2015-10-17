/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so;

import com.dostojic.njt.db.Database;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dejan
 */
public abstract class GenericSO {
  /*  
    public final void execute() throws Exception{
        try {
            Database.getCurrent().beginTransaction();
            checkPrecondition();
            executeOperation();
            Database.getCurrent().commit();
        } catch (Exception ex) {
            Logger.getLogger(GenericSO.class.getName()).log(Level.SEVERE, null, ex);
            Database.getCurrent().rollback();
            throw ex;
        }
        
    }
    
    protected abstract void checkPrecondition() throws Exception;
    protected abstract void executeOperation() throws Exception;
    
    public final void execute(Object o) throws Exception{
        try {
            Database.getCurrent().beginTransaction();
            checkPrecondition(o);
            executeOperation(o);
            Database.getCurrent().commit();
        } catch (Exception ex) {
            Logger.getLogger(GenericSO.class.getName()).log(Level.SEVERE, null, ex);
            Database.getCurrent().rollback();
            throw ex;
        }
        
    }
    
    protected abstract void checkPrecondition(Object o) throws Exception;
    protected abstract void executeOperation(Object o) throws Exception;
    
 */   
    public final void execute(Object... args) throws Exception{
        try {
            Database.getCurrent().beginTransaction();
            checkPrecondition(args);
            executeOperation(args);
            Database.getCurrent().commit();
        } catch (Exception ex) {
            Logger.getLogger(GenericSO.class.getName()).log(Level.SEVERE, null, ex);
            Database.getCurrent().rollback();
            throw ex;
        }
        
    }
    
    protected abstract void checkPrecondition(Object... args) throws Exception;
    protected abstract void executeOperation(Object... args) throws Exception;
    
}
