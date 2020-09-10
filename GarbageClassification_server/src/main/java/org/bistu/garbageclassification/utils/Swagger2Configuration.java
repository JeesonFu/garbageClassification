package org.bistu.garbageclassification.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	
	//api接口包扫描路径，接口文档版本
	public static final String SWAGGER_SCAN_BASE_PACKAGE = "org.bistu.garbageclassification.controller";
	public static final String VERSION = "1.0";
	
    /**
     * 通过 createRestApi函数来构建一个DocketBean
     * 函数名,可以随意命名
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//调用apiInfo方法,创建一个ApiInfo实例，,里面是展示在文档页面信息内容
                //初始化API选择构造器
                .select()
                //API接口所在的包名下扫描controller
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                //路径筛选(any总为true)
                .paths(PathSelectors.any())
                .build();
    }
    
    //构建api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot Swagger2 构建RESTful API")
                //License地址
                .termsOfServiceUrl("无")
                //API文档版本
                .version(VERSION)
                //描述
                .description("API 接口文档描述")
                .build();
    }
}

//http://localhost:8080/swagger-ui.html