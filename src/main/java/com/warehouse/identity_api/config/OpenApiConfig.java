package com.warehouse.identity_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Server server = new Server();
    server.setUrl("http://localhost:8084/identity-system");
    server.setDescription("Identity System API");

    Contact contact = new Contact();
    contact.setName("Phoebe Dev");

    Info info = new Info().title("Identity System API")
                          .version("1.0")
                          .description("This Api exposes endpoints to manage identity systems")
                          .contact(contact);

    SecurityScheme userIdScheme = new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                                      .in(SecurityScheme.In.HEADER).name("X-User-Id");

    SecurityScheme roleScheme = new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                                                    .name("X-Role");

    SecurityScheme emailScheme = new SecurityScheme().type(SecurityScheme.Type.APIKEY).type(SecurityScheme.Type.APIKEY)
                                                     .in(SecurityScheme.In.HEADER).name("X-Email");

    Components components = new Components().addSecuritySchemes("X-User-Id", userIdScheme)
                                            .addSecuritySchemes("X-Role", roleScheme)
                                            .addSecuritySchemes("X-Email", emailScheme);

    SecurityRequirement securityRequirement = new SecurityRequirement().addList("X-User-Id")
                                                                       .addList("X-Role")
                                                                       .addList("X-Email");

    return new OpenAPI().info(info).servers(List.of()).components(components).addSecurityItem(securityRequirement);
  }
}
