package cn.pancc.springboot.examples.security.jwt.security.filter;

/**
 * @author pancc
 * @version 1.0
 */

import cn.pancc.springboot.examples.security.jwt.security.handler.LoginFailureHandler;
import cn.pancc.springboot.examples.security.jwt.security.handler.LoginSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author pancc
 * @version 1.0
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {


    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        super();
        super.setAuthenticationManager(authenticationManager);
        super.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        super.setAuthenticationFailureHandler(new LoginFailureHandler());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }


}


