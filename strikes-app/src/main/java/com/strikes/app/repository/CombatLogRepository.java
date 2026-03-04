package com.strikes.app.repository;

import com.strikes.app.model.CombatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CombatLogRepository extends JpaRepository<CombatLog, Long> {
    // We add a custom method to find all logs for a specific adventurer
    List<CombatLog> findByAdventurerId(Long adventurerId);
}
