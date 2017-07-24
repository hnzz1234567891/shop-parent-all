package com.shop.core.base;

public class PageInfo {

    private Integer limit;;
    private Integer page;;
    private Integer totalCount;
    private Integer offset;
    private Integer totalPages;
    private Integer[] slider;
    private boolean firstPage;
    private boolean hasPrePage;
    private boolean lastPage;
    private boolean hasNextPage;
    private Integer nextPage;
    private Integer prePage;;
    private Integer startRow;;
    private Integer endRow;

    public Integer getLimit() {
        return limit;
    }
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public Integer getOffset() {
        return offset;
    }
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
    public Integer getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    public Integer[] getSlider() {
        return slider;
    }
    public void setSlider(Integer[] slider) {
        this.slider = slider;
    }
    public boolean isFirstPage() {
        return firstPage;
    }
    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }
    public boolean isHasPrePage() {
        return hasPrePage;
    }
    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }
    public boolean isLastPage() {
        return lastPage;
    }
    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
    public boolean isHasNextPage() {
        return hasNextPage;
    }
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
    public Integer getNextPage() {
        return nextPage;
    }
    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }
    public Integer getPrePage() {
        return prePage;
    }
    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }
    public Integer getStartRow() {
        return startRow;
    }
    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }
    public Integer getEndRow() {
        return endRow;
    }
    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }
}
