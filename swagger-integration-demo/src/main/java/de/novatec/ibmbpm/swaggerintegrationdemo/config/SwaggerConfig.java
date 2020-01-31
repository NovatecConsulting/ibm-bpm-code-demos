/*
package de.novatec.ibmbpm.swaggerintegrationdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private String basePackageName = "de.novatec.ibmbpm.swaggerintegrationdemo.services";

    @Bean
    public Docket chucknorrisApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackageName))
                .paths(regex("/chucknorris.*"))
                .build()
                .groupName("chucknorris");
    }

    @Bean
    public Docket donaldtrumpApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackageName))
                .paths(regex("/donaldtrump.*"))
                .build()
                .groupName("donaldtrump");
    }

    @Bean
    public Docket memeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackageName))
                .paths(regex("/meme.*"))
                .build()
                .groupName("memegenerator");
    }
}
*/
