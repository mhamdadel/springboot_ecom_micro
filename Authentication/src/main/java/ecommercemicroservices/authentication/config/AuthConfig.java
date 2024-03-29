package ecommercemicroservices.authentication.config;

import ecommercemicroservices.authentication.auth.JwtAuthenticationEntryPoint;
import ecommercemicroservices.authentication.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
public class AuthConfig {


    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }
    @Bean
    public JwtAuthenticationEntryPoint accessDeniedHandler(){
        return new JwtAuthenticationEntryPoint();
    }

}
