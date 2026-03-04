package com.strikes.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "combat_log")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor

public class CombatLog {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
	@JoinColumn(name = "adventurer_id") // This matches the column name in your DB
	private Adventurer adventurer; // This MUST be named exactly 'adventurer'
    
    private String actionType;  // e.g., "STRIKE" or "HEAL"
    
    @Column(length = 1000)
    private String description; // The full "Shattered Knee: Major bleeding" text
    
    private int severity;       // The 1-6 scale result
    
    private LocalDateTime timestamp = LocalDateTime.now(); // When it happened

}
