//package ecommercemicroservices.authentication.auth.filters;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import ecommercemicroservices.authentication.model.JwtAuthenticationModel;
//import ecommercemicroservices.authentication.model.redis.TokensEntity;
//import ecommercemicroservices.authentication.service.TokensRedisService;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import javax.swing.text.Utilities;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.Date;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private ObjectMapper mapper=new ObjectMapper();
//
//    private final TokensRedisService tokensRedisService;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            JwtAuthenticationModel authModel = mapper.readValue(request.getInputStream(), JwtAuthenticationModel.class);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(authModel.getUsername(), authModel.getPassword());
//            return authenticationManager.authenticate(authentication);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        String token = Jwts.builder()
//                .setSubject(authResult.getName())
//                .claim("authorities", authResult.getAuthorities())
//                .setIssuedAt(new Date())
//                .setIssuer(SecurityConstants.ISSUER)
//                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC)))
//                .signWith(SignatureAlgorithm.HS256, SecurityConstants.KEY)
//                .compact();
//
//        log.info(token);
//        TokensEntity tokensEntity = TokensEntity.builder().id(Utilities.generateUuid()).authenticationToken(token)
//                .username(authResult.getName())
//                .createdBy("SYSTEM").createdOn(LocalDateTime.now())
//                .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
//                .build();
//        tokensEntity = tokensRedisService.save(tokensEntity);
//        response.addHeader(SecurityConstants.HEADER, String.format("Bearer %s", tokensEntity.getId()));
//        response.addHeader("Expiration", String.valueOf(30*60));
//
//        ConnValidationResponse respModel = ConnValidationResponse.builder().status(HttpStatus.OK.name()).token(String.format("Bearer %s", tokensEntity.getId())).methodType(HttpMethod.GET.name()).isAuthenticated(true).build();
//        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        response.getOutputStream().write(mapper.writeValueAsBytes(respModel));
//    }
//}