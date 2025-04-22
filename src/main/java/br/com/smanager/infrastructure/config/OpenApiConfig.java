package br.com.smanager.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Github",
                        url = "https://github.com/FabricioCaires95"
                ),
                description = "Simple project to create project, members and tasks ",
                title = "Smanager",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
