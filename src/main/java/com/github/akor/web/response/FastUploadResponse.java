package com.github.akor.web.response;

import com.github.akor.exception.FastDFSException;

import java.io.Serializable;

/**
 * @Author : AiTao
 * @Create : 2021-10-23 17:08
 * @Description : FastDFS文件上传响应结果
 */
public class FastUploadResponse implements Serializable {
    private String groupName;//组名
    private String remotePath;//远程访问路径

    public FastUploadResponse(String[] params) {
        if (params != null && params.length == 2) {
            this.groupName = params[0];
            this.remotePath = params[1];
        } else {
            throw new FastDFSException("缺少上传文件响应参数");
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
}
