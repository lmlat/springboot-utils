package com.aitao.beans;

import com.aitao.enums.MimeType;

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
