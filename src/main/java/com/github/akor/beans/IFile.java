package com.github.akor.beans;

import com.github.akor.enums.MimeType;

/**
 * 扩展文件接口
 *
 * @Company QAX
 * @Author : ai.tao
 * @Create : 2022/2/23 15:48
 */
public interface IFile {
    /**
     * 检测文件类型
     *
     * @return {@link String 文件类型}
     */
    MimeType getMimeType();
}
