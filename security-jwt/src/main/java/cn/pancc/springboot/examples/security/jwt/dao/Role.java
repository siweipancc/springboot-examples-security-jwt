package cn.pancc.springboot.examples.security.jwt.dao;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author pancc
 * @version 1.0
 */
@Entity
@Data
@Accessors(chain = true)
public class Role implements Serializable {
    private static final long serialVersionUID = 4780835237354872481L;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "cn.pancc.springboot.examples.security.jwt.dao.IdGenerator")
    private Long id;


    private String name;


}
