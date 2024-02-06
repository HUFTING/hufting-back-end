package com.likelion.hufsting.domain.Member.security.config;

import com.likelion.hufsting.domain.Member.repository.MemberRepository;
import com.likelion.hufsting.domain.Member.security.filter.AuthenticationAndAuthorizationExceptionHandlerFilter;
import com.likelion.hufsting.domain.Member.security.filter.OauthJwtAuthorizationFilter;
import com.likelion.hufsting.domain.Member.security.filter.ProfileSetUpStatusFilter;
import com.likelion.hufsting.domain.Member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable); // csrf disable
        http.cors(configure -> {
            configure.configurationSource(corsFilter());
        });
        http.sessionManagement(configure ->
                configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(configure -> {
            configure.requestMatchers("/api/v1/my-**").authenticated(); // my-matchingrequests, my-matchingposts
            configure.requestMatchers("/api/v1/profile/**").authenticated();
            configure.requestMatchers("/api/v1/alarms").authenticated();
            configure.requestMatchers("/api/v1/member/**").authenticated();
            configure.requestMatchers("/api/v1/followingList/**").authenticated();
            configure.requestMatchers("/api/v1/searching").authenticated();
            // matching posts authorization
            configure.requestMatchers(HttpMethod.POST, "/api/v1/matchingposts**").authenticated();
            configure.requestMatchers(HttpMethod.PUT, "/api/v1/matchingposts**").authenticated();
            configure.requestMatchers(HttpMethod.DELETE, "/api/v1/matchingposts**").authenticated();
            //// matching requests authorization
            configure.requestMatchers(HttpMethod.POST, "/api/v1/matchingrequests**").authenticated();
            configure.requestMatchers(HttpMethod.PUT, "/api/v1/matchingrequests**").authenticated();
            configure.requestMatchers(HttpMethod.PATCH, "/api/v1/matchingrequests**").authenticated();
            configure.requestMatchers(HttpMethod.DELETE, "/api/v1/matchingrequests**").authenticated();
            configure.anyRequest().permitAll();
        });
        http.addFilterBefore(oauthJwtAuthorizationFilter(), LogoutFilter.class);
        http.addFilterBefore(exceptionHandlerFilter(), OauthJwtAuthorizationFilter.class);
        http.addFilterAfter(profileSetUpStatusFilter(), OauthJwtAuthorizationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter(), ProfileSetUpStatusFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsFilter(){
        // 필요한 객체 초기화
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // config 객체 설정
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "HEAD", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public OauthJwtAuthorizationFilter oauthJwtAuthorizationFilter(){
        return new OauthJwtAuthorizationFilter(jwtUtil, memberRepository);
    }

    @Bean
    public ProfileSetUpStatusFilter profileSetUpStatusFilter(){
        return new ProfileSetUpStatusFilter(memberRepository);
    }

    @Bean
    public AuthenticationAndAuthorizationExceptionHandlerFilter exceptionHandlerFilter(){
        return new AuthenticationAndAuthorizationExceptionHandlerFilter();
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
}

