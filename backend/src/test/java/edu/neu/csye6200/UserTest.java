package edu.neu.csye6200;

import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.repository.UserRepository;
import edu.neu.csye6200.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    public void testUserRepositorty(){
       List<User>  userList = userRepository.findAll();  // select * from user
        System.out.println(userList);
    }

    @Test
    public void testTransactionMethods(){
        User user = userService.getUserByID(1L);
        System.out.println(user);
    }
}


