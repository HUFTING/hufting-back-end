package com.likelion.hufsting.domain.Member.security.config;

import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.Member.security.GoogleOauthMemberDetailsService;
import com.likelion.hufsting.domain.Member.security.filter.OauthJwtAuthorizationFilter;
import com.likelion.hufsting.domain.Member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final GoogleOauthMemberDetailsService oauthMemberDetailsService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable); // csrf disable
        http.cors(configure -> {
            configure.configurationSource(corsConfigurationSource);
        });
        http.sessionManagement(configure ->
                configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(configure -> {
            configure.requestMatchers("/api/v1/**").hasRole("USER");
            configure.anyRequest().permitAll();
        });
        http.addFilterBefore(oauthJwtAuthorizationFilter(), LogoutFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource cosFilter(){
        // 필요한 객체 초기화
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 허용 URL List 만들기
        List<String> allowedUrls = new ArrayList<>();
        allowedUrls.add("*");
        // config 객체 설정
        config.setAllowCredentials(true);
        config.setAllowedHeaders(allowedUrls);
        config.setAllowedOrigins(allowedUrls);
        config.setAllowedMethods(allowedUrls);
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public OauthJwtAuthorizationFilter oauthJwtAuthorizationFilter(){
        return new OauthJwtAuthorizationFilter(jwtUtil, memberRepository);
    }

    // password encryption bea
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    private GoogleOauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler(){
        return new GoogleOauthAuthenticationSuccessHandler(jwtUtil);
    }
}

