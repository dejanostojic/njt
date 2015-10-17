/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.play;

import com.dostojic.njt.db.dao.PlayDao;
import com.dostojic.njt.so.GenericSO;

/**
 *
 * @author dejan
 */
public class DeletePlaySO extends GenericSO{

    @Override
    protected void checkPrecondition(Object... args) throws Exception {
        
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        PlayDao.getInstance().deleteByPk(((Long) args[0]).longValue());
    }
}
