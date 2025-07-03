package com.roomo.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("lister@example.com".equals(username)) {
            return new User(
                    username,
                    new BCryptPasswordEncoder().encode("password"),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_LISTER"))
            );
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
