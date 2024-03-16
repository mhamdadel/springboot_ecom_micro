package ecommercemicroservices.authentication.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        dataMap.put("status", "403");
        dataMap.put("message", "Access denied");

        JSONObject json = new JSONObject(dataMap);
        response.getWriter().write(mapper.writeValueAsString(
                json.toString()
        ));
    }
}
