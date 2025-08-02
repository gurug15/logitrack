package com.project.logitrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.logitrack.Entity.LogisticCenter;

@Repository
public interface LogisticCenterRepository extends JpaRepository<LogisticCenter, Long> {

}
