/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.util;

import java.util.List;

/**
 *
 * @author dostojic
 */
public abstract class AbstractLister<Model> implements java.io.Serializable{
    
    public abstract List<Model> getList();
    
    
    
}
