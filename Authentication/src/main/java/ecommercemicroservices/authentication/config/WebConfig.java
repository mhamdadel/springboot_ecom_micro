package ecommercemicroservices.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("./**")
                        .allowedOrigins("*")
                        .allowedHeaders(HttpHeaders.CONTENT_TYPE, "X-CSRF-TOKEN")
                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
                        .allowCredentials(true);
            }
        };
    }
}
