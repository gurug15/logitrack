package com.project.logitrack.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.logitrack.Entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	Optional<Item> findByName(String name);
}
