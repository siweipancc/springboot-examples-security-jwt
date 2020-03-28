package cn.pancc.springboot.examples.security.jwt.security.handler;

import cn.pancc.springboot.examples.security.jwt.BaseEntity;
import cn.pancc.springboot.examples.security.jwt.security.JwtProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author pancc
 * @version 1.0
 */

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private static ObjectMapper objectMapper = new ObjectMapper();



    @SuppressWarnings("unchecked")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        String username = authentication.getName();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        String token = JwtProcessor.generateToke(username, authorities);

        objectMapper.writeValue(response.getOutputStream(), new BaseEntity().setData(token));

    }
}
