package com.strikes.app.dto; // This MUST match the folder structure

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // Added this for Spring compatibility
public class StrikeResult<HitLocation> {
    private HitLocation location;     // Ensure this uses HitLocation, not BodyPart
    private String woundName;    
    private String description;  
    private int hpDamage;        
    private boolean isInstantDeath; 
}