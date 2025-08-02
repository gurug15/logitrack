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

	
	@Query("""
			SELECT u FROM User u
			WHERE (:query IS NULL OR :query = '' OR
			    CAST(u.id AS string) = :query OR
			    LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
			    LOWER(SUBSTRING(u.email, 1, POSITION('@' IN u.email) - 1)) LIKE LOWER(CONCAT('%', :query, '%')) OR
			    LOWER(u.phone) LIKE LOWER(CONCAT('%', :query, '%')) OR
			    LOWER(u.roleId.roleName) LIKE LOWER(CONCAT('%', :query, '%')))
			""")
			List<User> searchUsersGlobal(@Param("query") String query);

	
	public User findByEmail(String name);
	
}
