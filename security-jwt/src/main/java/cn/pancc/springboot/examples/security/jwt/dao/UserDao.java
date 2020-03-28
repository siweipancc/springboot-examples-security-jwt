package cn.pancc.springboot.examples.security.jwt.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author pancc
 * @version 1.0
 */
public interface UserDao extends CrudRepository<User, Long> {


    User findByUsername(String username);
}
