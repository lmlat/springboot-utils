package com.github.akor.util.http;

import com.github.akor.common.Checks;
import com.github.akor.common.DateUtils;
import com.github.akor.common.IOUtil;
import com.github.akor.enums.BSearchType;
import com.github.akor.enums.NowCoderReviewType;
import com.github.akor.enums.SiteType;
import com.github.akor.model.NowCoderInterviewQuestion;
import com.github.akor.util.PinyinUtils;
import com.github.akor.util.http.sync.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : AiTao
 * @Create : 2021-12-08 20:00
 * @Description : 爬取数据工具类
 */
public class ReptileUtils {
    public static void main(String[] args) throws IOException, InterruptedException {
        downloadImage("风景", 3, SiteType.QIANKUTU);
        /*parseFanJu("柯南", BSearchType.B_STATION_BANG_UMI);
        Document document = Jsoup.parse(new URL("https://www.nowcoder.com/ta/review-test?page=1"), 30000);
        System.out.println(document);
        parseNowCoder(21);*/
    }

    private static final String QIANKUWANG_IMG_CLASSNAME = ".lazy";

    private static final String B_SEARCH_PAGE_URL = "https://search.bilibili.com/%s?keyword=%s&from_source=web_search";

    private static final String QIANKUTU_PAGE_URL = "https://588ku.com/beijing/%s-0-default-0-8-0-0-0-0-%d";
    // 牛客面试题库
    private static final String NOW_CODE_REVIEW_PAGE_URL = "https://www.nowcoder.com/ta/%s/review?page=%d";

    private static final String NOW_CODE_PAGE_URL = "https://www.nowcoder.com/ta/%s";
    // 解析页面配置超时时间
    private static final int PARSE_DEFAULT_TIMEOUT = 30000;

    /**
     * 抓取千库网图片
     *
     * @param siteType 网站类型
     * @param keyword  图片关键字
     * @param pageSize 分页大小
     */
    public static void downloadImage(String keyword, Integer pageSize, SiteType siteType) throws IOException {
        String newKeyword = keyword;
        if (Checks.contain("[\\u2E80-\\u9FFF]", keyword)) {
            newKeyword = PinyinUtils.getPinyin(keyword);
        }
        for (int page = 1; page <= pageSize; page++) {
            System.out.println("=====================第" + page + "页数据=========================");
            //String body = Jsoup.connect(String.format(QIANKUTU_PAGE_URL, newKeyword, page)).execute().body();
            String body = HttpUtils.get(String.format(QIANKUTU_PAGE_URL, newKeyword, page));
            Document document = Jsoup.parse(body);
            Elements imageList = document.select(QIANKUWANG_IMG_CLASSNAME);
            for (Element img : imageList) {
                String imgUrl = img.attr("data-original");
                if (imgUrl.startsWith("//bpic.588ku.com")) {
                    System.out.println("https:" + imgUrl);
                    int endIndex = imgUrl.indexOf("!");
                    int startIndex = imgUrl.lastIndexOf("/", endIndex);
                    String filename = imgUrl.substring(startIndex, endIndex);
                    IOUtil.FileHelper.download("https:" + imgUrl, "C:/" + siteType.getDesc() + File.separator + keyword + File.separator + page + File.separator, filename);
                }
            }
        }
    }

    /**
     * 解析B站番剧
     *
     * @param keyword 搜索番剧标题
     */
    public static void parseFanJu(String keyword, BSearchType bSearchType) throws IOException {
        String encode = URLEncoder.encode(keyword, "UTF-8");
        String url = String.format(B_SEARCH_PAGE_URL, bSearchType.getValue(), encode);// 搜索地址
        Document document = Jsoup.parse(new URL(url), PARSE_DEFAULT_TIMEOUT);
        Elements elements = document.getElementsByClass(bSearchType.getClassName());
        System.out.println(elements.size());
        for (Element element : elements) {
            Elements link = element.select(".left-img");
            System.out.println(link.attr("href").split("//")[1]);
            System.out.println(element.getElementsByClass("title").attr("title"));
            System.out.println(element.getElementsByClass("desc").text());
        }
    }

    /**
     * 解析牛客面试题
     *
     * @param pageSize 页数
     * @throws IOException
     * @throws InterruptedException
     */
    public static void parseNowCoder(Integer pageSize) throws IOException, InterruptedException {
        List<NowCoderInterviewQuestion> interviewQuestionList = new ArrayList<>();
        for (int j = 1; j <= pageSize; j++) {
            String url = String.format(NOW_CODE_PAGE_URL, NowCoderReviewType.TEST.getKeyword());
            Document doc = Jsoup.parse(new URL(url), NowCoderReviewType.TEST.getParseInterval());
            for (int i = 1; i <= 10; i++) {
                if (i % 5 == 0) {
                    Thread.sleep(1000);
                }
                String url2 = String.format(NOW_CODE_REVIEW_PAGE_URL, NowCoderReviewType.TEST.getKeyword(), i);
                Document document = Jsoup.parse(new URL(url2), PARSE_DEFAULT_TIMEOUT);
                NowCoderInterviewQuestion interviewQuestion = new NowCoderInterviewQuestion();
                interviewQuestion.setId(document.getElementsByClass("final-order").first().text());
                interviewQuestion.setSpecialColumn(document.select(".back-to-home a").eq(1).text());
                interviewQuestion.setAnswer(document.getElementsByClass("design-answer-box").first().text());
                interviewQuestion.setProblem(document.getElementsByClass("final-question").first().text());
                interviewQuestion.setType(document.select(".final-model-title h2 span").first().text());
                interviewQuestion.setCreateTime(DateUtils.toString(new Date()));
                interviewQuestion.setUpdateTime(DateUtils.toString(new Date()));
                interviewQuestionList.add(interviewQuestion);
            }
        }
        for (NowCoderInterviewQuestion question : interviewQuestionList) {
            System.out.println(question.getId());
            System.out.println(question.getSpecialColumn());
            System.out.println(question.getProblem());
            System.out.println(question.getAnswer());
            System.out.println(question.getType());
        }
    }
}
