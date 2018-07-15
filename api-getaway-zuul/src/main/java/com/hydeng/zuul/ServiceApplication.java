package com.hydeng.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-24
 */
@SpringBootApplication
@EnableZuulProxy
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class,args);
    }
}
