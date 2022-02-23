package com.aitao.web.response;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-11-02 19:50
 * @Description : 分页响应对象
 */
public class PageResult {
    // 当前页
    private Integer currentPage;
    // 每页显示的总条数
    private Integer pageSize;
    // 命中数据条数
    private Long total;
    // 总页数
    private Integer totalPage;
    // 开始索引
    private Integer startIndex;
    // 是否有下一页
    private boolean isNext;
    // 是否有上一页
    private boolean isPrev;
    // 分页结果
    private List<T> data;

    public static Map<Object, Object> params = new HashMap<>();

    public PageResult(Integer currentPage, Integer pageSize, Long total) {
        params.put("currentPage", currentPage);
        params.put("pageSize", pageSize);
        params.put("total", total);
        params.put("startIndex", (this.currentPage - 1) * this.pageSize);
        params.put("totalPage", ((Long) ((this.total + this.pageSize - 1) / this.pageSize)).intValue());
        params.put("isNext", this.currentPage < this.totalPage);
        params.put("isPrev", this.currentPage >= this.totalPage);
    }

    public <K, V> PageResult putItem(K key, V value) {
        params.put(key, value);
        return this;
    }

    public Integer getCurrentPage() {
        return (Integer) params.get("currentPage");
    }

    public PageResult setCurrentPage(Integer currentPage) {
        params.put("currentPage", currentPage);
        return this;
    }

    public Integer getPageSize() {
        return (Integer) params.get("pageSize");
    }

    public PageResult setPageSize(Integer pageSize) {
        params.put("pageSize", pageSize);
        return this;
    }

    public Long getTotal() {
        return (Long) params.get("total");
    }

    public PageResult setTotal(Long total) {
        params.put("total", total);
        return this;
    }

    public Integer getTotalPage() {
        return (Integer) params.get("totalPage");
    }

    public PageResult setTotalPage(Integer totalPage) {
        params.put("totalPage", totalPage);
        return this;
    }

    public Integer getStartIndex() {
        return (Integer) params.get("startIndex");
    }

    public PageResult setStartIndex(Integer startIndex) {
        params.put("startIndex", startIndex);
        return this;
    }

    public boolean isNext() {
        return (boolean) params.get("isNext");
    }

    public PageResult setNext(boolean isNext) {
        params.put("isNext", isNext);
        return this;
    }

    public boolean isPrev() {
        return (boolean) params.get("isPrev");
    }

    public PageResult setPrev(boolean isPrev) {
        params.put("isPrev", isPrev);
        return this;
    }

    public List<T> getData() {
        return (List<T>) params.get("data");
    }

    public PageResult setData(List<T> data) {
        params.put("data", data);
        return this;
    }
}
