package com.kemueljoshuamariano.ecommerce.permission.repository;

import com.kemueljoshuamariano.ecommerce.permission.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Optional<Permission> findByName(String name);

    List<Permission> findByNameIn(Collection<String> names);
}
