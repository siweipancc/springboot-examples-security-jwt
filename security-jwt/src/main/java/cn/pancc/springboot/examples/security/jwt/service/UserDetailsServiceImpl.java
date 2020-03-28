package cn.pancc.springboot.examples.security.jwt.service;

import cn.hutool.core.collection.CollUtil;
import cn.pancc.springboot.examples.security.jwt.security.WebSecurityConfig;
import cn.pancc.springboot.examples.security.jwt.dao.Role;
import cn.pancc.springboot.examples.security.jwt.dao.User;
import cn.pancc.springboot.examples.security.jwt.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 这里将配置给 Security 调用并且存验证 {@link org.springframework.security.core.context.SecurityContextHolder} 中的用户
 *
 * @author pancc
 * @version 1.0
 * @see WebSecurityConfig
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Transactional(readOnly = true, rollbackFor = Exception.class)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username Not Found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> getAuthorities(Collection<Role> roles) {
        if (CollUtil.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }
}
