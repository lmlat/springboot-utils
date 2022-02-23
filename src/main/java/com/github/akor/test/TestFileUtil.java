package com.github.akor.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.NameFileFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TestFileUtil {

    public static void main(String[] args) throws IOException {
        File file1 = new File("D:\\Test\\aa.txt");
        File file2 = new File("D:\\Test\\bb.txt");
        File fileDirectory = new File("D:\\Test");

        //1.创建文件(获取文件)
        //1.1创建文件
        //1)从文件夹中获取指定文件
        File file = FileUtils.getFile(fileDirectory, "aa.txt");
        //2)获取获取临时文件z
        File tmpFile = FileUtils.getTempDirectory();
        //3)获取临时文件路径
        String tmpFilePath = FileUtils.getTempDirectoryPath();
        //4)获取用户文件
        File userFileDirectory = FileUtils.getUserDirectory();
        //5)获取用户文件路径
        String userFileDirectoryPath = FileUtils.getUserDirectoryPath();

        //1.2 创建文件夹
        //1)强制创建文件夹
        FileUtils.forceMkdir(fileDirectory);
        //2)强制创建父文件夹
        //FileUtils.forceMkdirParent(file1);

        //2删除文件
        //2.1删除文件夹
        //1)删除文件夹中的内容(包括子文件夹和子文件)
        FileUtils.cleanDirectory(new File("D:\\Test"));
        //2)温和的删除文件,不抛出异常
        FileUtils.deleteQuietly(file1);
        //3)强制删除文件
        FileUtils.forceDelete(file1);
        //4)JVM退出时强制删除文件
        FileUtils.forceDeleteOnExit(file1);


        //3修改文件
        //3.1修改文件
        //1)移动文件夹
        FileUtils.moveDirectory(file1,file2);
        //3.2修改文件夹
        //1)移动文件夹
        FileUtils.moveDirectory(file1,file2);
        //2)将文件移到指定的文件夹
        FileUtils.moveFileToDirectory(file1,fileDirectory,true);
        //3)移动文件或文件夹到指定的文件夹
        FileUtils.moveToDirectory(file1,fileDirectory,true);

        //4.文件查找
        //1)是否递归查找指定扩展名的文件
        String[] extensions  = new String[]{"txt","jpg","png"};
        Collection<File> fileList = FileUtils.listFiles(fileDirectory, extensions, true);

        //5.读取文件
        //1)根据文件获取文件输入流
        FileInputStream fileInputStream = FileUtils.openInputStream(file1);
        //2)读取文件到字节数组
        byte[] bytes = FileUtils.readFileToByteArray(file1);
        //3)读取文件到字符串
        String fileContent = FileUtils.readFileToString(file1, "UTF-8");
        //4)读取文件到集合中
        List<String> fileContentList = FileUtils.readLines(file1, "UTF-8");
        //5)获取文件大小
        long fileSize = FileUtils.sizeOf(file1);


        //6.写文件
        //1)根据文件获取输出流
        FileOutputStream fileOutputStream = FileUtils.openOutputStream(file1);
        //2)获取文件输出流并指定是否追加到文件中
        FileOutputStream fileOutputStream1 = FileUtils.openOutputStream(file1, true);
        //3)将字节数组内容写到文件中,文件不存在时创建
        FileUtils.writeByteArrayToFile(file1, bytes);
        //4)将字节数组内容写到文件中,文件不存在时创建,并指定是否追加
        FileUtils.writeByteArrayToFile(file1,bytes,true);
        //5)将集合数据按行写到文件中
        FileUtils.writeLines(file1,fileContentList);
        //6)将集合数据按行写到文件中,并指定是否追加
        FileUtils.writeLines(file1,fileContentList,true);
        //7)将字符串写到文件中,并指定编码
        FileUtils.writeStringToFile(file,fileContent,"UTF-8");
        //8)将字符串数据写到文件中,并指定编码和是否追加的方式
        FileUtils.writeStringToFile(file,fileContent,"UTF-8",true);

        //7.复制文件
        //7.1复制文件目录
        //1)复制文件目录中的内容到另一个目录
        File fileDir = new File("D:\\Test");
        File destDir = new File("D:\\Test1");
        FileUtils.copyDirectory(fileDir, destDir);
        //2)复制文件目录,并指定是否保存文件日期
        FileUtils.copyDirectory(fileDir,destDir,true);
        //3)使用文件过滤器过滤文件
        FileUtils.copyDirectory(fileDir,destDir,new NameFileFilter("aa"));
        //7.2复制文件
        //1)复制文件
        FileUtils.copyFile(file1, file2);
        //2)复制文件并指定是否保留文件日期
        FileUtils.copyDirectory(file1,file2,true);
        //3)复制文件夹到目录
        FileUtils.copyFileToDirectory(file1,destDir);
        //4)将url资源复制到指定文件
        FileUtils.copyURLToFile(new URL(""), file);

        //8.文件过滤

        //9.文件比较与判断
        //1)比较文件内容是否相同
        System.out.println(FileUtils.contentEquals(file1, file2));
        //2)判断一个文件夹是否包含另外一个文件夹
        System.out.println(FileUtils.directoryContains(file1,file2));
        //3)判断文件是否新文件
        System.out.println(FileUtils.isFileNewer(file1, new Date()));
        System.out.println(FileUtils.isFileOlder(file1,new Date()));
        //4)判断文件和另外一个文件比是否是新的
        System.out.println(FileUtils.isFileNewer(file1,file2));
        System.out.println(FileUtils.isFileOlder(file1,file2));

        //10.涉及文件操作的数据转换
        //1)将文件集合转换为文件数组
        File[] fileArray = FileUtils.convertFileCollectionToFileArray(fileList);
        //2)将url资源转换为文件对象
        File newUrlFiles = FileUtils.toFile(new URL(""));
        //3)将多个url资源转换为文件数组
        URL[] urls = new URL[]{};
        File[] newUrlFiles1 = FileUtils.toFiles(urls);
        //4将文件数组转化为url资源
        URL[] urls1 = FileUtils.toURLs(newUrlFiles1);

        //11.文件迭代
        LineIterator iterator = FileUtils.lineIterator(file1, "UTF-8");
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
