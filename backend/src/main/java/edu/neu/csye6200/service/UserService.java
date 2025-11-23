package edu.neu.csye6200.service;

import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUserByID(Long id) {
        User u1 = userRepository.findById(id).orElse(null);
        u1.setUsername("YOYI");
        return u1;
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = this.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User not found");

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("USER")
                .build();
    }

}
