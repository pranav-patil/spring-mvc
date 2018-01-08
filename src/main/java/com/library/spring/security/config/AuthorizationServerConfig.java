package com.library.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${security.oauth2.resource.id}")
	private String resourceId;
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(this.authenticationManager)
				 .tokenServices(tokenServices())
				 .tokenStore(tokenStore());
	}

	/**
	 * @see <a href="https://github.com/spring-projects/spring-security-oauth/issues/1222">Upgrading to spring-security@5 - PasswordEncoder</a>
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
				   .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
				   .passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	/**
	 * @see <a href="http://sivatechlab.com/secure-rest-api-using-spring-security-oauth2/">How to Secure REST API using Spring Security and OAuth2</a>
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("normal-app")
				.authorizedGrantTypes("authorization_code", "implicit")
				.authorities("ROLE_CLIENT")
				.scopes("read", "write")
				.resourceIds(resourceId)
				.accessTokenValiditySeconds(3600)
				.refreshTokenValiditySeconds(10000)
				.and()
				.withClient("trusted-app")
				.authorizedGrantTypes("client_credentials", "password", "refresh_token")
				.authorities("ROLE_TRUSTED_CLIENT")
				.scopes("read", "write")
				.resourceIds(resourceId)
				.accessTokenValiditySeconds(3600)
				.refreshTokenValiditySeconds(10000)
				.secret(clientSecret)
				.and()
				.withClient("register-app")
				.authorizedGrantTypes("client_credentials")
				.authorities("ROLE_REGISTER")
				.scopes("read")
				.resourceIds(resourceId)
				.secret(clientSecret);
	}

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}
}