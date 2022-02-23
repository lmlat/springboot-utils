package com.aitao.poi;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-09-02 22:26
 * @Description :
 */
@Repository
public interface RoleMapper {
    @Results(id = "BaseResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "summary", column = "summary"),
            @Result(property = "growRatio", column = "grow_ratio"),
            @Result(property = "status", column = "status"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
    })
    @Select("select *from role")
    List<Role> queryAll();
}
