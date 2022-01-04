package com.crypto.tradingplatform.service;

import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto userRegistrationDto);
}
