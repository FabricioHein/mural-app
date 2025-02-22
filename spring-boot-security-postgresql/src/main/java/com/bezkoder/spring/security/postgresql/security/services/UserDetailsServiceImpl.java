package com.bezkoder.spring.security.postgresql.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Find user by username (e.g., a unique username, not an email)
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    return UserDetailsImpl.build(user); // Assuming UserDetailsImpl is your custom implementation
  }

  @Transactional
  public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
    // Find user by email
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
    return UserDetailsImpl.build(user); // Assuming UserDetailsImpl is your custom implementation
  }

}
