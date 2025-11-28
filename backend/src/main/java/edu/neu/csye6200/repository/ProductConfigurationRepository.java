package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductConfigurationRepository extends JpaRepository<ProductConfiguration, Long> {

    /**
     * Find all product configurations for a specific tier
     */
    List<ProductConfiguration> findByTier(Tier tier);

    /**
     * Find product configuration by product name and tier
     */
    Optional<ProductConfiguration> findByProductNameAndTier(String productName, Tier tier);

    /**
     * Find PayAsYouGo product configuration by product name and tier with payment eagerly loaded
     * Uses native query to filter by payment_type discriminator and JPQL to fetch payment
     */
    @Query(value = "SELECT pc.* FROM product_configuration pc " +
            "INNER JOIN payments p ON pc.payment_id = p.id " +
            "WHERE pc.product_name = :productName AND pc.tier = :tier AND p.payment_type = 'PAYG' " +
            "LIMIT 1", nativeQuery = true)
    Optional<ProductConfiguration> findPayAsYouGoByProductNameAndTierNative(
            @Param("productName") String productName,
            @Param("tier") String tier);
    
    /**
     * Find PayAsYouGo product configuration by product name and tier with payment eagerly loaded
     * Uses JPQL to eagerly fetch payment relationship
     */
    @Query("SELECT DISTINCT pc FROM ProductConfiguration pc " +
           "JOIN FETCH pc.payment p " +
           "WHERE pc.productName = :productName AND pc.tier = :tier")
    List<ProductConfiguration> findByProductNameAndTierWithPayment(
            @Param("productName") String productName,
            @Param("tier") Tier tier);
}
