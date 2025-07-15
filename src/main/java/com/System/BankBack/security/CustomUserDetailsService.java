package com.System.BankBack.security;

import com.System.BankBack.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*; import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountHolderRepository holderRepo;
    private final AdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.System.BankBack.model.users.User user = adminRepo.findByUsername(username);
        if (user == null) {
            user = holderRepo.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return UserPrincipal.build(user);
    }
}