package com.hibahumeida.nagarro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;



@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    @Lazy
    private SessionRegistry sessionRegistry;
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	 http
    	 .csrf().disable()
         .authorizeRequests()
         .antMatchers("/", "index", "/css/*", "/js/*","/login").permitAll()
         //permit only admin statement with query params
         .regexMatchers("/statement/\\??(?:&?[^=&]*=[^=&]*)*").hasRole("ADMIN")
         .anyRequest()
         .authenticated()
         .and()
         .formLogin()
         .successHandler(new MyAuthenticationSuccessHandler())
         .and()
         .logout()
         	 .logoutUrl("/logout")
         	 .invalidateHttpSession(true)
	         .clearAuthentication(true)
	         .deleteCookies("JSESSIONID")
	         .logoutSuccessUrl("/login")
	         .and()
	         .exceptionHandling()
	         .accessDeniedHandler(new CustomAccessDeniedHandler())
	         .and()
	         .sessionManagement(session -> session
	        		 .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	        		 .invalidSessionUrl("/logout")
					 .maximumSessions(1)
	                 .maxSessionsPreventsLogin(true)
	                 .expiredUrl("/logout")
	                 .sessionRegistry(sessionRegistry())
	        	        )
	     ;
    	 return http.build();
    }
    
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
    
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(
                admin,
                user
        );

    }
}
