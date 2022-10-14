package com.qimeixun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenshouyang
 * @date 2020/5/2015:21
 */

@SpringBootApplication
@MapperScan("com.qimeixun.mapper")
public class MobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileApplication.class, args);
    }

}
