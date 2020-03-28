# 1. 简述
## 1.1 安全框架
使用 Spring-Security 作为安全框架实现

## 1.2 实体类与表结构
遵从 Spring-Security 核心设计，分为用户与角色表

在类设计中将专注于类关系的构建，因此
User 避免直接实现 *UserDetails* , 将 Authority 的获取放到 **service** 层中

## 1.3 前后端传递包装类 BaseEntity
规定：用于在前后端交互中包装传递信息

## 1.4 JWT 处理器 JwtProcessor
处理 token 的生成与解析

## 1.5 核心过滤器

### 1.5.1 表单信息获取过滤器 JwtLoginFilter
直接继承 框架提供的 UsernamePasswordAuthenticationFilter ，用于过滤表单中 `username` 和 `password`,
重写 `successfulAuthentication` 与 `unsuccessfulAuthentication` 方法直接向浏览器打印登录回显信息

### 1.5.2 token 头过滤器 JwtAuthenticationFilter  
过滤请求头 'Authorization' 中的请求信息，如 `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4iLCJzdWIiOiJhZG1pbnMiLCJleHAiOjE1ODUzODU4MTd9.mAijBO5nREegGlJn_8W_vxVqruqsnXx4Zn_dtcM2FcQ`

## 1.6 登陆请求处理器 LoginSuccessHandler 与 LoginFailureHandler

### 1.6.1 LoginSuccessHandler
由 `JwtLoginFilter` 调用，处理 `JwtLoginFilter` 传递过来的登陆成功信息，获取用户名与权限，调用 `JwtProcessor` 生成 token 包装于`BaseEntity`
中，传递给前端

### 1.6.2 LoginFailureHandler
由 `JwtLoginFilter` 调用，处理 `JwtLoginFilter` 传递过来的登陆失败信息，获取错误信息包装于`BaseEntity`中，传递给前端

# 2. 额外说明

## 2.2 UsernamePasswordAuthentication
UsernamePasswordAuthentication 的产生方式有两种
### 2.2.1 一类产生方式
第一种为登陆过滤器产生，此时的产物需要调用 service 验证合法性，注意下边的代码：
```java  

    // WebSecurityConfig.java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
``` 

我们传递了自定义的 `UserDetailsService` 并确定了加密方式，因此我们在`org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder.performBuild`
方法中，Spring 使用了自定义的 `UserDetailsService` 产生 `ProviderManager`,当我们的自定义登陆过滤器 **JwtLoginFilter** 工作时，
当进入 `attemptAuthentication` 方法时，就会调用该 *ProviderManager* 

### 2.2.1 二类产生方式
第二种为 token 头过滤器产生，此时的 token 由于是我们我们在登陆的时候服务器返回的（一类产生方式的最终结果），
即已经验证签名并包括`用户名`与`权限`的，此时 `JwtAuthenticationFilter` 将直接获取相应的信息产生 UsernamePasswordAuthentication


## 2.1 Session 与 SecurityContextHolder 的陷阱
身份信息在服务器的传递在 SecurityContextHolder， 由于使用前后端分离，session 是不必要的。且 SecurityContextHolder 是
绑定在 session 中的，这意味着持有同一个 session 的浏览器，即我们第一次请求根据表单或者 token 头 过滤器创建了 Authentication，之后，
以后的每次请求都会使用同一份 Authentication，即使之后的请求不带有任何身份信息。

解决方式——在安全配置类关闭session: `http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);`

## 2.2 方法安全拦截
为了简化代码，使用了方法安全拦截 ` @EnableGlobalMethodSecurity(prePostEnabled = true)`, ` @PreAuthorize`  ,
抛错默认由 Spring 管理（见`org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.error`）

# 3. 测试
 - 配置数据库
 - 运行 SecurityJwtApplication ，控制台打印预留的两个身份信息，包括 token
 - 使用 postman 等向 `http://localhost:8080/login` 发送表单请求
 - 使用 头部与不同 token 组合形式 " Authorization: Bearer ${token} " 向 url : `http://localhost:8080/user` 与 `http://localhost:8080/admin` 发送请求 
 








