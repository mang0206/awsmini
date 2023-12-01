package com.example.unimeeting.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.unimeeting.repository.UserRepository;
import com.example.unimeeting.domain.User;
import lombok.RequiredArgsConstructor;

@Service
public interface MyUserDetailsService extends UserDetailsService {

    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException;
}
