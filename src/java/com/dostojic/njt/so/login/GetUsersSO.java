/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.login;

import com.dostojic.njt.db.dao.UserDao;
import com.dostojic.njt.model.User;
import com.dostojic.njt.so.GenericSO;
import java.util.List;

/**
 *
 * @author dejan
 */
public class GetUsersSO extends GenericSO{

    List<User> users;

    public List<User> getUsers() {
        return users;
    }

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
        
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        users = UserDao.getInstance().loadAll();
    }
    
}
