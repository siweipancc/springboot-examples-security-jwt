package cn.pancc.springboot.examples.security.jwt.dao;

import org.springframework.data.repository.CrudRepository;

/**
 * @author pancc
 * @version 1.0
 */
public interface RoleDao extends CrudRepository<Role, Long> {

}
