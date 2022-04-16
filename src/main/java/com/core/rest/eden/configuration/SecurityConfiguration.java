package com.core.rest.eden.configuration;

import com.core.rest.eden.configuration.filter.CustomAuthenticationFilter;
import com.core.rest.eden.configuration.filter.CustomAuthorizationFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

//import static org.springframework.web.bind.annotation.RequestMethod.GET;


@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
                .antMatchers("/token/refresh/**")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers(GET, "/users/**", "/posts/**", "/comments/**" ).hasAnyAuthority("USER");
        http
                .authorizeRequests()
                .antMatchers(POST, "/users/**", "/posts/**", "/comments/**" ).hasAnyAuthority("USER");
        http
                .authorizeRequests()
                .antMatchers(DELETE, "/users/**" ).hasAnyAuthority("ADMIN");
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        http
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
