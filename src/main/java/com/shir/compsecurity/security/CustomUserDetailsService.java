package com.shir.compsecurity.security;

import com.shir.compsecurity.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountService accounts;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var ua = accounts.findEntityByUsernameOrNull(username);
        if (ua == null) throw new UsernameNotFoundException("User not found: " + username);
        return accounts.toUserDetails(ua);
    }
}
