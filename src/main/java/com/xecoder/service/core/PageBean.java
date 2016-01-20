package com.xecoder.service.core;

import org.springframework.data.domain.Sort;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/12-15:06
 * Feeling.com.xecoder.service
 */
public class PageBean {
    private int totalCount;
    private int currentPage;
    private int maxPage;
    private Sort.Direction sort;
    private String sortColumn;

    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getMaxPage() {
        return maxPage;
    }
    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public Sort.Direction getSort() {
        return sort;
    }

    public void setSort(Sort.Direction sort) {
        this.sort = sort;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
}
