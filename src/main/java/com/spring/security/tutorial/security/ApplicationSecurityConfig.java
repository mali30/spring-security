package com.spring.security.tutorial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.spring.security.tutorial.security.Roles.ADMIN;
import static com.spring.security.tutorial.security.Roles.STUDENT;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Uses bcrypt to encrypt password
     */
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    /*
     * Client must now submit username and password in each request via http calls
     * downside is that there is no way to log out because username and password
     * is sent each request
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                allow all users to see the following matchers
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
//                will protect api student api so that you must have the student roles to access
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    /**
     * method is used for retrieving our users from our database
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails moeUser =  User.builder()
                .username("moe")
                .password(passwordEncoder.encode("password"))
                .roles(STUDENT.name()) // ROLE_STUDENT -> how spring will rename
                .build();

        UserDetails johnnyUser = User.builder()
                .username("johnny")
                .password(passwordEncoder.encode("password"))
                .roles(ADMIN.name())
                .build();

        return new InMemoryUserDetailsManager(moeUser, johnnyUser);
    }
}
