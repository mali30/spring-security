package com.spring.security.tutorial.security;

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

import static com.spring.security.tutorial.security.Roles.ADMIN;
import static com.spring.security.tutorial.security.Roles.ADMIN_TRAINEE;
import static com.spring.security.tutorial.security.Roles.STUDENT;
import static com.spring.security.tutorial.security.UserPermissions.COURSE_WRITE;



@Configuration
@EnableWebSecurity
// config for using annotation @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
                .csrf().disable() // todo figure out why I disabled this
                .authorizeRequests()
//                allow all users to see the following matchers
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
//                will protect student api so that you must have the student roles to access
                .antMatchers("/api/v1/students/**").hasAnyRole(
                        STUDENT.name(),
                        ADMIN.name(),
                        ADMIN_TRAINEE.name()
        )
//                permission(authority) based authentication
//                .antMatchers(HttpMethod.GET, "/api/v1/management/students").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())
//                .antMatchers(HttpMethod.PUT, "/api/v1/management/students").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, "/api/v1/management/students").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.DELETE, "/api/v1/management/students/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET, "/api/v1/students/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())
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
        UserDetails annaUser =  User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("password"))
//                .roles(STUDENT.name()) // ROLE_STUDENT -> how spring will rename
                .authorities(STUDENT.getGrantedAuthority())
                .build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password"))
//                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthority())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password"))
//                .roles(ADMIN_TRAINEE.name())
                .authorities(ADMIN_TRAINEE.getGrantedAuthority())
                .build();

        return new InMemoryUserDetailsManager(
                annaUser,
                lindaUser,
                tomUser
        );
    }
}
