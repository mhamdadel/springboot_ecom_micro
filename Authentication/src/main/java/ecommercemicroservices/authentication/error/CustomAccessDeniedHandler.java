package ecommercemicroservices.authentication.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler extends RuntimeException implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req,
                       HttpServletResponse res,
                       AccessDeniedException accessDeniedException) throws IOException {



        ObjectMapper mapper = new ObjectMapper();
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        dataMap.put("status", "403");
        dataMap.put("message", accessDeniedException.getMessage());

        JSONObject json = new JSONObject(dataMap);
        String jsonString = json.toString();
        res.getWriter().write(mapper.writeValueAsString(
                jsonString
        ));
    }

}
