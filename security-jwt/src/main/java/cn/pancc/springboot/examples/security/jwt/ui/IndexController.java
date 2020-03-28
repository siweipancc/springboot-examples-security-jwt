package cn.pancc.springboot.examples.security.jwt.ui;

import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 任何 {{@link PreAuthorize}} 操作未满足条件时都会抛出 {@link AccessDeniedException} 错误，由自定义错误处理器处理
 *
 * @author pancc
 * @version 1.0
 * @see SecurityExpressionRoot
 */
@RestController
@RequestMapping("/")
public class IndexController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("user")
    public Map<String, Object> userDetails(@NonNull Authentication authentication) {
        Map<String, Object> map = new HashMap<>(3);
        map.putIfAbsent("username", authentication.getName());
        map.putIfAbsent("authorities", authentication.getAuthorities());
        return map;
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminFacade(@NonNull Authentication authentication) {
        return "你好管理员: " + authentication.getName();

    }
}
