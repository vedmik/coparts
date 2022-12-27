package software.sigma.bu003.internship.coparts.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Coparts API",
        version = "1.0",
        description = """
            Coparts - spare parts for cars from Copart.com.
            With this application, you can create a service to order any spare parts for any car, knowing only the part code.
        """))
public class OpenApiConfig {}
