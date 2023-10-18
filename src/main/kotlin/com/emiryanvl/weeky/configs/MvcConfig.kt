package com.emiryanvl.weeky.configs

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/*.html")
            .addResourceLocations("classpath:/templates/")
        registry.addResourceHandler("/assets/**")
            .addResourceLocations("classpath:/static/assets/")
        registry.addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/css/")
        registry.addResourceHandler("/img/**")
            .addResourceLocations("classpath:/static/img/")
        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
        registry.addResourceHandler("/scss/**")
            .addResourceLocations("classpath:/static/scss/")
    }
}