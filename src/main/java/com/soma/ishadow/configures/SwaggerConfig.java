package com.soma.ishadow.configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        ParameterBuilder ParameterBuilder = new ParameterBuilder();
        ParameterBuilder.name("ACCESS-TOKEN") //헤더 이름
                .description("ACCESS-TOKEN") //설명
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        List<Parameter> parameterList=new ArrayList<>();
        parameterList.add(ParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameterList)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ishadow"))
                .paths(PathSelectors.ant("/api/**"))
                .build();
                //.apiInfo(apiInfo());
    }

//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                "Team-Edit.",
//                "Edit. 앱 REST API",
//                "version 1.0",
//                "https://naver.com",
//                new Contact("Contact Me", "https://daum.net", "tigger@tigger.com"),
//                "Edit Licenses",
//                "https://naver.com",
//                new ArrayList<>()
//        );
//    }
}
