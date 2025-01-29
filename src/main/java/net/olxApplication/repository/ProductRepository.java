package net.olxApplication.repository;

import net.olxApplication.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
