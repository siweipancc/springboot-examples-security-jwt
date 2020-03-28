package cn.pancc.springboot.examples.security.jwt.security;

import cn.pancc.springboot.examples.security.jwt.SecurityJwtApplication;
import cn.pancc.springboot.examples.security.jwt.security.filter.JwtAuthenticationFilter;
import cn.pancc.springboot.examples.security.jwt.security.filter.JwtLoginFilter;
import cn.pancc.springboot.examples.security.jwt.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author pancc
 * @version 1.0
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl customUserDetailsService;


    /**
     * 传递 自定义 service 到 {@link DaoAuthenticationConfigurer } 中
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }


    /**
     * 1. 放置处理 token 头的身份过滤器
     * <p>
     * 2. 放置自定义的登录过滤器
     * <p>
     * 3. 关闭 session 的创建, 避免 从 session 中获取 {@link SecurityContext}
     * <p>
     * 4. 开启全局方法拦截 {@link SecurityJwtApplication}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilter(new JwtLoginFilter(authenticationManager()))
                .addFilterBefore(new JwtAuthenticationFilter(), JwtLoginFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }


}
