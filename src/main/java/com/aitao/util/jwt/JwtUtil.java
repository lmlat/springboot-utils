package com.aitao.util.jwt;

import com.aitao.common.Checks;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Author : AiTao
 * Date : 2020/10/26
 * Time : 10:14
 * Information : token工具类
 */
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    //秘钥, 这里用的是阿里云的一个过期APP密钥
    private static final String SECRET_KEY = "t3r8FlIyCfISAOtcqxlBjueAW2LWBT";
    //token过期时间 1天
    private static long EXPIRE_TIME = 1000 * 60 * 60 * 24;

    /**
     * 生成的token串
     *
     * @param username 用户名
     * @return 返回生成的token串
     */
    public static String getToken(String username) {
        return getToken(username, null);
    }

    /**
     * 生成的token串
     *
     * @param username 用户名
     * @param claimMap 自定义属性列表
     * @return 返回生成的token串
     */
    public static String getToken(String username, Map<String, Object> claimMap) {
        if (claimMap != null && claimMap.containsKey("exp")) {
            Date exp = (Date) claimMap.get("exp");
            EXPIRE_TIME = exp.getTime();
        }
        JwtBuilder jwt = Jwts.builder();
        jwt
                //设置头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //设置签名算法和密钥
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                //设置用户名
                .setSubject(username)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                //发布时间
                .setIssuedAt(new Date());
        //自定义多个属性
        if (claimMap != null && !claimMap.isEmpty()) {
            for (Map.Entry<String, Object> claim : claimMap.entrySet()) {
                jwt.claim(claim.getKey(), claim.getValue());
            }
        }
        //生成token串
        return jwt.compact();
    }

    /**
     * 解析token串
     *
     * @param token token串
     * @return 返回解析后的token主体
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                //设置密钥
                .setSigningKey(SECRET_KEY)
                //解析token串
                //.parseClaimsJwt(token) //该方法会抛出 UnsupportedJwtException 异常
                .parseClaimsJws(token)
                //获取body部分
                .getBody();
    }

    /**
     * 判断token是否存在
     *
     * @param token token串
     * @return true存在, false不存在
     */
    public static boolean checkToken(String token) {
        if (Checks.isEmpty(token)) {
            return false;
        }
        try {
            return Objects.nonNull(parseToken(token));//通过密钥解析token串
        } catch (Exception e) {
            LOGGER.error("当前token串无效");
            return false;
        }
    }

    /**
     * 判断token是否存在（通过获取请求头信息来获取token串，再使用密钥解析token串）
     *
     * @param request 请求对象
     * @return true存在, false不存在
     */
    public static boolean checkToken(HttpServletRequest request) {
        return checkToken(request.getHeader("token")); //通过获取请求头信息获取token再使用密钥解析token
    }

    /**
     * 根据token串获取用户名信息
     *
     * @param token token串
     * @return 返回用户名
     */
    public static String getUsername(String token) {
        return getClaim(token, "sub");
    }

    /**
     * 根据token串获取属性列表
     *
     * @param token token串
     * @return 返回属性列表
     */
    public static List<Pair<String, Object>> getClaimsToList(String token) {
        if (Checks.isEmpty(token)) {
            return null;
        }
        List<Pair<String, Object>> list = new ArrayList<>();
        Claims claims = parseToken(token);
        if (claims == null) {
            LOGGER.warn("解析token串数据为空");
            return null;
        }
        for (Map.Entry<String, Object> claim : claims.entrySet()) {
            list.add(new Pair<>(claim.getKey(), claim.getValue()));
        }
        return list;
    }

    /**
     * 根据token串获取属性列表
     *
     * @param token token串
     * @return 返回自定义属性列表
     */
    public static Map<String, Object> getClaimsToMap(String token) {
        if (Checks.isEmpty(token)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Claims claims = parseToken(token);
        if (claims == null) {
            LOGGER.warn("解析token串数据为空");
            return null;
        }
        for (Map.Entry<String, Object> claim : claims.entrySet()) {
            map.put(claim.getKey(), claim.getValue());
        }
        return map;
    }

    /**
     * 根据token串获取指定的自定义属性
     *
     * @param token token串
     * @param key   键
     * @return 返回指定的自定义属性值
     */
    public static String getClaim(String token, String key) {
        return String.valueOf(parseToken(token).get(key));
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("permission", "admin");
        map.put("id", "44");
        map.put("role", "管理员");
        String token = getToken("aitao", map);
        System.out.println(token);
        System.out.println("用户名:" + getUsername(token));
        System.out.println("角色:" + getClaim(token, "role"));
        System.out.println(getClaimsToMap(token));
        System.out.println(getClaimsToList(token));
    }
}
