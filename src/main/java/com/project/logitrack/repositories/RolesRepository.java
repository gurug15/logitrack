package com.project.logitrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.logitrack.Entity.Roles;


@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

}
