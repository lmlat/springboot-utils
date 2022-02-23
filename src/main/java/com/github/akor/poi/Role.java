package com.github.akor.poi;

import com.github.akor.annotation.ExcelExportReport;
import com.github.akor.model.User;

import java.util.Date;

/**
 * @Author : AiTao
 * @Create : 2021-12-01 16:06
 * @Description :
 */
public class Role {
    @ExcelExportReport(property = "id", title = "角色编号")
    private Integer id;
    @ExcelExportReport(property = "name", title = "角色名")
    private String name;
    @ExcelExportReport(property = "summary", title = "简介")
    private String summary;
    @ExcelExportReport(property = "growRatio", title = "成长比率", decimal = 2)
    private double growRatio;
    @ExcelExportReport(property = "status", title = "状态")
    private byte status;
    @ExcelExportReport(property = "gmtCreate", title = "创建时间")
    private Date gmtCreate;
    @ExcelExportReport(property = "gmtModified", title = "修改时间")
    private Date gmtModified;
    @ExcelExportReport(property = "user.username", title = "用户名")
    private final User user = new User("aitao", "123456");

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getGrowRatio() {
        return growRatio;
    }

    public void setGrowRatio(double growRatio) {
        this.growRatio = growRatio;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
