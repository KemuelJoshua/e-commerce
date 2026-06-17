package com.kemueljoshuamariano.ecommerce.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kemueljoshuamariano.ecommerce.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByDeletedFalse();

    Optional<Category> findByName(String name);

    Optional<Category> findById(Long id);

}
