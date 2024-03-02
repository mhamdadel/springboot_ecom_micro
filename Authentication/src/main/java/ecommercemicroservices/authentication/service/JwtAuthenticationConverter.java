package ecommercemicroservices.authentication.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthenticationConverter { //implements Converter<Jwt, AbstractAuthenticationToken> {

    private final ObjectMapper jsonmapper;

    private record Role(String name,
                        Set<Integer> markets,
                        Set<Character> division,
                        Set<String> subdivisions,
                        Set<String> rights,
                        Instant fromDate,
                        Instant toDate) {
    }

    // Constructor
    public JwtAuthenticationConverter(ObjectMapper jsonmapper) {
        this.jsonmapper = jsonmapper;
    }

    // Uncomment this method if implementing Converter interface
    // @Override
    // public AbstractAuthenticationToken convert(Jwt jwt) {
    public AbstractAuthenticationToken convert(Jwt jwt) {
        var grantedAuthorities = new HashSet<GrantedAuthority>();
        if (jwt.hasClaim("roles")) {
            var jwtRoles = getRoles(jwt);

            var roles = jwtRoles.stream()
                    .map(Role::name)
                    .map(name -> "ROLE_" + name.replace(" ", "_").toUpperCase())
                    .map(SimpleGrantedAuthority::new);

            var markets = jwtRoles.stream()
                    .flatMap(role -> role.markets().stream().map(market -> "MARKET_" + market))
                    .map(SimpleGrantedAuthority::new);

            var divisions = jwtRoles.stream()
                    .flatMap(role -> role.division().stream().map(division -> "DIV_" + division))
                    .map(SimpleGrantedAuthority::new);

            var subdivisions = jwtRoles.stream()
                    .flatMap(role -> role.subdivisions().stream().map(subdivision -> "SUBDIV_" + subdivision))
                    .map(SimpleGrantedAuthority::new);

            var rights = jwtRoles.stream()
                    .flatMap(role -> role.rights().stream().map(right -> "RIGHT_" + right))
                    .map(SimpleGrantedAuthority::new);

            // Concatenate authorities and add to grantedAuthorities set
            Stream.of(roles, markets, divisions, subdivisions, rights)
                    .flatMap(authorityStream -> authorityStream)
                    .forEach(grantedAuthorities::add);
        }
        // return new JwtAuthenticationToken(jwt, grantedAuthorities);
        return new JwtAuthenticationToken(jwt, grantedAuthorities);
    }

    private Set<Role> getRoles(Jwt jwt) {
        return jsonmapper.convertValue(jwt.getClaim("roles"), new TypeReference<Set<Role>>() {})
                .stream()
                .filter(role -> isBetween(role.fromDate(), role.toDate()))
                .collect(Collectors.toSet());
    }

    private boolean isBetween(Instant fromDate, Instant toDate) {
        var now = Instant.now();
        return fromDate.isBefore(now) && toDate.isAfter(now);
    }
}
