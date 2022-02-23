package com.aitao.alibaba;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.BucketReferer;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Author : AiTao
 * Date : 2020/11/5
 * Time : 9:51
 * Information : 阿里对象OSS工具类
 */
@Component
public class OssUtil {
    //日志对象
    private static final Logger logger = LoggerFactory.getLogger(OssUtil.class);
    // endpoint是访问OSS的访问域名
    private static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    //连接keyId
    private static final String ACCESS_KEY_ID = "LTAI5tLH7iBQYK7RPvLanuuf";
    //连接密钥
    private static final String ACCESS_KEY_SECRET = "t3r8FlIyCfISAOtcqxlBjueAW2LWBT";
    //需要存储的bucketName
    private static final String BUCKET_NAME = "lml-bucket";
    //文件夹名称
    private static final String FOLDER_NAME = "";
    //实例化OSSClient对象
    private static final OSS ossClient = getOSSClient();

    /**
     * 获取OSS客户端对象
     *
     * @return 返回OSSClient
     */
    private static OSS getOSSClient() {
        return new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 创建存储空间
     *
     * @param bucketName 存储空间名
     * @return 返回创建成功后的存储空间
     */
    private String createBucket(String bucketName) {
        //创建存储空间
        Bucket bucket = ossClient.createBucket(bucketName);
        logger.info(bucketName + "存储空间创建成功");
        return bucket.getName();
    }

    /**
     * 删除存储空间
     * 注意：
     * (1)删除存储空间之前，必须先删除存储空间下的所有文件、LiveChannel和分片上传产生的碎片。
     * (2)要删除分片上传产生的碎片，首先使用Bucket.ListMultipartUploads列举出所有碎片，然后使用
     * Bucket.AbortMultipartUpload删除这些碎片。
     *
     * @param bucketName 存储空间名
     * @return true删除成功, false删除失败
     */
    private boolean deleteBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
        logger.info(bucketName + "存储空间已删除");
        return !doesBucketExist();
    }

    /**
     * 判断存储空间Bucket是否存在
     * doesBucketExist(String bucketName)
     *
     * @return true存在，false不存在
     */
    public static boolean doesBucketExist() {
        //若Bucket不存在，则创建一个;否则啥也不做
        if (ossClient.doesBucketExist(BUCKET_NAME)) {
            logger.info("Bucket已创建，名称为：" + BUCKET_NAME);
            return true;
        } else {
            logger.info("Bucket不存在，已经创建Bucket，Bucket名称为：" + BUCKET_NAME);
            ossClient.createBucket(BUCKET_NAME);
            return false;
        }
    }

    /**
     * 关闭OSSClient对象
     */
    public static void close() {
        ossClient.shutdown();
    }

    /**
     * 获取指定存储空间的信息
     */
    public static String getBucketInfo() {
        StringBuilder resultInfo = new StringBuilder();
        //获取存储信息对象
        BucketInfo bucketInfo = ossClient.getBucketInfo(BUCKET_NAME);
        resultInfo.append("地域:" + bucketInfo.getBucket().getLocation() + "\n");
        resultInfo.append("创建日期:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bucketInfo.getBucket().getCreationDate()) + "\n");
        resultInfo.append("拥有者:" + bucketInfo.getBucket().getOwner() + "\n");
        resultInfo.append("存储空间名:" + bucketInfo.getBucket().getName() + "\n");
        return resultInfo.toString();
    }

    /**
     * 配置防盗链
     */
    public static void setReferer() {
        List<String> refererList = new ArrayList<String>();
        // 添加Referer白名单。Referer参数支持通配符星号（*）和问号（？）。
        refererList.add("http://www.aliyun-lam.cn");
        refererList.add("http://www.*.com");
        refererList.add("http://www.?.aliyuncs.com");
        // 设置存储空间Referer列表。设为true表示Referer字段允许为空。
        BucketReferer br = new BucketReferer(true, refererList);
        ossClient.setBucketReferer(BUCKET_NAME, br);
    }

    /**
     * 获取Bucket存储空间Referer的白名单列表
     */
    public static void listReferer() {
        BucketReferer br = ossClient.getBucketReferer(BUCKET_NAME);
        List<String> refererList = br.getRefererList();
        for (String referer : refererList) {
            System.out.println(referer);
        }
    }

    /**
     * 清空防盗链
     */
    public static void clearReferer() {
        //防盗链不能直接清空，需要新建一个允许空Referer的规则来覆盖之前的规则。
        BucketReferer br = new BucketReferer();
        ossClient.setBucketReferer(BUCKET_NAME, br);
    }

    /**
     * 获取OSS指定文件的URL访问地址
     *
     * @return 返回URL地址串
     */
    public static String url(String fileName) {
        //http://                  oss-cn-beijing.aliyuncs.com
        //https://lml-bucket.      oss-cn-beijing.aliyuncs.com
        return (ENDPOINT.replaceFirst("http://", "https://" + BUCKET_NAME + ".")) + "/" + fileName;
    }

    /**
     * 获取指定文件下的所有文件访问路径
     *
     * @param folder 文件夹名
     * @return 返回指定文件夹下的所有文件的访问路径地址
     */
    public static List<String> getFilesUrl(String folder) {
        List<String> result = new ArrayList<>();
        folder = folder.concat("/");
        //列举您存储空间（Bucket）中的文件（Object）
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BUCKET_NAME);
        listObjectsRequest.setPrefix(folder);//指定返回文件的Key必须以Prefix作为前缀。指定下一级文件
        listObjectsRequest.setMarker("");//获取下一页的起始点。设定从Marker之后按字母排序开始返回Object
        listObjectsRequest.setMaxKeys(100);//设置分页的页容量,默认值为100
        listObjectsRequest.setDelimiter("/");//使用它时 Prefix文件路径要以“/”结尾。跳出递归循环，只去指定目录下的文件。
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (OSSObjectSummary objectSummary : objectSummaries) {
            //忽略文件夹目录名 "folder/"
            if (!objectSummary.getKey().equals(folder)) {
                result.add(url(objectSummary.getKey()));
            }
        }
        return result;
    }

    /**
     * 获取指定文件下的指定文件的访问路径
     *
     * @param folder   文件夹名
     * @param fileName 文件名
     * @return 返回指定文件夹下的指定文件的访问路径地址
     */
    public static String getFileUrl(String folder, String fileName) {
        folder = folder.concat("/");
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BUCKET_NAME);
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setPrefix(folder);
        listObjectsRequest.setMarker("");
        listObjectsRequest.setMaxKeys(100);
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        return url(objectListing.getObjectSummaries().get(1).getKey());
    }

    /**
     * 上传文件（单文件）
     *
     * @param folder   文件目录名
     * @param fileName 文件名
     * @param is       文件流对象
     * @return 返回上传成功后的访问路径
     */
    public static String uploadFile(String folder, String fileName, InputStream is) {
        String allPath = folder.concat("/").concat(fileName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, allPath, is);
        ossClient.putObject(putObjectRequest);
//        close();
        System.out.println("Object：" + allPath + "存入OSS成功。");
        return url(allPath);
    }


    /**
     * 删除指定文件
     *
     * @param fileName
     */
    public static void deleteFile(String fileName) {
        ossClient.deleteObject(BUCKET_NAME, fileName);
        close();
    }


    /**
     * 流式下载
     *
     * @param objectName 表示从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如"qrcode/aitao.txt"
     */
    public static void downloadFile(String objectName) {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(BUCKET_NAME, objectName);
        // 读取文件内容。
        System.out.println("文件内容如下:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println("\n" + line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 列表指定存储空间下所有文件夹下的所有文件
     *
     * @return 返回所有文件的访问URL地址
     */
    public static List<String> listBucketFiles() {
        List<String> urls = new ArrayList<>();
        ObjectListing objectListing = ossClient.listObjects(BUCKET_NAME);
        List<OSSObjectSummary> objectSummary = objectListing.getObjectSummaries();
        System.out.println("您有以下Object：");
        for (OSSObjectSummary object : objectSummary) {
            urls.add(url(object.getKey()));
        }
        System.out.println(urls);
        return urls;
    }

    public static void main(String[] args) throws FileNotFoundException {
//        System.out.println(OssUtils.listBucketFiles());
//        System.out.println(getFilesUrl("avatar"));
//        System.out.println(getFileUrl("avatar","1.jpg"));

/*        List<Bucket> buckets = OssUtils.ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }*/
//        deleteFile("my-first-key");
        System.out.println(uploadFile("qrcode", "utils/common.util.js", new FileInputStream(new File("C:\\Users\\10034\\Desktop\\Learn\\springboot-utils\\src\\main\\resources\\common.util.js"))));
        //downloadFile("qrcode/tg98589.png");
        //System.out.println(downloadLocalFile("qrcode/tg98589.png", "tg98589.png"));
        //listBucketFiles();
//        clearReferer();
    }
}
