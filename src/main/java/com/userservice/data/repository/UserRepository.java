package com.userservice.data.repository;

import com.userservice.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query(value = "SELECT * FROM users WHERE Username = CAST(?1 AS varchar) ;", nativeQuery = true)
    Users findByUsername(String Username);
}
