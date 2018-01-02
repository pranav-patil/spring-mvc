package com.library.spring.web.mapper;

import com.library.spring.security.model.UserBean;
import com.library.spring.web.domain.Account;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(
        withIgnoreFields = {"id", "name", "roles", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"},
        withIoC = IoC.SPRING
)
public interface UserAccountMapper {

    UserBean mapToUserBean(Account account);

    Account mapToAccount(UserBean userBean);
}
