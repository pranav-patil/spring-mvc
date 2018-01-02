package com.library.spring.web.service;

import com.library.spring.web.domain.Account;
import com.library.spring.web.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername( username );
        if ( account.isPresent() ) {
            return account.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
        }
    }

    public Account findAccountByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername( username );
        if ( account.isPresent() ) {
            return account.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
        }
    }

    public Account register(Account account) throws AccountException {
        if ( accountRepository.countByUsername( account.getUsername() ) == 0 ) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            return accountRepository.save( account );
        } else {
            throw new AccountException(String.format("Username[%s] already taken.", account.getUsername()));
        }
    }

    /**
     * @Transactional annotation must be added to successfully remove the date.
     */
    @Transactional
    public void removeAuthenticatedAccount() throws UsernameNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = findAccountByUsername(username);
        accountRepository.deleteAccountById(account.getId());
    }
}
