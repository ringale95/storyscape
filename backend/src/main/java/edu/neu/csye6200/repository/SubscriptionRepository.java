package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.Subscription;
import edu.neu.csye6200.entity.SubscriptionStatus;
import edu.neu.csye6200.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
    List<Subscription> findByUser(User user);
    
    List<Subscription> findByUserId(Long userId);
    
    List<Subscription> findByStatus(SubscriptionStatus status);
    
    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.status = :status")
    List<Subscription> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") SubscriptionStatus status);
    
    @Query("SELECT s FROM Subscription s WHERE s.endDate < :currentDate AND s.status = 'ACTIVE'")
    List<Subscription> findExpiredSubscriptions(@Param("currentDate") LocalDateTime currentDate);
    
    Optional<Subscription> findByIdAndUserId(Long id, Long userId);
    
    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.product.id = :productId AND s.status = 'ACTIVE'")
    Optional<Subscription> findActiveSubscriptionByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
