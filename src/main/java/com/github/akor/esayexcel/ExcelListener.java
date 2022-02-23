package com.github.akor.esayexcel;

import com.github.akor.model.User;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-10-21 19:31
 * @Description :
 */
public class ExcelListener extends AnalysisEventListener<User> {
    //最终结果集合
    List<User> resultList = new ArrayList<>();

    //一行一行读取excel内容
    @Override
    public void invoke(User readUser, AnalysisContext analysisContext) {
        System.out.println("user:" + readUser);
        resultList.add(readUser);
    }

    //读取表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("head:" + headMap);
    }

    //读取数据完成后执行的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完成");
    }
}
