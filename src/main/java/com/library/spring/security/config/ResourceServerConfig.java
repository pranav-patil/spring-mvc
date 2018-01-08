package com.library.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

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

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(resourceId)
				 .tokenServices(tokenServices)
				 .tokenStore(tokenStore)
				 .stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.anonymous().disable()
			.requestMatchers()
				.antMatchers("/api/**")
				.antMatchers("/user/**")
				.antMatchers("/secure/view/**")
			.and()
			.authorizeRequests()
				.antMatchers("/api/**").hasRole("USER")
				.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/secure/view/**").hasRole("USER");
	}
}