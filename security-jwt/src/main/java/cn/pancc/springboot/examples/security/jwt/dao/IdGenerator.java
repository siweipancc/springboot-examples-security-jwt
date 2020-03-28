package cn.pancc.springboot.examples.security.jwt.dao;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @author pancc
 * @version 1.0
 */
@SuppressWarnings("unused")
@CommonsLog
public class IdGenerator implements IdentifierGenerator {
    private static final Snowflake SNOWFLAKE = new Snowflake(1, 2L);

    public IdGenerator() {
        log.info("使用ID产生器: " + getClass());
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Long id = SNOWFLAKE.nextId();
        log.debug(String.format("为类 [%s] 生产 id: {%s}", object.getClass().getName(), id));
        return id;
    }
}
