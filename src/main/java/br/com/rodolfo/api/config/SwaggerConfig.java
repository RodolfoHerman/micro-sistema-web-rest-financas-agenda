package br.com.rodolfo.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 */
@Configuration
@Profile("dev")
@EnableSwagger2
public class SwaggerConfig {

    private static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    public Docket api() {
        
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("br.com.rodolfo.api.controllers"))
                .paths(PathSelectors.any()).build()
                .apiInfo(this.apiInfo());
    }

	private ApiInfo apiInfo() {
        
        return new ApiInfoBuilder().title("Finaças API")
                .description("Documentação da API de acesso aos endpoints das Finanças")
                .version("1.0")
                .build();
	}

}