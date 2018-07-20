package com.hydeng.course;

import com.hydeng.course.filter.CourseFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-24
 */
@SpringBootApplication
public class CourseEdgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseEdgeServiceApplication.class,args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(CourseFilter courseFilter ) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(courseFilter);

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }
}
