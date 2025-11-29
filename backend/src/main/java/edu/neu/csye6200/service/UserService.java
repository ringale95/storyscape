package edu.neu.csye6200.service;

import edu.neu.csye6200.dto.UpdateUserDTO;
import edu.neu.csye6200.entity.Tier;
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
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }


    @Transactional
    public User updateUser(Long userId, UpdateUserDTO dto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update only non-null fields (partial update pattern)
        if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) {
            existingUser.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().isEmpty()) {
            existingUser.setLastName(dto.getLastName());
        }

        if (dto.getBio() != null) {
            existingUser.setBio(dto.getBio());
        }

        if (dto.getTier() != null && !dto.getTier().isEmpty()) {
            try {
                existingUser.setTier(Tier.valueOf(dto.getTier().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid tier value: " + dto.getTier());
            }
        }

        if (dto.getWalletCents() != null && dto.getWalletCents() >= 0 && dto.getWalletCents() >= existingUser.getWalletCents()) {
            existingUser.setWalletCents(dto.getWalletCents());
        }

        return userRepository.save(existingUser);
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

