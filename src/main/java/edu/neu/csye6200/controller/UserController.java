package edu.neu.csye6200.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
   @GetMapping(value = "/api/users")
    public String user(){
       return "Welcome";
   }
}
