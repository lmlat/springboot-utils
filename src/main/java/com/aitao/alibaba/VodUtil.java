package com.aitao.alibaba;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-10-21 20:19
 * @Description : 视频点播工具类
 */
public class VodUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(VodUtil.class);
    //点播服务接入区域
    private static final String REGION_ID = "cn-beijing";
    //    private static final String REGION_ID = "cn-beijing";
    private static final String ACCESS_KEY_ID = "LTAI5tLH7iBQYK7RPvLanuuf";
    private static final String ACCESS_SECRET = "t3r8FlIyCfISAOtcqxlBjueAW2LWBT";
    private static final DefaultAcsClient CLIENT;

    static {
        CLIENT = build();
    }

    /**
     * 初始化构建视频点播客户端实例对象
     */
    public static DefaultAcsClient build() {
        DefaultProfile defaultProfile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID, ACCESS_SECRET);
        //创建视频点播客户端实例
        return new DefaultAcsClient(defaultProfile);
    }

    /**
     * 获取播放视频响应信息
     */
    public static GetPlayInfoResponse getPlayInfoResponse(String id) {
        LOGGER.info("VodUtil_getPlayInfoResponse_execute, param:{}", id);
        GetPlayInfoResponse playInfoResponse = null;
        try {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(id);
            playInfoResponse = CLIENT.getAcsResponse(request);
        } catch (ClientException e) {
            LOGGER.error("获取播放响应信息异常, VodUtil_getPlayInfoResponse_error", e);
        }
        return playInfoResponse;
    }

    /**
     * 获取视频播放认证响应信息
     */
    public static GetVideoPlayAuthResponse getVideoPlayAuthResponse(String id) {
        LOGGER.info("VodUtil_getVideoPlayAuthResponse_execute, param:{}", id);
        GetVideoPlayAuthResponse authResponse = null;
        try {
            GetVideoPlayAuthRequest authRequest = new GetVideoPlayAuthRequest();
            authRequest.setVideoId(id);
            authResponse = CLIENT.getAcsResponse(authRequest);
        } catch (ClientException e) {
            LOGGER.error("获取视频播放认证响应信息异常, VodUtil_getPlayInfoResponse_error", e);
        }
        return authResponse;
    }

    /**
     * 根据id获取视频基本信息
     *
     * @param id 视频id
     * @return 返回GetPlayInfoResponse.VideoBase对象
     */
    public static GetPlayInfoResponse.VideoBase getVideoBase(String id) {
        GetPlayInfoResponse response = getPlayInfoResponse(id);
        if (response == null || response.getVideoBase() == null) {
            LOGGER.warn("获取视频基本信息为空");
            return null;
        }
        return response.getVideoBase();
    }

    /**
     * 根据id获取视频播放信息
     *
     * @param id 视频id
     * @return 返回GetPlayInfoResponse.VideoBase对象
     */
    public static GetPlayInfoResponse.PlayInfo getPlayInfo(String id) {
        GetPlayInfoResponse response = getPlayInfoResponse(id);
        if (response == null || response.getPlayInfoList() == null || response.getPlayInfoList().isEmpty()) {
            LOGGER.warn("获取视频播放信息为空");
            return null;
        }
        return response.getPlayInfoList().get(0);
    }

    /**
     * 根据id获取视频点播访问地址
     *
     * @param id 视频ID
     * @return 播放地址
     */
    public static String getPlayUrlById(String id) {
        GetPlayInfoResponse.PlayInfo playInfo = getPlayInfo(id);
        if (playInfo == null) {
            LOGGER.warn("视频播放信息为空, id:{}", id);
            return null;
        }
        return playInfo.getPlayURL();
    }

    /**
     * 根据id获取视频凭证
     *
     * @param id 视频ID
     * @return 返回视频凭证
     */
    public static String getVoucherById(String id) {
        GetVideoPlayAuthResponse authResponse = getVideoPlayAuthResponse(id);
        if (authResponse == null || authResponse.getPlayAuth() == null) {
            LOGGER.warn("获取视频凭证失败");
            return null;
        }
        return authResponse.getPlayAuth();
    }

    public static CreateUploadVideoResponse getCreateUploadVideoResponse(String title, String filename) throws ClientException {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(title);
        request.setFileName(filename);
        return CLIENT.getAcsResponse(request);
    }

    /**
     * 本地上传
     *
     * @param title          标题
     * @param fileUploadPath 上传文件路径
     * @return 响应参数
     */
    public static Map<String, Object> uploadLocal(String title, String fileUploadPath) {
        UploadVideoRequest request = new UploadVideoRequest(ACCESS_KEY_ID, ACCESS_SECRET, title, fileUploadPath);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        request.setStorageLocation("outin-eabb4b1b327811ec82dd00163e0eb78b.oss-cn-beijing.aliyuncs.com");
        request.setApiRegionId(REGION_ID);
        /* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
        注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启*/
        //request.setEnableCheckpoint(false);
        /* OSS慢请求日志打印超时时间，是指每个分片上传时间超过该阈值时会打印debug日志，如果想屏蔽此日志，请调整该阈值。单位: 毫秒，默认为300000毫秒*/
        //request.setSlowRequestsThreshold(300000L);
        /* 可指定每个分片慢请求时打印日志的时间阈值，默认为300s*/
        //request.setSlowRequestsThreshold(300000L);
        /* 是否显示水印(可选)，指定模板组ID时，根据模板组配置确定是否显示水印*/
        //request.setIsShowWaterMark(true);
        /* 自定义消息回调设置(可选)，参数说明请参见请求参数说明。*/
        // request.setUserData("{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackURL\":\"http://test.test.com\"}}");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 工作流ID(可选) */
        //request.setWorkflowId("d4430d07361f0*be1339577859b0177b");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
        //request.setPrintProgress(false);
        /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
        //request.setProgressListener(new PutObjectProgressListener());
        /* 设置您实现的生成STS信息的接口实现类*/
        // request.setVoDRefreshSTSTokenListener(new RefreshSTSTokenImpl());
        /* 设置应用ID*/
        //request.setAppId("app-1000000");
        /* 点播服务接入点 */
        //request.setApiRegionId("cn-shanghai");
        /* ECS部署区域*/
        // request.setEcsRegionId("cn-shanghai");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        Map<String, Object> params = new HashMap<>();
        params.put("requestId", response.getRequestId());
        if (response.isSuccess()) {
            params.put("videoId", response.getVideoId());
        } else {
            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            params.put("videoId", response.getVideoId());
            params.put("errorCode", response.getCode());
            params.put("errorMessage", response.getMessage());
        }
        return params;
    }

    public static void main(String[] args) throws ClientException {
//        Map<String, Object> uploadVideo = uploadLocal("1.mp4", "C:/1.mp4");
//        System.out.println(uploadVideo);
        System.out.println(getPlayUrlById("d7e4ea260ca1496eac0ed50a8ab5d6ae"));
    }
}
