package com.project.logitrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.logitrack.Entity.TrackingHistory;

public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Long> {

}
