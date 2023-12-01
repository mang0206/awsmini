package com.example.unimeeting.config;

import com.example.unimeeting.config.jwt.JwtAuthenticationFilter;
import com.example.unimeeting.config.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.example.unimeeting.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SpringSecurityConfig {

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() throws Exception {
        // ? : 1개의 문자와 매칭
        // * : 0개 이상의 문자와 매칭
        // ** : 0개 이상의 디렉토리와 파일 매칭
        // images/**을 요청하면 인증절차 없이 클라이언트에 응답
        return (web) -> web.ignoring().requestMatchers("/imgaes/**");
    }

    @Bean
    protected SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthorizationFilter())
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()

                .anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        System.out.println("등록");
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), UserRepository);
    }
}