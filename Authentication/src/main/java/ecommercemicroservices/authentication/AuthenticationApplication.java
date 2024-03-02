package ecommercemicroservices.authentication;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ecommercemicroservices.authentication.config.RsaKeyConfigProperties;

@SpringBootApplication
@ComponentScan(value = { "ecommercemicroservices.authentication.*" })
@EntityScan(value = "ecommercemicroservices.authentication.*")
@EnableJpaRepositories(value = "ecommercemicroservices.authentication.*")
@OpenAPIDefinition(
        info = @Info(
                title = "Authentication Api Guide",
                version = "1.0.0",
                description = "Authentication Api Guide",
                termsOfService = "muhammed",
                contact = @Contact(
                        name = "Mr Muhammed Adel",
                        email = "muhammed.adel.elshall@gmail.com"
                ),
                license = @License(
                        name = "muhammed",
                        url = "mhamdadel.github.io/index"
                )
        )

)
@EnableConfigurationProperties(RsaKeyConfigProperties.class)
public class AuthenticationApplication {
    public static void main(String[] args) {
            SpringApplication.run(AuthenticationApplication.class, args);
    }

}
