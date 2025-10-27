package lwl.config;

import lwl.filter.JwtAuthenticationTokenFilter;
import lwl.handler.AccessDeniedHandlerImpl;
import lwl.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;

@Configuration //配置类
@EnableWebSecurity // 开启Spring Security的功能 代替了 implements WebSecurityConfigurerAdapter
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    AuthenticationConfiguration authenticationConfiguration;//获取AuthenticationManager

    @Resource
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    AccessDeniedHandlerImpl accessDeniedHandler;
    @Resource
    AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // 1. 创建 CorsConfiguration 对象
        CorsConfiguration config = new CorsConfiguration();

        // 2. 配置允许的源、方法和头部
        // 允许所有源（生产环境应限制为明确的域名）
        config.addAllowedOrigin("*");
        // 允许的 HTTP 方法：GET, POST, PUT, DELETE 等
        config.addAllowedMethod("*");
        // 允许的头部信息
        config.addAllowedHeader("*");
        // 是否允许发送 Cookie 或 HTTP 认证信息
        config.setAllowCredentials(true);

        // 3. 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 4. 对所有路径 ('/**') 应用这个配置
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * 配置Spring Security的过滤链。
     *
     * @param http 用于构建安全配置的HttpSecurity对象。
     * @return 返回配置好的SecurityFilterChain对象。
     * @throws Exception 如果配置过程中发生错误，则抛出异常。
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护
                .csrf(AbstractHttpConfigurer::disable)
                // 设置会话创建策略为无状态 不通过Session获取SecurityContext
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置授权规则   指定user/login路径.允许匿名访问(未登录可访问已登陆不能访问). 其他路径需要身份认证
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/user/login")
                                .anonymous()
                                .anyRequest()
                                .authenticated()
                )
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理
                .exceptionHandling(
                        exception -> exception.accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint)
                );

        // 构建并返回安全过滤链
        return http.build();
    }
}
