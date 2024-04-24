package com.microcommerce.product.infrastructure.repository;

import com.microcommerce.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Page<Product> findPageBy(Pageable pageable);

}
