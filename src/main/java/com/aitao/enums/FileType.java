package com.aitao.enums;

/**
 * @Author : tao.ai
 * @Create : 2021/09/30 16:26
 * @Description : 文件类型枚举
 */
public enum FileType {
    XML(".xml"),
    PROPERTIES(".properties"),
    TXT(".txt"),
    UNKNOWN("未知");

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    private final String extension;//扩展名

    /**
     * 根据扩展名或文件名获取指定的文件类型
     *
     * @param name 扩张名 or 文件名
     * @return 返回文件类型
     */
    public static FileType getFileType(String name) {
        for (FileType fileType : FileType.values()) {
            if (name.toUpperCase().equals(fileType.name()) || name.toLowerCase().endsWith(fileType.extension)) {
                return fileType;
            }
        }
        return UNKNOWN;
    }
}
