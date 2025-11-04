package com.warehouse.identity_api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  public static final String[] PUBLIC_URLS = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"};

  @Value("${key.internal}")
  private String keyInternal;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public OncePerRequestFilter internalKeyFilter() {
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String internalKeyHeader = request.getHeader("X-Internal-Key");
        if (request.getRequestURI().startsWith("/internal") && !keyInternal.equals(internalKeyHeader)) {
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid internal key");
          return;
        }
        filterChain.doFilter(request, response);
      }
    };
  }

  @Bean
  @Order(1)
  public SecurityFilterChain handleInternalUrls(HttpSecurity http) throws Exception {
    http.securityMatcher("/internal/**")
        .addFilterAfter(internalKeyFilter(), CsrfFilter.class)
        .authorizeHttpRequests(req -> req.anyRequest().permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()));

    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain handlePublicUrls(HttpSecurity http) throws Exception {
    RequestMatcher[] matchers = Arrays.stream(PUBLIC_URLS)
                                      .map(AntPathRequestMatcher::new)
                                      .toArray(RequestMatcher[]::new);

    http.securityMatcher(new OrRequestMatcher(matchers))
        .authorizeHttpRequests(req -> req.anyRequest().permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .formLogin(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(List.of("http://localhost:8084", "http://localhost:8080"));
    configuration.setAllowedHeaders(List.of("Content-Type", "X-Internal-Key", "Authorization"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
