package com.demo.urlShorterDemo.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry

import springfox.documentation.builders.ApiInfoBuilder

import springfox.documentation.service.ApiInfo
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableWebMvc
@EnableSwagger2
@Component
class SwaggerConfig: WebMvcConfigurer {


    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.demo.urlShorterDemo"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(getApiInfo())
    }

    private fun getApiInfo(): ApiInfo? {
        return ApiInfoBuilder()
            .title("Url Shortener APIs")
            .version("1.0.0")
            .build()
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }


}

