package com.security.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true) //enables pre-authorize annotations
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
     * Those comment lines were replaced by annotations 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "index", "/css/", "/js/*").permitAll()
            .antMatchers("/api/**").hasRole(Role.STUDENT.name())
            /*.antMatchers(HttpMethod.DELETE, "management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
            .antMatchers(HttpMethod.POST, "management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
            .antMatchers(HttpMethod.PUT, "management/api/**").hasAuthority(UserPermission.COURSE_WRITE.getPermission())
            .antMatchers(HttpMethod.GET, "management/api/**").hasAnyRole(Role.ADMIN.name(), Role.ADMIN_TRAINEE.name())*/
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
            //.roles(Role.STUDENT.name())
            .authorities(Role.STUDENT.getGrantedAuthority())
            .build();
        
        UserDetails admin =User.builder()
            .username("linda")
            .password(passwordEncoder.encode("password123"))
            //.roles(Role.ADMIN.name())
            .authorities(Role.ADMIN.getGrantedAuthority())
            .build();

        UserDetails tomUser = User.builder()
            .username("tom")
            .password(passwordEncoder.encode("password1"))
            //.roles(Role.ADMIN_TRAINEE.name())
            .authorities(Role.ADMIN_TRAINEE.getGrantedAuthority())
            .build();

        return new InMemoryUserDetailsManager(admin, user, tomUser);
    }
    
}
