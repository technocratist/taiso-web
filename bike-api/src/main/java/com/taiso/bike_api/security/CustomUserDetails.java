package com.taiso.bike_api.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.taiso.bike_api.domain.MemberEntity;

public class CustomUserDetails implements UserDetails {

    private final MemberEntity member;

    public CustomUserDetails(MemberEntity member) {
        this.member = member;
    }

    // Provide a default role. Adjust as needed based on your MemberEntity.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // Ensure MemberEntity has a getPassword() method.
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // Ensure MemberEntity has a getUsername() method.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
