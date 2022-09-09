package com.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * SuperMercadoApplication controller class.
 * @author jesu_
 */
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = "com.middleware")
@SpringBootApplication
public class SuperMercadoApplication {
    /**
     * Main method.
     * Method to execute application.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SuperMercadoApplication.class, args);
    }

}
