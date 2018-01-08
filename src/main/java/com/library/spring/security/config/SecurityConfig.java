package com.library.spring.security.config;


import com.library.spring.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity( debug = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String LOGIN_PAGE = "/public/view/login";

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(accountService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * NOTE: anonymous().disable() along with loginPage() config causes ERR_TOO_MANY_REDIRECTS error.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/",
                    "/js/**",
                    "/css/**",
                    "/images/**",
                    "/fonts/**",
                    "/webjars/**",
                    "/oauth/token",
                    "/public/view/**",
                    "/version").permitAll()
            .antMatchers("/secure/view/**").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .failureHandler(failureHandler())
            .loginPage(LOGIN_PAGE).permitAll()
            .and()
            .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/secure/view/logout"))
            .logoutSuccessUrl(LOGIN_PAGE)
            .permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(authenticationEntryPoint());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

                String contentType = request.getContentType();

                if(contentType != null && !MediaType.APPLICATION_FORM_URLENCODED.toString().equals(contentType) && contentType.startsWith("application")) {
                    response.getWriter().append("Authentication failure");
                    response.setStatus(401);
                } else {
                    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                    redirectStrategy.sendRedirect(request, response, LOGIN_PAGE);
                }
            }
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

                String contentType = request.getContentType();

                if(contentType != null && !MediaType.APPLICATION_FORM_URLENCODED.toString().equals(contentType) && contentType.startsWith("application")) {
                    response.getWriter().append("Access denied");
                    response.setStatus(403);
                } else {
                    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                    redirectStrategy.sendRedirect(request, response, LOGIN_PAGE);
                }
            }
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {

                String contentType = request.getContentType();

                if(contentType != null && !MediaType.APPLICATION_FORM_URLENCODED.toString().equals(contentType) && contentType.startsWith("application")) {
                    response.getWriter().append("Not authenticated");
                    response.setStatus(401);
                } else {
                    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
                    redirectStrategy.sendRedirect(request, response, LOGIN_PAGE);
                }
            }
        };
    }
}
