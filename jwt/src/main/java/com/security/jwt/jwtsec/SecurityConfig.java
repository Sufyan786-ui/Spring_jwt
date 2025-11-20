package com.security.jwt.jwtsec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  @Autowired
  DataSource datasource;
  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http  .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll() // allow H2 console
                    .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()) // non-deprecated basic auth
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    // <-- stateless
            )
            .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
            );

    return http.build();
    }

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
UserDetails user = User.withUsername("user")
                    .password("password")
                    .roles("USER")
                    .build();
UserDetails admin = User.withUsername("admin")
                    .password("password")
                    .roles("ADMIN")
                    .build();
JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager(dataSource);
userDetailsService.createUser(user);
userDetailsService.createUser(admin);
return userDetailsService;
  }

}
