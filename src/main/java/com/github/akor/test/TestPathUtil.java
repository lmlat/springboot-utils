package com.github.akor.test;

import com.github.akor.common.PathUtils;

import java.io.IOException;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/28 11:22
 * @Motto : 投入得越多，就能得到越多得价值
 * @Description :
 */
public class TestPathUtil {
    public static void main(String[] args) throws IOException {
        System.out.println(PathUtils.getUserHomePath());
        System.out.println(PathUtils.getClassPath("test.txt"));//结果：C:/Users/test/Desktop/Learn/springboot-utils/target/classes/test.txt
        System.out.println(PathUtils.getClassPath());//结果：C:/Users/test/Desktop/Learn/springboot-utils/target/classes/
        System.out.println(PathUtils.getProjectPath());//结果：C:/Users/test/Desktop/Learn/springboot-utils
        System.out.println(PathUtils.getTempPath());//结果：C:/Users/test/AppData/Local/Temp/
        System.out.println(PathUtils.getUserHomePath());//结果：C:/Users/test
        System.out.println(PathUtils.getClassPath(TestPathUtil.class));//结果：/C:/Users/test/Desktop/Learn/springboot-utils/target/classes/com/aitao/springbootutils/test/
    }
}
