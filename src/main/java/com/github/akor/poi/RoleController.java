package com.github.akor.poi;

import com.github.akor.files.ExcelUtils;
import com.github.akor.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @RequestMapping("/export/excel")
    public void reportExportExcel(HttpServletResponse response, HttpServletRequest request) {
        //得到所有要导出的数据
        List<Role> exportDtoList = roleService.queryAll();
        ExcelUtils.export("角色表", exportDtoList, Role.class, request, response);
    }

    @RequestMapping("/test/valid")
    @ResponseBody
    public String testValidator(@RequestBody @Valid User user) {
        System.out.println("user:" + user);
        return "ok";
    }

    @RequestMapping("/test/cookie")
    public void testCookie() {

    }
}