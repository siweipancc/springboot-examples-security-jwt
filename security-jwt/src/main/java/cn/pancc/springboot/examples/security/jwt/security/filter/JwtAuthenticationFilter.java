package cn.pancc.springboot.examples.security.jwt.security.filter;

import cn.pancc.springboot.examples.security.jwt.security.JwtProcessor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 头只需要过滤一次
 *
 * @author pancc
 * @version 1.0
 */
@CommonsLog
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = JwtProcessor.getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("当前登录用户: " + authentication.getName());
        }
        filterChain.doFilter(request, response);
    }
}
