package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.ProductConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductConfigurationRepository extends JpaRepository<ProductConfiguration, Long> {
}

