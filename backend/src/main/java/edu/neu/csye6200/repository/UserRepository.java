package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    /**
     * Find user by ID with subscriptions and their payments eagerly loaded
     * Uses DISTINCT to prevent duplicate User entities from JOIN FETCH
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.subscriptions s " +
           "LEFT JOIN FETCH s.payment " +
           "WHERE u.id = :userId")
    Optional<User> findByIdWithSubscriptions(@Param("userId") Long userId);
}
