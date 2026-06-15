package com.kemueljoshuamariano.ecommerce.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "providerId"})
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 255)
    private String providerId;

    @Column(unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstname;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String lastname;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(nullable = false, length = 50)
    private String role = "USER";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}