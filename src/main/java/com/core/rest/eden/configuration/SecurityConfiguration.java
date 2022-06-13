package com.core.rest.eden.configuration;

import com.core.rest.eden.configuration.filter.CustomAuthenticationFilter;
import com.core.rest.eden.configuration.filter.CustomAuthorizationFilter;
import com.core.rest.eden.services.AuthenticationService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationService authenticationService;
    private final UserService userService;



    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* Enable CORS */
        http
                .cors();

        /* Disable CSRF */
        http
                .csrf()
                .disable();

        /* Add XSS Backend Protection */
        http
                .headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'");

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /* Permit Resources */
        http
                .authorizeRequests()
                .antMatchers(POST, "/auth/register")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers(GET, "/auth/token/refresh/**", "/auth/token/revoke/**")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers(GET, "/topics")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers(GET, "/users/**", "/posts/**", "/comments/**", "/files/**" ).hasAnyAuthority("USER");
        http
                .authorizeRequests()
                .antMatchers(POST, "/users/**", "/posts/**", "/comments/**", "files/**" ).hasAnyAuthority("USER");
        http
                .authorizeRequests()
                .antMatchers(DELETE, "/users/**" ).hasAnyAuthority("ADMIN");
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        http
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), authenticationService, userService));
        http
                .addFilterBefore(new CustomAuthorizationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);

    }


    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        corsConfiguration.setMaxAge(Duration.ofMinutes(10));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
