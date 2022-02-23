package com.github.akor.poi;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
public class RoleService {
    @Resource
    private RoleMapper roleMapper;

    public List<Role> queryAll() {
        return roleMapper.queryAll();
    }
}