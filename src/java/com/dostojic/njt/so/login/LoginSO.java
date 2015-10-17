/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.so.login;

import com.dostojic.njt.model.User;
import com.dostojic.njt.db.dao.UserDao;
import com.dostojic.njt.so.GenericSO;

/**
 *
 * @author dostojic
 */
public class LoginSO extends GenericSO{
    
    private User user;
    
    @Override
    protected void checkPrecondition(Object... args) throws Exception {
        
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        
        String userName = (String) args[0];
        String pass = (String) args[1];
        User u = UserDao.getInstance().loadByUsername(userName);
        if (u != null && pass.equals(u.getPassword())){
            user = u;
        }
    }

    public User getUser() {
        return user;
    }
    
    
    
    
    
}
