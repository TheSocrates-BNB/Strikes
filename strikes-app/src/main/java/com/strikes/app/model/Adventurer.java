package com.strikes.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adventurers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Adventurer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private int maxHp;
	private int currentHp;

	// Change this to match the Service call

	private String wounds;
	private int dr;

	private int size;
	private int strikeClass;
	private String status;
	private boolean isHumanoid;
	
	@OneToMany(mappedBy = "adventurer", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<CombatLog> combatLogs;
}