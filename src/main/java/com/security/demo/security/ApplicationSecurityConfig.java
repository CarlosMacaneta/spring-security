package com.security.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    /**
     * Injecting password encoder
     * @param passwordEncoder
     */
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Basic Auth
     * 
     * using antMatchers to wide list permission withou auth
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/", "index", "/css/", "/js/*").permitAll()
            .antMatchers("/api/**").hasRole(Role.STUDENT.name())
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
    }

    /**
     * creating in memory user
     * This method can retrieve user from database
     */
    @Override
    @Bean 
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("annasmith")
            .password(passwordEncoder.encode("password"))
            .roles(Role.STUDENT.name())
            .build();
        
        UserDetails admin =User.builder()
            .username("linda")
            .password(passwordEncoder.encode("password123"))
            .roles(Role.ADMIN.name())
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
    
}
