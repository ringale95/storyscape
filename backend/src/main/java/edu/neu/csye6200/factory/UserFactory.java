package edu.neu.csye6200.factory;

import org.mindrot.jbcrypt.BCrypt;

import edu.neu.csye6200.dto.UserRegisterDTO;
import edu.neu.csye6200.entity.User;

public class UserFactory {
    private static UserFactory instance;
    private UserFactory(){}
    public static UserFactory getInstance(){
        if(instance == null)
            instance = new UserFactory();
        return instance;
    }
    
    public User createUserFromRegistration(UserRegisterDTO dto) {
        //bcrypt hash
        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(12));
        return new User(dto, hashedPassword);
    }
}
