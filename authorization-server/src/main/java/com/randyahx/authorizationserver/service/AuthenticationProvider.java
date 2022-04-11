package com.randyahx.authorizationserver.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service @RequiredArgsConstructor
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    // Authenticate user
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = userDetailService.loadUserByUsername(username);

        return verifyPassword(user, password);
    }

    private Authentication verifyPassword(UserDetails user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
