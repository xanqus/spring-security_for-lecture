package com.example.security_forlecture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity// 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
public class SecurityConfig {

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests(authroize -> authroize.antMatchers("/user/**")
                        .authenticated()
                        .antMatchers("/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll())
                //인증 안된 페이지로 가면 로그인 페이지로 보냄
                .formLogin()
                .loginPage("/loginForm")
                //.usernameParameter("username2") username 파라미터 바꾸고싶으면 이렇게
                .loginProcessingUrl("/login")// login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                // /login 안만들어도 됨
                .defaultSuccessUrl("/")
                .and()
                .build();
    }

}