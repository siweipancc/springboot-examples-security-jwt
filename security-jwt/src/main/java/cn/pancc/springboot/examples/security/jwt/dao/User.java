package cn.pancc.springboot.examples.security.jwt.dao;

import cn.pancc.springboot.examples.security.jwt.service.UserDetailsServiceImpl;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 我们并不实现<code> UserDetails </code> , 而是交由业务层包装
 *
 * @author pancc
 * @version 1.0
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see org.springframework.security.core.userdetails.User
 * @see UserDetailsServiceImpl#loadUserByUsername
 */
@Entity
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1139786770280424194L;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "cn.pancc.springboot.examples.security.jwt.dao.IdGenerator")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany(cascade = CascadeType.REFRESH)
    private List<Role> roles;

}
