package com.strikes.app.service;

import com.strikes.app.model.Adventurer;
import com.strikes.app.model.CombatLog;
import com.strikes.app.model.enums.HitLocation;
import com.strikes.app.model.enums.DamageType;
import com.strikes.app.repository.AdventurerRepository;
import com.strikes.app.repository.CombatLogRepository;
import com.strikes.app.dto.StrikeRequest;
import com.strikes.app.dto.StrikeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CombatService {
	
    @Autowired
    private AdventurerRepository adventurerRepo;
	
    @Autowired
    private CombatLogRepository logRepo;

    @Autowired
    private WoundFactory woundFactory;
    
    @Transactional
    public StrikeResult executeStrike(StrikeRequest request) {
        
        // 1. Load the Target
        Adventurer target = adventurerRepo.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Target not found"));

        // 2. The Math: (Damage - DR) / StrikeClass
        int netDamage = Math.max(0, request.getRawDamage() - target.getDr());
        int severity = 0;
        
        if (netDamage > 0) {
            severity = Math.min(Math.max(netDamage / target.getStrikeClass(), 1), 6);
        }

        // --- 3. UPDATED: Determine Location (Manual or Random) ---
        HitLocation location;
        
        // Check if the request has a manual location name (e.g., "HEAD")
        if (request.getManualLocation() != null && !request.getManualLocation().isEmpty()) {
            // Convert the String from the frontend into the actual Enum type
            location = HitLocation.valueOf(request.getManualLocation());
        } else {
            // Fallback to the d100 roll logic if no manual location was picked
            location = mapRollToLocation(request.getLocationRoll(), target.isHumanoid());
        }

        // 4. Generate Wound from Factory
        StrikeResult result = woundFactory.getWound(request.getType(), location, severity, netDamage);

        // 5. Update Adventurer State
        applyEffectToTarget(target, result);
        adventurerRepo.save(target);

        // 6. Record in Combat History
        createCombatLog(target, result, severity);

        return result;
    }

    private HitLocation mapRollToLocation(int roll, boolean isHumanoid) {
        if (isHumanoid) { 
            if (roll <= 40) return (roll <= 20) ? HitLocation.RIGHT_LEG : HitLocation.LEFT_LEG;
            if (roll <= 50) return HitLocation.ABDOMEN;
            if (roll <= 70) return HitLocation.TORSO;
            if (roll <= 90) return (roll <= 80) ? HitLocation.RIGHT_ARM : HitLocation.LEFT_ARM;
            return HitLocation.HEAD;
        }
        return (roll <= 90) ? HitLocation.TORSO : HitLocation.HEAD; 
    }

    private void applyEffectToTarget(Adventurer target, StrikeResult result) {
        // Update HP and Status string
        target.setCurrentHp(target.getCurrentHp() - result.getHpDamage());
        target.setStatus(result.getWoundName() + ": " + result.getDescription());
        
        // Handle Instant Death (Severity 6) or 0 HP
        if (result.isInstantDeath()) {
            target.setStatus("DEAD: " + result.getWoundName());
            target.setCurrentHp(0);
        } else if (target.getCurrentHp() <= 0) {
            target.setStatus("DOWNED"); 
        }
    }

    private void createCombatLog(Adventurer targetId, StrikeResult result, int severity) {
        CombatLog log = new CombatLog();
        log.setAdventurer(targetId);
        log.setActionType("STRIKE");
        log.setSeverity(severity);
        // Narrative summary for the history table
        log.setDescription(result.getWoundName() + " to " + result.getLocation() + ": " + result.getDescription());
        logRepo.save(log); 
    }
}