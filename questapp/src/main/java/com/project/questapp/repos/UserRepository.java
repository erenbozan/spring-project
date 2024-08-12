package com.project.questapp.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.questapp.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String username);
}
