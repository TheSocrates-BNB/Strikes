package com.strikes.app.repository;

import com.strikes.app.model.Adventurer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdventurerRepository extends JpaRepository<Adventurer, Long> {
    // Basic CRUD (Create, Read, Update, Delete) is automatically included
}
