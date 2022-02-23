package com.github.akor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/*忽略security校验登录页*/
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan(basePackages = {"com.aitao.poi"})
public class SpringbootUtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootUtilsApplication.class, args);
    }

}
