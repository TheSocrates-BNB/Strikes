package com.strikes.app.controller;

import com.strikes.app.service.CombatService;

import jakarta.transaction.Transactional;

import com.strikes.app.dto.StrikeRequest; // Fixed Import
import com.strikes.app.dto.StrikeResult;  // Added Import
import com.strikes.app.model.Adventurer;
import com.strikes.app.repository.AdventurerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/combat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CombatController {
    
    @Autowired
    private CombatService combatService;

    @PostMapping("/strike")
    public StrikeResult processStrike(@RequestBody StrikeRequest request) {
        return combatService.executeStrike(request);
    }
       
}