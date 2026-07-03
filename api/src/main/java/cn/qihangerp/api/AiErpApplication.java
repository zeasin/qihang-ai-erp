package cn.qihangerp.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.qihangerp"})
@MapperScan("cn.qihangerp.mapper")
@EnableScheduling
public class AiErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiErpApplication.class, args);
    }
}