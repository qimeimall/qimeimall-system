package com.qimeixun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.qimeixun.mapper")
//@ComponentScan("com.qimallxun")
public class QimeimallSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(QimeimallSystemApplication.class, args);
    }

}
