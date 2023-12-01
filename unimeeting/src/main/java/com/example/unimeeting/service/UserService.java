package com.example.unimeeting.service;

import lombok.RequiredArgsConstructor;
import com.example.unimeeting.domain.User;
import com.example.unimeeting.dto.AddUserRequest;
import com.example.unimeeting.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public int save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getIdx();
    }

    public User findByUserId(String user_id){
        return userRepository.findByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByUserIdAndPassword(String user_id, String password){
        return userRepository.findByUserIdAndPassword(user_id, password)
                .orElseThrow(() -> new IllegalArgumentException());
    }
    
}
