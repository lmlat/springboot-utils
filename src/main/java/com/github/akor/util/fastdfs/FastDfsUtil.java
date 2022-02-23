package com.github.akor.util.fastdfs;

import com.github.akor.common.DateUtils;
import com.github.akor.exception.FastDFSException;
import com.github.akor.web.response.FastUploadResponse;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Author : AiTao
 * Create : 2021/4/16 3:15
 * Description : fastDFS文件系统工具类
 */
@Component
public class FastDfsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastDfsUtil.class);
    /**
     * Storage客户端
     */
    private static StorageClient sc;
    /**
     * Storage服务端
     */
    private static StorageServer ss;
    /**
     * Tracker服务端
     */
    private static TrackerServer ts;
    /**
     * Tracker客户端
     */
    private static TrackerClient tc;

    private static final Integer DEFAULT_PORT = 80;

    //初始化
    static {
        try {
            String filePath = new ClassPathResource("fdfs.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);//初始化配置文件
            sc = createStorageClient();//Storage客户端
            ss = createStorageServer();//Storage服务端
            tc = createTrackerClient();//Tracker客户端
            ts = createTrackerServer();//Tracker服务端
        } catch (MyException | IOException e) {
            throw new FastDFSException("FastDFS初始化异常", e);
        }
    }

    /**
     * 上传文件(Web)
     *
     * @param uf 文件对象
     * @return
     */
    public static FastUploadResponse upload(UploadFile uf) {
        //配置文件属性信息【可选参数】
        NameValuePair[] nvp = new NameValuePair[1];
        nvp[0] = new NameValuePair("author", uf.getAuthor());
        //上传结果
        String[] uploadResult = null;
        try {
            uploadResult = sc.upload_file(uf.getContent(), uf.getExt(), nvp);
        } catch (IOException | MyException e) {
            LOGGER.error("文件上传异常, filename:{}, path:{}", uf.getFilename(), uf.getLocalPath(), e);
            return null;
        }
        LOGGER.info("文件上传时间:{}", DateUtils.toString(new Date()));
        if (uploadResult == null && sc != null) {
            LOGGER.error("文件上传失败, errorCode:{}", sc.getErrorCode());
            return null;
        }
        FastUploadResponse fastUploadResponse = new FastUploadResponse(uploadResult);
        LOGGER.info("文件上传成功, groupName:{}, remotePath:{}", fastUploadResponse.getGroupName(), fastUploadResponse.getRemotePath());
        return fastUploadResponse;
    }

    /**
     * 本地文件上传
     *
     * @param localPath   本地文件路径
     * @param fileExtName 文件扩展名
     * @return
     */
    public static String[] upload(String localPath, String fileExtName) {
        //上传结果
        String[] uploadResult = null;
        try {
            System.out.println("localPath:" + localPath + " " + "fileExtName:" + fileExtName);
            uploadResult = sc.upload_file(localPath, fileExtName, null);
        } catch (IOException | MyException e) {
            System.out.println("上传失败,文件路径为:" + localPath);
            throw new RuntimeException(e);
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "上传成功");
        if (uploadResult == null && sc != null) {
            System.out.println("上传失败" + sc.getErrorCode());
            return null;
        }
        System.out.println("上传成功,group_name:" + uploadResult[0] + ",remoteFileName:" + uploadResult[1]);
        return uploadResult;
    }

    /**
     * 下载文件(web)
     *
     * @param groupName      组名
     * @param remoteFileName 完整文件名
     * @return 返回字节输入流
     */
    public static InputStream download(String groupName, String remoteFileName) {
        try {
            //下载文件
            byte[] bytes = sc.download_file(groupName, remoteFileName);
            //返回字节输入流
            return new ByteArrayInputStream(bytes);
        } catch (IOException | MyException e) {
            System.out.println("下载失败!!!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param groupName      组名
     * @param remoteFilename 完整文件名
     * @return 只有返回0才表示文件删除成功
     */
    public static int delete(String groupName, String remoteFilename) {
        int flag = -1;
        try {
            flag = sc.delete_file(groupName, remoteFilename);
        } catch (IOException | MyException e) {
            LOGGER.error("文件删除异常", e);
            flag = -2;
        }
        return flag;
    }

    /**
     * 查看远端文件信息
     *
     * @param groupName      组名
     * @param remoteFileName 完整文件名
     * @return 返回文件详细信息
     */
    public static FileInfo getFileInfo(String groupName, String remoteFileName) {
        FileInfo fileInfo = null;
        try {
            fileInfo = sc.get_file_info(groupName, remoteFileName);
        } catch (IOException | MyException e) {
            LOGGER.error("查看服务器文件信息异常", e);
        }
        return fileInfo;
    }

    /**
     * 获取文件路径
     *
     * @param port 端口号,多个端口号只生效第一个端口
     * @return 返回文件路径
     */
    public static String getTrackerUrl(Integer port) {
        if (port == null) {
            port = DEFAULT_PORT;
        }
        //获取ip地址
        return "http://" + ts.getInetSocketAddress().getHostString() + ":" + port + "/";
    }

    /**
     * 初始化Storage客户端
     */
    private static StorageClient createStorageClient() {
        if (sc == null) {
            if (ts == null) {
                ts = createTrackerServer();
            }
            if (ss == null) {
                ss = createStorageServer();
            }
            sc = new StorageClient(ts, ss);
        }
        return sc;
//        return new StorageClient(createTrackerServer(), null);
    }

    /**
     * 初始化Storage服务器端
     */
    public static StorageServer createStorageServer() {
        try {
            if (ss == null) {
                if (tc == null) {
                    tc = createTrackerClient();
                }
                if (ts == null) {
                    ts = createTrackerServer();
                }
                ss = tc.getStoreStorage(ts);
            }
            return ss;
        } catch (IOException | MyException e) {
            throw new FastDFSException("初始化StorageServer失败");
        }
    }

    /**
     * 初始化Tracker服务器端
     */
    private static TrackerServer createTrackerServer() {
        try {
            if (ts == null) {
                if (tc == null) {
                    tc = createTrackerClient();
                }
                ts = tc.getTrackerServer();
            }
            return ts;
        } catch (IOException e) {
            throw new FastDFSException("初始化TrackerServer失败");
        }
    }

    /**
     * 初始化Tracker客户端
     */
    private static TrackerClient createTrackerClient() {
        return new TrackerClient();
    }

    public static StorageClient getStorageClient() {
        if (sc == null) {
            sc = createStorageClient();
        }
        return sc;
    }

    public static StorageServer getStorageServer() {
        if (ss == null) {
            ss = createStorageServer();
        }
        return ss;
    }

    public static TrackerServer getTrackerServer() {
        if (ts == null) {
            ts = createTrackerServer();
        }
        return ts;
    }

    public static TrackerClient getTrackerClient() {
        if (tc == null) {
            tc = createTrackerClient();
        }
        return tc;
    }

    public static class UploadFile {
        private String filename;//文件名
        private byte[] content;//字节文件内容
        private String localPath;//本地文件路径名
        private String ext;//文件后缀名
        private String md5;//文件md5编码
        private String uploadDate;//上传日期
        private String author;//作者
        private String authVoucher;//凭证

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getUploadDate() {
            return uploadDate;
        }

        public void setUploadDate(String uploadDate) {
            this.uploadDate = uploadDate;
        }

        public String getAuthVoucher() {
            return authVoucher;
        }

        public void setAuthVoucher(String authVoucher) {
            this.authVoucher = authVoucher;
        }
    }

    public static void main(String[] args) throws IOException, MyException {
        StorageClient sc = FastDfsUtil.getStorageClient();
        String[] uploadResult = sc.upload_file("C:\\Users\\10034\\Desktop\\社招面经（1个月）\\零基础Java学习路线.md", "md", null);
        System.out.println(Arrays.toString(uploadResult));
        //sc.download_file("group1", "M00/00/00/rBkXRmB4o3SAPGbzAACREuAxHZs118.jpg", "C:\\Users\\10034\\Desktop\\a.jpg");
        //System.out.println(FastDfsUtils.delete("group1", "M00/00/00/rBkXRmB4qmWAI_zYAABXg-85_zg664.pdf"));
    }
}
