package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
