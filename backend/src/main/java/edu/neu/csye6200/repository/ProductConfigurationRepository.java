package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.Product;
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
         * Find product configuration by product and tier
         */
        Optional<ProductConfiguration> findByProductAndTier(Product product, Tier tier);

        /**
         * Find PayAsYouGo product configuration by product and tier with payment
         * eagerly loaded
         * Uses JPQL to eagerly fetch payment relationship
         */
        @Query("SELECT DISTINCT pc FROM ProductConfiguration pc " +
                        "JOIN FETCH pc.payment p " +
                        "WHERE pc.product = :product AND pc.tier = :tier")
        List<ProductConfiguration> findByProductAndTierWithPayment(
                        @Param("product") Product product,
                        @Param("tier") Tier tier);

        /**
         * Find all product configurations for a specific product
         */
        List<ProductConfiguration> findByProduct(Product product);
}
