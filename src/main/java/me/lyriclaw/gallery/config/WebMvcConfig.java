package me.lyriclaw.gallery.config;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@EnableSpringDataWebSupport
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private Environment environment;

    @Value("${project.front-end.dist-path}")
    private Path frontEndDistPath;

    @Bean
    @Profile("!production")
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(Lists.newArrayList(
                        new ParameterBuilder()
                                .name("TOKEN")
                                .description("Auth header for private APIs")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build(),
                        new ParameterBuilder()
                                .name("page")
                                .description("Page parameter for page queries")
                                .modelRef(new ModelRef("string"))
                                .parameterType("query")
                                .defaultValue("0")
                                .required(false)
                                .build(),
                        new ParameterBuilder()
                                .name("size")
                                .description("Size parameter for page queries")
                                .modelRef(new ModelRef("string"))
                                .parameterType("query")
                                .defaultValue("10")
                                .required(false)
                                .build()
                        )
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage("me.lyriclaw.gallery.controller"))
                .paths(regex("/api(/public|/private).*"))
                .build();
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Lyrical Gallery Backend REST API")
                .description("\"Spring Boot REST API for Lyrical Gallery\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("Lyric Law", "https://lyricseraph.github.io", "lyriclaw@lyriclaw.me"))
                .build();
    }

    @Override
    protected void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        boolean isProduction = Arrays.asList(environment.getActiveProfiles()).contains("production");
        if (!isProduction) {
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        registry.addResourceHandler("/static/dist/**")
                .addResourceLocations("file:" + frontEndDistPath.toString() + "/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
        argumentResolvers.add(new SortHandlerMethodArgumentResolver());
    }
}