package com.kemueljoshuamariano.ecommerce.user.repository;

import com.kemueljoshuamariano.ecommerce.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles r
        LEFT JOIN FETCH r.permissions
        WHERE u.username = :username
    """)
    Optional<User> findByUsernameWithRoles(@Param("username") String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
