package edu.neu.csye6200.service;

import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUserByID(Long id) {
        User u1 =  userRepository.findById(id).orElse(null);
        u1.setUsername("YOYI");
        return u1;
    }
}
