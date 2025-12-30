package com.springBoot.saravana_bhavan.CONFIG;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 
 @Configuration
 public class config implements WebMvcConfigurer {

     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {

         registry.addResourceHandler("/uploads/food/**")
                 .addResourceLocations("file:C:/uploads/food/");
     }
 }

