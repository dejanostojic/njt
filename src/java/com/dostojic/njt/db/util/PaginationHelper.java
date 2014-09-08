/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.db.util;

/**
 *
 * @author dostojic
 */
public abstract class PaginationHelper {

    public PaginationHelper(int rowCount, int pageSize, int page) {
        setPageSize(pageSize);
        setRowCount(rowCount);
        setPage(page);
    }
    
    private int pageSize=10;
    private int page =1;
    private int rowCount;

    public int getPageSize() {
        return pageSize;
    }

    private void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page > getPageCount())
           page = getPageCount();
        if(page < 1)
           page = 1; 
        this.page = page;
    }

    public int getRowCount() {
        return rowCount;
    }

    private void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    public int getOffset() {
        return (page - 1)*pageSize;
    }
    
    
    public boolean isFirstPage() {
        return page==1;
    }
    
    public int getPageCount() {
        return (rowCount+pageSize-1)/pageSize;
    }
    public boolean isLastPage() {
        int pageCount = getPageCount();
        return page == pageCount || (page == 1 && pageCount == 0);
    }
    
    public int getFirstRowOnPage() {
        return (page - 1) * pageSize + 1;
    }
    
    public int getLastRowOnPage() {
        int last = page * pageSize;
        if(last > rowCount)
            last = rowCount;
        return last;
    }
    
    public int getRowCountOnPage() {
        return getLastRowOnPage() - getFirstRowOnPage() +1;
    }
    
    public int getNextPage() {
        if(!isLastPage()) {
            return page+1;
        } else {
            return page;
        }
    }
    
    public int getPreviousPage() {
        if(!isFirstPage()) {
            return page - 1;
        } else
            return page;
    }
    
    public boolean isPagingVisable(){
        return rowCount > pageSize;
    }
}

