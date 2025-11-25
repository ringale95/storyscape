package edu.neu.csye6200.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByIsActiveTrue();
    
    List<Product> findByStatus(ProductStatus status);
    
    List<Product> findByProductType(ProductType productType);
    
    Optional<Product> findByIdAndIsActiveTrue(Long id);
    
    List<Product> findByIsActiveTrueAndStatus(ProductStatus status);
}
