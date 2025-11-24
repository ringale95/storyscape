package edu.neu.csye6200.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.dto.LoginDTO;
import edu.neu.csye6200.dto.UserRegisterDTO;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.factory.UserFactory;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    private UserFactory userFactory = UserFactory.getInstance();

    public boolean authenticate(LoginDTO loginDTO) {
        User user = userService.findByEmail(loginDTO.getEmail());
        if (user == null)
            throw new IllegalArgumentException("User not found");

        return BCrypt.checkpw(loginDTO.getPassword(), user.getPasswordHash());
    }

    public User register(UserRegisterDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is missing");
        }

        if (userService.findByEmail(dto.getEmail()) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        User newUser = userFactory.createUserFromRegistration(dto);
        return userService.saveUser(newUser);
    }
}
