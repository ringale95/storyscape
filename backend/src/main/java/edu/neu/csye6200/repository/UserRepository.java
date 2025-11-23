package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
