package com.github.akor.beans;

import com.github.akor.enums.MimeType;

import java.io.File;
import java.net.URI;

/**
 * 抽象文件类
 *
 * @Company QAX
 * @Author : admin
 * @Create : 2022/1/26 11:17
 */
public abstract class AbstractFile extends File implements IFile {
    //private Map<String, MimeType> MIMETYPES = new HashMap<>();
    //
    //{
    //    Arrays.stream(MimeType.values()).forEach(mimeType -> MIMETYPES.put(mimeType.getName(), mimeType));
    //}

    public AbstractFile(String pathname) {
        super(pathname);
    }

    public AbstractFile(String parent, String child) {
        super(parent, child);
    }

    public AbstractFile(File parent, String child) {
        super(parent, child);
    }

    public AbstractFile(URI uri) {
        super(uri);
    }

    @Override
    public MimeType getMimeType() {
        return MimeType.getInstance(getType());
    }

    public abstract String getType();
}
