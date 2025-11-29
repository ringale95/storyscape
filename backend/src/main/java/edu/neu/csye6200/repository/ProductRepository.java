package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by name (case-insensitive)
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find product by name (exact match, case-insensitive)
     */
    Optional<Product> findByNameIgnoreCase(String name);
}
