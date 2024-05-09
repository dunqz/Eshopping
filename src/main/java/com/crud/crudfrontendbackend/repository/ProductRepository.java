package com.crud.crudfrontendbackend.repository;

import com.crud.crudfrontendbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Query(value = "SELECT *FROM PRODUCT WHERE product_name = :productName",nativeQuery = true)
    Product findbyProductName(String productName);


    @Transactional
    @Query(value = "SELECT *FROM Product WHERE is_deleted = false;",nativeQuery = true)
    List<Product> findAllNotDeletedProduct();

    @Transactional
    @Query(value = "SELECT *FROM Product WHERE seller= :seller",nativeQuery = true)
    Product findbySeller(String seller);
}
