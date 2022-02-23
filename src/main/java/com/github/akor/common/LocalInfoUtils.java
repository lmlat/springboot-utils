package com.github.akor.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/07/14 13:42
 * @Description : 获取本地信息工具类（ipv4,ipv6,mac,默认网关,DHCP等信息）
 * @Version : 1.0
 * 依赖包：无
 */
public class LocalInfoUtils {
    /**
     * 获取window网卡信息(ipconfig /all下的所有信息)
     */
    public static String getWindows(String keyword) throws IOException {
        String res = null;
        Process process = Runtime.getRuntime().exec("ipconfig /all");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        int index;
        keyword = keyword.toLowerCase();
        while ((line = bufferedReader.readLine()) != null) {
            if (line.toLowerCase().contains(keyword)) {
                index = line.indexOf(":");
                if (index != -1) {
                    res = line.substring(index + 1).trim();
                    if (keyword.startsWith("ip")) {
                        res = res.substring(0, res.length() - "(首选)".length() - 1);
                    }
                }
                break;
            }
        }
        return res;
    }

    /**
     * 获取本地mac地址
     */
    public static String getMac() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String mac;
        if (os.startsWith("windows")) {
            mac = getWindowsMacAddress();
        } else if (os.startsWith("linux")) {
            mac = getLinuxMacAddress();
        } else {
            mac = getUnixMacAddress();
        }
        return mac != null ? mac : "";
    }

    /**
     * 获取Unix、Linux网卡信息（ifconfig eth0下的所有信息）
     */
    private static String getLinuxAndUnix(String command, String keyword, int startIndex, String charsetName) throws IOException {
        String res = null;
        Process process = Runtime.getRuntime().exec(command);//执行指令
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), charsetName));//解析执行指令的流
        String line;
        int index;
        while ((line = br.readLine()) != null) {
            index = line.toLowerCase().indexOf(keyword);
            if (index != -1) {
                res = line.substring(index + startIndex).trim();
                break;
            }
        }
        br.close();
        return res;
    }

    /**
     * 获取Unix网卡的mac地址.
     */
    private static String getUnixMacAddress() throws IOException {
        return getLinuxAndUnix("ifconfig eth0", "hwaddr", 7, "GBK");
    }

    /**
     * 获取Linux网卡的mac地址.
     */
    private static String getLinuxMacAddress() throws IOException {
        String address = getLinuxAndUnix("ifconfig eth0", "硬件地址", 4, "GBK");
        if (address == null) {
            address = getUnixMacAddress();
        }
        return address;
    }

    /**
     * 获取widnows网卡的mac地址.
     */
    private static String getWindowsMacAddress() throws IOException {
        String res = null;
        //ipconfig /all显示信息中包含有mac地址信息
        Process process = Runtime.getRuntime().exec("ipconfig /all");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        int index;
        while ((line = br.readLine()) != null) {
            //物理地址. . . . . . . . . . . . . : E6-AA-EA-6A-35-1A
            //读取这行数据时，通过 - 分隔字符串,数组长度必定是6
            if (line.split("-").length == 6) {
                index = line.indexOf(":");
                if (index != -1) {
                    res = line.substring(index + 1).trim();
                }
                break;
            }
        }
        br.close();
        return res;
    }
    public static void main(String[] argc) throws IOException {
        System.out.println("mac: " + getMac());
        System.out.println("默认网关: " + getWindows("默认网关"));
        System.out.println("子网掩码: " + getWindows("子网掩码"));
        System.out.println("IPv4: " + getWindows("IPv4"));
        System.out.println("IPv6: " + getWindows("IPv6"));
    }
}