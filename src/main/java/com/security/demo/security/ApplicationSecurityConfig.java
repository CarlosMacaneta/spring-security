package com.security.demo.security;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
     * 
     * Generating crsf token
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "index", "/css/", "/js/*").permitAll()
            .antMatchers("/api/**").hasRole(Role.STUDENT.name())
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .usernameParameter("username")
                .passwordParameter("password")
            .and()
            .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1))
                .key("securedkeytime")
                .rememberMeParameter("remember-me")
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // if csrf is enabled this line must be removed
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");
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
