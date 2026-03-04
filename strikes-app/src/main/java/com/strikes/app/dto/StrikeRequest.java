package com.strikes.app.dto; 

import com.strikes.app.model.enums.DamageType; 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class StrikeRequest {
    private Long targetId; 
    private DamageType type; 
    private int rawDamage; 
    private int locationRoll; 
    private String manualLocation; // 👈 NEW: e.g., "HEAD" or "TORSO"
}