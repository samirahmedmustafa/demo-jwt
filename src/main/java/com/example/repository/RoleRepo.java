package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{
	Role findByName(String name);
}
