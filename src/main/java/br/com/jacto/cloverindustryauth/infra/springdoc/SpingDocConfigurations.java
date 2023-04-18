package br.com.jacto.cloverindustryauth.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpingDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Indústria Trevo S.A.")
                        .description("API REST da Indústria Trevo S.A., contendo soluções de segurança e " +
                                "escalabilidade, que possibilitam aos usuários fazerem login na aplicação de acordo " +
                                "com seu nível de acesso, além de possilbilitar o cadastro, visualização, alteração " +
                                "e exclusão dos produtos da empresa.")
                        .contact(new Contact()
                                .name("Filipe Evangelista Avila")
                                .email("filipeevan@outlook.com"))
                        .license(new License()
                                .name("Apache 2.0")));
    }
}
