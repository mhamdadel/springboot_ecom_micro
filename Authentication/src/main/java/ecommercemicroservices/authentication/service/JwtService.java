package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Order(1)
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @Autowired
    public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Jwt jwt = jwtDecoder.decode(token);
        final Map<String, Object> claimsMap = jwt.getClaims();
        final Claims claims = new DefaultClaims(claimsMap);
        return claimsResolver.apply(claims);
    }

    public Boolean validateToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                throw new AccessDeniedException("Invalid token");
            }

            final Jwt jwt = jwtDecoder.decode(token);
            final Map<String, Object> claimsMap = jwt.getClaims();
            final Claims claims = new DefaultClaims(claimsMap);
            logger.info("Decoded JWT claims: {}", claims);

            String username = claims.getSubject();
            CustomUser user = userRepository.findByUsernameOrEmail(username, username)
                    .orElseThrow(() -> new AccessDeniedException("User not found"));

            if (claims.getExpiration().before(new Date())) {
                throw new AccessDeniedException("Token has expired");
            }

            if (!user.isAccountNonExpired() || !user.isAccountNonLocked() ||
                    !user.isCredentialsNonExpired() || !user.isEnabled()) {
                throw new AccessDeniedException("User account is not active");
            }

            Date tokenIssueDate = claims.getIssuedAt();
            if (user.getUpdatedAt() != null && user.getUpdatedAt().after(tokenIssueDate)) {
                throw new AccessDeniedException("User profile has been updated, token is invalid");
            }

            return true;
        } catch (Exception e) {
            logger.error("Error occurred during token validation: {}", e.getMessage());
            return false;
        }
    }

    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        claims.put("scope", scope);
        claims.put("name", authentication.getName());
        claims.put("principal", authentication.getPrincipal());

        Instant now = Instant.now();

        JwtClaimsSet.Builder claimsSetBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName());

        claims.forEach(claimsSetBuilder::claim);

        JwtClaimsSet jwtClaimsSet = claimsSetBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
