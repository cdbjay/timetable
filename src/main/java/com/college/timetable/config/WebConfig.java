package com.college.timetable.config;

import com.college.timetable.viewresolver.ExcelViewResolver;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.ArrayList;
import java.util.List;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .defaultContentType(MediaType.APPLICATION_JSON)
                .favorPathExtension(true);
    }

    /*
     * Configure ContentNegotiatingViewResolver
     */
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);

        // Define all possible view resolvers
        List<ViewResolver> resolvers = new ArrayList<>();

        resolvers.add(new ThymeleafViewResolver());
        resolvers.add(excelViewResolver());


        resolver.setViewResolvers(resolvers);
        return resolver;
    }

    /*
     * Configure View resolver to provide XLS output using Apache POI library to
     * generate XLS output for an object content
     */
    public ViewResolver excelViewResolver() {
        return new ExcelViewResolver();
    }

}