package com.project.logitrack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.logitrack.Entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	
	@Query("SELECT obj FROM User obj WHERE obj.roleId.id = :roleId")
	List<User> getUserByRoleId(@Param("roleId") Integer roleId);

	
	
	public User findByEmail(String name);
}
