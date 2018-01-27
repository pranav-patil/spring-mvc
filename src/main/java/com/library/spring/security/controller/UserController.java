package com.library.spring.security.controller;


import com.library.spring.security.model.UserBean;
import com.library.spring.web.domain.Account;
import com.library.spring.web.mapper.UserAccountMapper;
import com.library.spring.web.service.AccountService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

/**
 * @see <a href="https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html">Expression-Based Access Control</a>
 */
@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private AccountService accountService;
    @Resource(name="tokenStore")
    private TokenStore tokenStore;
    @Resource(name="tokenServices")
    private ConsumerTokenServices tokenServices;
    @Autowired
    private UserAccountMapper userAccountMapper;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(path = "/info", produces = "application/json" )
    @ApiOperation(value = "Get Current User Info", notes = "Retrieves the current logged in user's account information.", response = UserBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")})
    public UserBean userInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        return userAccountMapper.mapToUserBean(account);
    }

    @PreAuthorize("hasAuthority('ROLE_REGISTER')")
    @PostMapping(path = "/register", produces = "application/json")
    @ApiOperation(value = "Register New User", notes = "Register a new user with USER role.", response = UserBean.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Basic username:password", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<UserBean> registerUser(@RequestBody UserBean userBean) throws AccountException {
        Account account = userAccountMapper.mapToAccount(userBean);
        account.grantAuthority("ROLE_USER");
        account = accountService.register(account);
        return new ResponseEntity<>(userAccountMapper.mapToUserBean(account), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping(path = "/remove", produces = "application/json")
    @ApiOperation(value = "Remove User", notes = "Removes or deletes a registered user.", response = String.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<?> removeUser() {
        accountService.removeAuthenticatedAccount();
        return new ResponseEntity<>("User removed.", HttpStatus.OK);
    }

    /**
     * @see <a href="http://www.baeldung.com/spring-security-oauth-revoke-tokens">Spring Security OAuth2 – Simple Token Revocation</a>
     * @param clientId
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/tokens/{clientId}")
    @ApiOperation(value = "Get Tokens", notes = "Retrieves all the tokens for specified client id.", response = String.class, responseContainer = "List")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")})
    public List<String> getTokens(@ApiParam(required = true, name = "clientId", value = "Client Id by which to find tokens")
                                  @PathVariable String clientId) {
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/tokens/revoke/{tokenId:.*}")
    @ApiOperation(value = "Revoke Token", notes = "Revokes a token by token id.", response = String.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")})
    public String revokeToken(@ApiParam(required = true, name = "tokenId", value = "Token ID to be revoked")
                              @PathVariable String tokenId) {
        tokenServices.revokeToken(tokenId);
        return tokenId;
    }
}
