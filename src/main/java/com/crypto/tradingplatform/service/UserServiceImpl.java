package com.crypto.tradingplatform.service;

import com.crypto.tradingplatform.domain.Role;
import com.crypto.tradingplatform.domain.User;
import com.crypto.tradingplatform.repository.RoleRepository;
import com.crypto.tradingplatform.repository.UserRepository;
import com.crypto.tradingplatform.web.detail.CustomUserDetails;
import com.crypto.tradingplatform.web.dto.UserRegistrationDto;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        User user = new User(
                userRegistrationDto.getEmail(),
                userRegistrationDto.getName(),
                passwordEncoder().encode(userRegistrationDto.getPassword()),
                Arrays.asList(roleRepository.getById((long) 1))
        );

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

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
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UsernameNotFoundException("Invalid email");

        return user;
    }
}
