package com.aitao.common;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : AiTao
 * @Create : 2021-10-22 1:48
 * @Description : B站视频下载工具类
 */
public class BVideoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BVideoUtils.class);
    // b站通用视频连接
    private static final String B_VIDEO_URL = "https://www.bilibili.com/video/";
    // 获取所有视频信息地址
    private static String VIDEO_NAMES_URL;
    // 具体的视频接口 https://www.bilibili.com/video/BV14J4114768?p=1
    private static String VIDEO_URL;
    // ffmpeg 本地地址
    private static final String FFMPEG_PATH = "F:\\ffmpeg\\bin\\ffmpeg.exe";
    // user_agent
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";
    // 默认本地保存路径
    private static final String DEFAULT_SAVE_PATH = "C:/";
    // 本地保存路径
    private static String LOCAL_SAVE_DIRECTORY_PATH;
    // 视频对象
    private static final Video VIDEO = new Video();

    static class Video {
        private String name;//视频名称
        private JSONObject videoInfo;
        private String videoBaseUrl;
        private String audioBaseUrl;
        private String videoBaseRange;
        private String audioBaseRange;
        private String videoSize;
        private String audioSize;
    }

    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.print("请输入视频的保存地址:(默认：C://online_learn_sys/视频名):");
            String localSaveDirectoryPath = "C://online_learn_sys/" + in.next() + File.separator;
            System.out.print("请输入BVID值:(默认：BV14J4114768), 输入q退出下载:");
            String BVID = in.next();
            if (BVID.equalsIgnoreCase("q")) {
                break;
            }
            //提供B站视频id和本地保存路径,下载视频
            download(BVID, localSaveDirectoryPath);
        }
    }

    public static void download(String bVid) {
        download(bVid, DEFAULT_SAVE_PATH);
    }

    public static void download(String bVid, String localSaveDirectoryPath) {
        LOCAL_SAVE_DIRECTORY_PATH = localSaveDirectoryPath;
        try {
            System.out.println("BVID:" + bVid);
            if (LOCAL_SAVE_DIRECTORY_PATH == null || LOCAL_SAVE_DIRECTORY_PATH.isEmpty()) {
                LOCAL_SAVE_DIRECTORY_PATH = DEFAULT_SAVE_PATH;
            }
            //创建BVID编号文件
            File bvidFile = new File(LOCAL_SAVE_DIRECTORY_PATH + bVid);
            if (!bvidFile.getParentFile().exists()) {
                bvidFile.getParentFile().mkdirs();
                bvidFile.createNewFile();
            }
            VIDEO_NAMES_URL = String.format("https://api.bilibili.com/x/player/pagelist?bvid=%s&jsonp=jsonp", bVid);
            VIDEO_URL = B_VIDEO_URL + bVid;
            parseVideoList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析网页信息获取视频集合(解析多个视频的case)
     */
    private static void parseVideoList() throws IOException {
        long start = System.currentTimeMillis();
        // 获取全部cid => 视频名称
        List<Pair<Long, String>> videoNames = getAllVideoName();
        System.out.println(videoNames);
        LOGGER.info("下载文件数量:{}", videoNames.size());
        if (videoNames.size() == 1) {
            parseVideoOne();
            return;
        }
        for (int i = 0; i < videoNames.size(); i++) {
            //拼接分页 https://www.bilibili.com/video/BV14J4114768?p=1
            String url = VIDEO_URL + "?p=" + (i + 1);
            System.out.println("解析的url地址为:" + url);
            HttpResponse res = HttpRequest.get(url).timeout(2000).execute();
            String html = res.body();
            //视频名称
            System.out.println("当前视频名称为:" + videoNames.get(i).getValue());
            VIDEO.name = videoNames.get(i).getValue();
            //截取视频信息
            //Pattern pattern = Pattern.compile("(?<=<script>window.__INITIAL_STATE__=).*?(?=</script>)");
            Pattern pattern = Pattern.compile("(?<=<script>window.__playinfo__=).*?(?=</script>)");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                String content = matcher.group();
                VIDEO.videoInfo = JSON.parseObject(content.substring(0, content.indexOf(";(function()")));
            }
            System.out.println("视频信息:" + VIDEO.videoInfo);
            //getVideoInfo();
            //通过这个地址获取视频地址流，https://api.bilibili.com/x/player/playurl?avid=80149248&cid=137143602&qn=32
            getVideoStream(VIDEO.videoInfo.getLong("aid"), videoNames.get(i).getKey(), 32);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

    /**
     * 获取视频流，并下载
     *
     * @param aid aid === avid
     * @param cid cid
     * @param qn  视频清晰度，32标准
     * @throws FileNotFoundException
     */
    private static void getVideoStream(Long aid, Long cid, int qn) throws FileNotFoundException {
        String url = String.format("https://api.bilibili.com/x/player/playurl?avid=%s&cid=%s&qn=%d", String.valueOf(aid), String.valueOf(cid), qn);
        HttpResponse res = HttpRequest.get(url).timeout(2000).execute();
        JSONObject html = JSON.parseObject(res.body()).getJSONObject("data").getJSONArray("durl").getJSONObject(0);
        // 保存音视频的位置
        File fileDir = new File(LOCAL_SAVE_DIRECTORY_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        // 下载视频
        File videoFile = new File(LOCAL_SAVE_DIRECTORY_PATH + File.separator + VIDEO.name + "_video.mp4");
        if (!videoFile.exists()) {
            FileOutputStream fos = new FileOutputStream(videoFile);
            System.out.println("--------------开始下载视频文件--------------");
            HttpResponse videoRes = HttpRequest.get(html.getString("url"))
                    .header("Referer", "https://www.bilibili.com/")
                    .header("Range", "bytes=0-" + html.getLong("size"))
                    .header("User-Agent", USER_AGENT)
                    .execute();
            InputStream is = videoRes.bodyStream();
            IOUtil.FileHelper.copy(is, fos);
            System.out.println("--------------视频文件下载完成--------------");
        }
    }

    /**
     * 解析网页信息获取视频集合(解析单个视频的case)
     */
    private static void parseVideoOne() {
        HttpResponse res = HttpRequest.get(VIDEO_URL).timeout(2000).execute();
        String html = res.body();
        Document document = Jsoup.parse(html);
        Element title = document.getElementsByTag("title").first();
        //视频名称
        VIDEO.name = title.text();
        //截取视频信息
        Pattern pattern = Pattern.compile("(?<=<script>window.__playinfo__=).*?(?=</script>)");
        Matcher matcher = pattern.matcher(html);
        if (!matcher.find()) {
            LOGGER.warn("未匹配到视频信息，退出程序！");
            return;
        }
        VIDEO.videoInfo = JSON.parseObject(matcher.group());
        getVideoInfo();
    }

    /**
     * 获取全部视频名称集合
     *
     * @return 返回视频名称集合
     */
    private static List<Pair<Long, String>> getAllVideoName() {
        //解析地址 "https://api.bilibili.com/x/player/pagelist?bvid=BV14J4114768&jsonp=jsonp"
        HttpResponse res = HttpRequest.get(VIDEO_NAMES_URL).timeout(3000).execute();
        //获取请求体
        String body = res.body();
        //System.out.println(jsonBody);
        //转换成JSON对象
        JSONObject jsonObject = JSON.parseObject(body);
        //获取data键的值
        JSONArray pages = jsonObject.getJSONArray("data");
        List<Pair<Long, String>> list = new ArrayList<>();
        JSONObject object = null;
        for (int i = 0; i < pages.size(); i++) {
            object = pages.getJSONObject(i);
            //解析data键中所有part键，也就是所有的视频名称
            list.add(new Pair<>(object.getLong("cid"), object.getString("part")));
        }
        return list;
    }

    /**
     * 解析视频和音频的具体信息
     */
    private static void getVideoInfo() {
        // 获取视频的基本信息
        JSONObject videoInfo = VIDEO.videoInfo;
        JSONArray videoInfoArr = videoInfo.getJSONObject("data").getJSONObject("dash").getJSONArray("video");
        VIDEO.videoBaseUrl = videoInfoArr.getJSONObject(0).getString("baseUrl");
        VIDEO.videoBaseRange = videoInfoArr.getJSONObject(0).getJSONObject("SegmentBase").getString("Initialization");
        HttpResponse videoRes = HttpRequest.get(VIDEO.videoBaseUrl)
                .header("Referer", VIDEO_URL)
                .header("Range", "bytes=" + VIDEO.videoBaseRange)
                .header("User-Agent", USER_AGENT)
                .timeout(2000)
                .execute();
        VIDEO.videoSize = videoRes.header("Content-Range").split("/")[1];
        // 获取音频基本信息
        JSONArray audioInfoArr = videoInfo.getJSONObject("data").getJSONObject("dash").getJSONArray("audio");
        VIDEO.audioBaseUrl = audioInfoArr.getJSONObject(0).getString("baseUrl");
        VIDEO.audioBaseRange = audioInfoArr.getJSONObject(0).getJSONObject("SegmentBase").getString("Initialization");
        HttpResponse audioRes = HttpRequest.get(VIDEO.audioBaseUrl)
                .header("Referer", VIDEO_URL)
                .header("Range", "bytes=" + VIDEO.audioBaseRange)
                .header("User-Agent", USER_AGENT)
                .timeout(2000)
                .execute();
        VIDEO.audioSize = audioRes.header("Content-Range").split("/")[1];
        downloadAudioFile();
    }

    /**
     * 下载音视频
     */
    private static void downloadAudioFile() {
        // 保存音视频的位置
        File fileDir = new File(LOCAL_SAVE_DIRECTORY_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        // 下载视频
        File videoFile = new File(LOCAL_SAVE_DIRECTORY_PATH + File.separator + VIDEO.name + "_video.mp4");
        if (!videoFile.exists()) {
            System.out.println("--------------开始下载视频文件--------------");
            HttpResponse videoRes = HttpRequest.get(VIDEO.videoBaseUrl)
                    .header("Referer", VIDEO_URL)
                    .header("Range", "bytes=0-" + VIDEO.videoSize)
                    .header("User-Agent", USER_AGENT)
                    .execute();
            videoRes.writeBody(videoFile);
            System.out.println("--------------视频文件下载完成--------------");
        }
        // 下载音频
        File audioFile = new File(LOCAL_SAVE_DIRECTORY_PATH + File.separator + VIDEO.name + "_audio.mp4");
        if (!audioFile.exists()) {
            System.out.println("--------------开始下载音频文件--------------");
            HttpResponse audioRes = HttpRequest.get(VIDEO.audioBaseUrl)
                    .header("Referer", VIDEO_URL)
                    .header("Range", "bytes=0-" + VIDEO.audioSize)
                    .header("User-Agent", USER_AGENT)
                    .execute();
            audioRes.writeBody(audioFile);
            System.out.println("--------------音频文件下载完成--------------");
        }
        //合并音视频
        mergeAudioVideoFile(videoFile, audioFile);
    }


    /**
     * 合并音视频
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件
     */
    private static void mergeAudioVideoFile(File videoFile, File audioFile) {
        System.out.println("--------------开始合并音视频--------------");
        String outFile = LOCAL_SAVE_DIRECTORY_PATH + File.separator + VIDEO.name + ".mp4";
        List<String> commend = new ArrayList<>();
        commend.add(FFMPEG_PATH);
        commend.add("-i");
        commend.add(videoFile.getAbsolutePath());
        commend.add("-i");
        commend.add(audioFile.getAbsolutePath());
        commend.add("-vcodec");
        commend.add("copy");
        commend.add("-acodec");
        commend.add("copy");
        commend.add(outFile);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commend);
        try {
            builder.inheritIO().start().waitFor();
            System.out.println("--------------音视频合并完成--------------");
            //删除零时文件
            videoFile.delete();
            audioFile.delete();
        } catch (InterruptedException | IOException e) {
            System.err.println("音视频合并失败！");
            e.printStackTrace();
        }
    }
}
