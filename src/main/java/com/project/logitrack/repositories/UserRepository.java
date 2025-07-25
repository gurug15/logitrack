package com.project.logitrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.logitrack.Entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String name);
}
