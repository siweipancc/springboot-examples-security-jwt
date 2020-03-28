package cn.pancc.springboot.examples.security.jwt;

import cn.pancc.springboot.examples.security.jwt.dao.Role;
import cn.pancc.springboot.examples.security.jwt.dao.RoleDao;
import cn.pancc.springboot.examples.security.jwt.dao.User;
import cn.pancc.springboot.examples.security.jwt.dao.UserDao;
import cn.pancc.springboot.examples.security.jwt.security.JwtProcessor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

/**
 * @author pancc
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@CommonsLog
@SpringBootApplication
public class SecurityJwtApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class, args);
    }




    /*初始化两个用户数据，用于前端直接调用测试*/

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public void run(String... args) {
        /*
         * 加密方式与设置相同
         * @see WebSecurityConfig
         */
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole = roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole = roleDao.save(userRole);


        User adminUser = new User();
        adminUser.setRoles(Collections.singletonList(adminRole)).setUsername("admins").setPassword(encoder.encode("admin"));
        User userUser = new User();
        userUser.setRoles(Collections.singletonList(userRole)).setUsername("user").setPassword(encoder.encode("user"));

        adminUser = userDao.save(adminUser);
        userUser = userDao.save(userUser);

        String adminToken = JwtProcessor.generateToke(adminUser);
        String userToken = JwtProcessor.generateToke(userUser);

        log.info(String.format("已存放两个用户:\n %s，密码 {%s}, jwt-token {%s} \n %s，密码 {%s}, jwt-token {%s}",
                adminUser, "admin", adminToken,
                userUser, "user", userToken));


    }
}
