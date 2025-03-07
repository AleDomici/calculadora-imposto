package com.catalisa.calculadoraImposto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalisa.calculadoraImposto.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}