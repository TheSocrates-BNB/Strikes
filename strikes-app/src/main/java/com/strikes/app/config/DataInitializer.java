package com.strikes.app.config;

import com.strikes.app.model.Adventurer;
import com.strikes.app.repository.AdventurerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner initDatabase(AdventurerRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				// Create a standard test target
				Adventurer dummy = new Adventurer();
				dummy.setName("Practice Dummy");
				dummy.setMaxHp(100);
				dummy.setCurrentHp(100);
				dummy.setDr(2); // Damage Reduction
				dummy.setStrikeClass(2); // The math divisor
				dummy.setStatus("HEALTHY");
				dummy.setHumanoid(true);

				repository.save(dummy);
				System.out.println("🛡️ Database Initialized: Practice Dummy created (ID: 1)");
			}
		};
	}

}
