package com.System.BankBack.security;

import com.System.BankBack.model.users.User;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

@AllArgsConstructor
class UserPrincipal implements UserDetails {
    private User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){ return List.of(new SimpleGrantedAuthority(user.getRole().name())); }
    @Override
    public String getPassword(){return user.getPassword();}
    @Override
    public String getUsername(){return user.getUsername();}
    @Override
    public boolean isAccountNonExpired(){return true;} @Override public boolean isAccountNonLocked(){return true;} @Override public boolean isCredentialsNonExpired(){return true;} @Override public boolean isEnabled(){return true;}
    static UserDetails build(User u){return new UserPrincipal(u);} }