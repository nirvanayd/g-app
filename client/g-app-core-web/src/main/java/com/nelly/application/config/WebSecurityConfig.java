package com.nelly.application.config;

import com.nelly.application.handler.CustomAccessDeniedHandler;
import com.nelly.application.handler.CustomAuthenticationEntryPoint;
import com.nelly.application.jwt.ExceptionHandlerFilter;
import com.nelly.application.jwt.JwtAuthenticationFilter;
import com.nelly.application.jwt.TokenProvider;
import com.nelly.application.util.CacheTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final CacheTemplate cacheTemplate;
    private final CorsFilter corsFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/hello", "/env").permitAll()
                .antMatchers("/sign-up", "/login", "/authority", "/duplicate-id", "/find-id", "/reset-password").permitAll()
                .antMatchers("/brands/rank", "/brands/intro/**", "/brands/keyword").permitAll()
                .antMatchers( "/users/**").hasRole("USER")
                .antMatchers( "/contents/**").hasRole("USER")
                .antMatchers( "/brands/**").hasRole("USER")
//                .antMatchers().hasRole("ADMIN")
                .and()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        new JwtAuthenticationFilter(tokenProvider, cacheTemplate),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
                // JwtAuthenticationFilter를 UsernamePasswordAuthentictaionFilter 전에 적용시킨다.
    }

    // 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
