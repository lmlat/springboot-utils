package com.github.akor.beans;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * 扩展File类
 *
 * @Company QAX
 * @Author : admin
 * @Create : 2022/1/26 11:23
 */
public class ExtFile extends AbstractFile {
    public ExtFile(String pathname) {
        super(pathname);
    }

    public ExtFile(String parent, String child) {
        super(parent, child);
    }

    public ExtFile(File parent, String child) {
        super(parent, child);
    }

    public ExtFile(URI uri) {
        super(uri);
    }

    @Override
    public String getType() {
        try {
            return new Tika().detect(this);
        } catch (IOException e) {
            throw new RuntimeException("检测文件类型异常");
        }
    }
}
