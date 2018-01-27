package com.library.spring.security.config;

import com.library.spring.security.filter.OAuth2AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * WebSecurityConfigurerAdapter with order=0 is an order inferior to the ResourceServerConfigurerAdapter with order=3
 * Hence ResourceServerConfigurerAdapter is a catch-all fallback for WebSecurityConfigurerAdapter at order=0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${security.oauth2.resource.id}")
	private String resourceId;
	@Autowired
	private DefaultTokenServices tokenServices;
	@Autowired
	private TokenStore tokenStore;
	private static final String LOGIN_PAGE = "/public/view/login";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(resourceId)
				 .tokenServices(tokenServices)
				 .tokenStore(tokenStore)
				 .stateless(false)
				 .accessDeniedHandler(accessDeniedHandler())
				 .authenticationEntryPoint(authenticationEntryPoint());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.anonymous().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
			.requestMatchers()
				.antMatchers("/api/**")
				.antMatchers("/user/**")
				.antMatchers("/secure/view/**")
			.and()
			.authorizeRequests()
				.antMatchers("/api/**").hasRole("USER")
				.antMatchers("/secure/view/**").hasRole("USER")
				.and()
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(authenticationEntryPoint())
				.and()
				.addFilterBefore(new OAuth2AuthenticationFilter(), SecurityContextPersistenceFilter.class);
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