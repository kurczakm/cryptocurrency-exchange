package com.crypto.tradingplatform.service;

import com.crypto.tradingplatform.domain.*;
import com.crypto.tradingplatform.repository.CryptocurrencyRepository;
import com.crypto.tradingplatform.repository.RoleRepository;
import com.crypto.tradingplatform.repository.UserRepository;
import com.crypto.tradingplatform.web.detail.CustomUserDetails;
import com.crypto.tradingplatform.web.dto.UserRegistrationDto;
import com.crypto.tradingplatform.web.dto.UserUpdateDto;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CryptocurrencyRepository cryptocurrencyRepository;

    private final BigDecimal startFunds = new BigDecimal(1000);

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CryptocurrencyRepository cryptocurrencyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        String email = userRegistrationDto.getEmail();
        String password = userRegistrationDto.getPassword();
        String username = userRegistrationDto.getName();
        User result = null;

        if(validateEmail(email) && validatePassword(password) && validateUsername(username)) {
            List<Cryptocurrency> cryptocurrencies = cryptocurrencyRepository.findAll();
            Wallet wallet = new Wallet(startFunds, cryptocurrencies);

            User user = new User(
                    email,
                    username,
                    passwordEncoder().encode(password),
                    Arrays.asList(roleRepository.getById((long) 1)),
                    wallet
            );

            result = userRepository.save(user);
        }

        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);

        if(user == null)
            throw new UsernameNotFoundException("Invalid username or password.");

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUser(user);
        userDetails.setAuthorities(mapRolesToAuthorities(user.getRoles()));

        return userDetails;
    }

    private Set<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public User updatePassword(UserUpdateDto userUpdateDto) {
        String newPassword = userUpdateDto.getNewPassword();
        User result = null;
        if(validatePassword(newPassword)) {
            User user = userRepository.findByName(userUpdateDto.getName());
            user.setPassword(passwordEncoder().encode(newPassword));
            result = userRepository.save(user);
        }
        return result;
    }

    public User updateEmail(UserUpdateDto userUpdateDto) {
        String newEmail = userUpdateDto.getNewEmail();
        User result = null;
        if(validateEmail(newEmail)) {
            User user = userRepository.findByName(userUpdateDto.getName());
            user.setEmail(newEmail);
            result = userRepository.save(user);
        }

        return result;
    }

    private boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        return emailPattern.matcher(email).matches();
    }

    private boolean validatePassword(String password) {
        Pattern passwordPattern = Pattern.compile("^.{6,128}$");
        return passwordPattern.matcher(password).matches();
    }

    private boolean validateUsername(String username) {
        Pattern passwordPattern = Pattern.compile("^[0-9a-zA-Z]{1,15}$");
        return passwordPattern.matcher(username).matches();
    }
}
