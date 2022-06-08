package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.swagger;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.security.CurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH = "Bearer";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any()).build().apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(List.of(securityContext()))
                .ignoredParameterTypes(ApiIgnore.class, CurrentUser.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Product REST API", "Product API to perform CRUD opertations", "1.0", "Terms of service",
                new Contact("Admin", "www.admin.cz", "admin@admin.com"), "License of API", "API license URL", Collections.emptyList());
    }

    private List<SecurityScheme> securitySchemes() {
        return List.of(new ApiKey(BEARER_AUTH, "Authorization", "header"));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(List.of(bearerAuthReference()))
                .forPaths(PathSelectors.any()).build();
    }

    private SecurityReference bearerAuthReference() {
        return new SecurityReference(BEARER_AUTH, new AuthorizationScope[0]);
    }

}