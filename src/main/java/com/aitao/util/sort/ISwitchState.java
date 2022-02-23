package com.aitao.util.sort;

/**
 * @Author : AiTao
 * @Create : 2021-12-21 2:52
 * @Description : 排序状态接口
 */
public interface ISwitchState {
    /**
     * 冒泡排序
     */
    ISort bubble();

    /**
     * 插入排序
     */
    ISort insert();

    /**
     * 默认排序
     */
    ISort normal();
}
