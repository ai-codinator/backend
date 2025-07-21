package com.aicodinator.backend.global.config;

import com.aicodinator.backend.domain.user.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF, form-login, http-basic 끄기
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // swagger, actuator 등 퍼밋
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/actuator/health"
                        ).permitAll()
                        // OAuth2 로그인 시작 및 콜백 URL 퍼밋
                        .requestMatchers(
                                "/oauth2/authorization/**",
                                "/login/oauth2/**"
                        ).permitAll()
                        // ADMIN API
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // (선택) 커스텀 로그인 페이지를 쓴다면 지정
                        // .loginPage("/login")

                        // 소셜 로그인 후 사용자 정보 처리 서비스 연결
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oAuth2UserService)
                        )
                        // 로그인 성공 시 리다이렉트 URL (true: 항상 리다이렉트)
                        .defaultSuccessUrl("/dashboard", true)
                );

        return http.build();
    }
}

