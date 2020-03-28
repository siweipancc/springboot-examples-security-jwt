package cn.pancc.springboot.examples.security.jwt.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.pancc.springboot.examples.security.jwt.dao.Role;
import cn.pancc.springboot.examples.security.jwt.dao.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pancc
 * @version 1.0
 */
@CommonsLog
public class JwtProcessor {
    /**
     * JWT密码
     */
    private static final String SECRET = "pancc";
    /**
     * Token前缀
     */
    public static final String BEARER = "Bearer";
    /**
     * 1小时过期
     */
    public static final int EXPIRATION_HOURS = 6;
    /**
     * 存放Token的Header Key
     */
    public static final String AUTHORIZATION = "Authorization";

    public static final String AUTHORITIES = "authorities";


    /**
     * 从User信息产生jwt token ,使用 HS256 签名
     *
     * @param user 合法的User, 通常从持久化库中获得
     * @return jwt
     */
    @Nullable
    public static String generateToke(final User user) {

        try {
            Objects.requireNonNull(user);
            return Jwts.builder()
                    .claim(AUTHORITIES, user.getRoles().stream().map(Role::getName).collect(Collectors.joining(",")))
                    .setSubject(user.getUsername())
                    .setExpiration(DateUtil.offsetHour(new Date(), EXPIRATION_HOURS))
                    .signWith(SignatureAlgorithm.HS256, SECRET)
                    .compact();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static String generateToke(String username, Collection<GrantedAuthority> authorities) {

        try {
            Objects.requireNonNull(username);
            Objects.requireNonNull(authorities);
            return Jwts.builder()
                    .claim(AUTHORITIES, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                    .setSubject(username)
                    .setExpiration(DateUtil.offsetHour(new Date(), EXPIRATION_HOURS))
                    .signWith(SignatureAlgorithm.HS256, SECRET)
                    .compact();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        try {
            Objects.requireNonNull(request);
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String token = StrUtil.subAfter(authorizationHeader, BEARER, true).trim();

            Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);

            String username = jws.getBody().getSubject();
            String authoritiesStr = (String) jws.getBody().get(AUTHORITIES);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesStr);
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (Exception e) {
            log.info("token 中并无有效身份信息");
            return null;
        }
    }

}
