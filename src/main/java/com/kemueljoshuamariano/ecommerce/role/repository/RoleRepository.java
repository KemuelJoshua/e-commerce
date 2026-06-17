package com.kemueljoshuamariano.ecommerce.role.repository;

import com.kemueljoshuamariano.ecommerce.role.model.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @EntityGraph(attributePaths = {"permissions"})
    List<Role> findAll();

    @EntityGraph(attributePaths = {"permissions"})
    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = {"permissions"})
    List<Role> findByNameIn(Collection<String> names);
}
