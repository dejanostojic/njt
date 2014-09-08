/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.util;

/**
 *
 * @author dostojic
 */
public class BaseListFilter {

    protected int orderBy = -1;
    protected boolean desc = false;
    protected String[] orderByColumns;
    protected String defaultOrder;
    protected int pageSize = 30;

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public String[] getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(String[] orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public void changeOrder(int ord) {
        if (orderBy == ord) {
            if (!desc) {
                desc = true;
            } else {
                // vracamo na default order
                orderBy = -1;
            }
        } else {
            desc = false;
            orderBy = ord;
        }
    }

    public String getOrderByClause() {
        if (orderByColumns == null) {
            return null;
        }
        if (orderBy == -1) {
            return defaultOrder;
        }
        return orderByColumns[orderBy] + (desc ? " desc" : "");

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
