/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.so.performance;

import com.dostojic.njt.performance.model.Performance;
import com.dostojic.njt.db.dao.PerformanceDao;
import com.dostojic.njt.so.GenericSO;
import java.util.List;

/**
 *
 * @author dejan
 */
public class GetAllPerformances extends GenericSO{

    private List<Performance> performances;

    public List<Performance> getPerformances() {
        return performances;
    }
    
    @Override
    protected void checkPrecondition(Object... args) throws Exception {
    }

    @Override
    protected void executeOperation(Object... args) throws Exception {
        performances = PerformanceDao.getInstance().loadAll();
    }
    
}
