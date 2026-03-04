package com.strikes.app.controller;

import com.strikes.app.model.Adventurer;
import com.strikes.app.repository.AdventurerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adventurers")
@CrossOrigin(origins = "*", allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class AdventurerController {

	@Autowired
	private AdventurerRepository adventurerRepository;

	// Create a new Adventurer (PC or Beast)
	@PostMapping
	public Adventurer create(@RequestBody Adventurer newHero) {
		newHero.setCurrentHp(newHero.getMaxHp());
		newHero.setStatus("HEALTHY");
		return adventurerRepository.save(newHero);
	}

	// List all current party members
	@GetMapping
	public List<Adventurer> getAll() {
		return adventurerRepository.findAll();
	}

	// Get a specific one by ID
	@GetMapping("/{id}")
	public Adventurer getOne(@PathVariable Long id) {
		return adventurerRepository.findById(id).orElseThrow();
	}

	// 3. RESET: Restore a specific hero to full health
	@PutMapping("/{id}/reset")
	public Adventurer reset(@PathVariable Long id) {
		Adventurer hero = adventurerRepository.findById(id).orElseThrow(() -> new RuntimeException("Hero not found"));

		hero.setCurrentHp(hero.getMaxHp());
		hero.setStatus("HEALTHY");
		hero.setWounds(""); // Wipe the wound description
		return adventurerRepository.save(hero);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
	    adventurerRepository.deleteById(id);
	}

}
