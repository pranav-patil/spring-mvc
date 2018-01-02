package com.library.spring.security.controller;


import com.library.spring.web.domain.Account;
import com.library.spring.web.service.AccountService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.login.AccountException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class ApiController {

    @Autowired
    private AccountService accountService;
    @Resource(name="tokenStore")
    private TokenStore tokenStore;
    @Resource(name="tokenServices")
    private ConsumerTokenServices tokenServices;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(path = "/info", produces = "application/json" )
    public Account userInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.findAccountByUsername(username);
    }

    @PreAuthorize("hasAuthority('ROLE_REGISTER')")
    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<?> registerUser(@RequestBody Account account) throws AccountException {
        account.grantAuthority("ROLE_USER");
        return new ResponseEntity<Object>(accountService.register(account), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path = "/remove", produces = "application/json")
    public ResponseEntity<?> removeUser() {
        accountService.removeAuthenticatedAccount();
        return new ResponseEntity<>("User removed.", HttpStatus.OK);
    }

    /**
     * @see <a href="http://www.baeldung.com/spring-security-oauth-revoke-tokens">Spring Security OAuth2 – Simple Token Revocation</a>
     * @param clientId
     * @return
     */
    @GetMapping(path = "/tokens/{clientId}")
    public List<String> getTokens(@ApiParam(required = true, name = "clientId", value = "Client Id by which to find tokens") @PathVariable String clientId) {
        List<String> tokenValues = new ArrayList<String>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
        if (tokens!=null){
            for (OAuth2AccessToken token:tokens){
                tokenValues.add(token.getValue());
            }
        }
        return tokenValues;
    }

    /**
     * @see <a href="http://www.baeldung.com/spring-security-oauth-revoke-tokens">Spring Security OAuth2 – Simple Token Revocation</a>
     */
    @PostMapping(path = "/tokens/revoke/{tokenId:.*}")
    public String revokeToken(@PathVariable String tokenId) {
        tokenServices.revokeToken(tokenId);
        return tokenId;
    }
}
