package com.strikes.app.service;

import com.strikes.app.dto.StrikeResult;
import com.strikes.app.model.enums.HitLocation;
import com.strikes.app.model.enums.DamageType;
import org.springframework.stereotype.Component;

@Component
public class WoundFactory {

	public StrikeResult getWound(DamageType type, HitLocation part, int severity, int damage) {
	    return switch (type) {
	        // Physical
	        case BLUDGEONING, CRUSHING -> handleBludgeoning(part, severity, damage);
	        case SLASHING -> handleSlashing(part, severity, damage);
	        case PIERCING -> handlePiercing(part, severity, damage);
	        case RANGED_PIERCING, RANGED_BLUNT -> handleRanged(part, severity, damage);
	        
	        // Elemental
	        case FIRE -> handleFire(part, severity, damage);
	        case COLD -> handleCold(part, severity, damage);
	        case ACID -> handleAcid(part, severity, damage);
	        case ELECTRICITY -> handleElectricity(part, severity, damage);
	        case NEGATIVE_ENERGY -> handleNegativeEnergy(part, severity, damage);

	        // Restorative Routing
	        case HEALING -> handleHealing(part, severity, damage);
	        case REGENERATION -> handleRegeneration(part, severity, damage); // Specific Sev 5+
	        case RESTORATION -> handleRestoration(part, severity, damage);   // Specific Sev 3+
	    };
	}

	private StrikeResult handleHealing(HitLocation part, int sev, int dmg) {
	    // We treat 'dmg' as 'healing amount' here
	    return switch (sev) {
	        case 1 -> new StrikeResult(part, "Minor Mend", "A small spark of life closes scratches.", -dmg, false);
	        case 2 -> new StrikeResult(part, "Light Heal", "Flesh knits together; pain recedes.", -dmg, false);
	        case 3 -> new StrikeResult(part, "Restoration", "Major healing. Cures Blindness and Daze.", -dmg, false);
	        case 4 -> new StrikeResult(part, "Serious Recovery", "Deep wounds close; bleeding stops instantly.", -dmg, false);
	        case 5 -> new StrikeResult(part, "Regeneration", "Crucial healing. Mends bones and regrows lost " + part.name() + ".", -dmg, false);
	        case 6 -> new StrikeResult(part, "Divine Intervention", "Complete restoration of body and soul.", -dmg, false);
	        default -> new StrikeResult(part, "Soothing Light", "General healing.", -dmg, false);
	    };
	}
	
	private StrikeResult handleRestoration(HitLocation part, int sev, int dmg) {
	    return switch (sev) {
	        case 1, 2 -> new StrikeResult(part, "Soothing Aura", "A gentle hum clears minor headaches. -1 to combat penalties removed.", -dmg, false);
	        case 3 -> new StrikeResult(part, "Lesser Restoration", "Vision clears and ears stop ringing. Blindness and Daze cured.", -dmg, false);
	        case 4 -> new StrikeResult(part, "Greater Restoration", "Neural pathways are flushed clean. Stun and Confusion cured.", -dmg, false);
	        case 5 -> new StrikeResult(part, "Absolute Purge", "All mental and sensory debuffs are evaporated by holy light.", -dmg, false);
	        case 6 -> new StrikeResult(part, "Spiritual Alignment", "The soul is locked back into the body. All non-physical conditions removed.", -dmg, false);
	        default -> new StrikeResult(part, "Restoration", "Biological systems stabilized.", -dmg, false);
	    };
	}

	private StrikeResult handleRegeneration(HitLocation part, int sev, int dmg) {
	    return switch (sev) {
	        case 1, 2, 3 -> new StrikeResult(part, "Tissue Knit", "Accelerated cell growth closes open wounds rapidly.", -dmg, false);
	        case 4 -> new StrikeResult(part, "Bone Fusion", "Cracked and splintered bones in the " + part.name() + " pull back together.", -dmg, false);
	        case 5 -> new StrikeResult(part, "Limb Reattachment", "Severed tendons reconnect; lost function of the " + part.name() + " is restored.", -dmg, false);
	        case 6 -> new StrikeResult(part, "Total Regrowth", "Biological miracle. The " + part.name() + " regrows from the stump instantly.", -dmg, false);
	        default -> new StrikeResult(part, "Regen Pulse", "Matter is being reconstructed.", -dmg, false);
	    };
	}

    private StrikeResult handleBludgeoning(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getBludgeoningHead(sev, dmg);
            case TORSO, ABDOMEN -> getBludgeoningTorso(sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getBludgeoningArm(part, sev, dmg); // Passing 'part'
            case LEFT_LEG, RIGHT_LEG -> getBludgeoningLeg(part, sev, dmg); // Passing 'part'
            default -> new StrikeResult(part, "Impact", "General blunt trauma", dmg, false);
        };
    }

    private StrikeResult getBludgeoningHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Glancing Blow", "Minor bump, no penalty.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Dizzying Blow", "Painful hit, stunned 1 round.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Stunning Strike", "Significant hit, stunned 1d3+1 rounds.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Brutal Hit", "Skull cracked, stunned 4 rounds, -4 to rolls.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Shattering Bash", "Unconscious 1d6 days, permanent stat loss.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Skullcrusher", "Skull crushed, a gruesome death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Bruised", "Head trauma.", dmg, false);
        };
    }

    private StrikeResult getBludgeoningTorso(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.TORSO, "Glancing Thud", "A heavy impact, -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.TORSO, "Dizzying Blow", "A painful hit, Breathless, dazed for 2 rounds.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.TORSO, "Cracking Bash", "A significant hit, stunned 2 rounds, stack -2 to combat rolls.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.TORSO, "Brutal Strike", "Heavy trauma, sternum fractures, 1d3+2 rounds stunned, breathless, stack -2 to combat rolls.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.TORSO, "Crushing Impact", "Dazed and stunned for the remainder of the encounter, severe internal damage.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.TORSO, "Incapacitating Crush", "Sternum and ribcage collapsed; death is imminent if not healed in 4 rounds.", dmg, false);
            default -> new StrikeResult(HitLocation.TORSO, "Bruised", "Torso trauma.", dmg, false); // Fixed text
        };
    }

    private StrikeResult getBludgeoningArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Glancing Thud", "A heavy impact, -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Heavy Blow", "A painful hit, drop hand, stack -2 to combat rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Cracking Hit", "A significant hit, arm fractures at the wrist, stack -4 to combat rolls for this arm.", dmg, false);
            case 4 -> new StrikeResult(part, "Breaking Strike", "Heavy hit, arm breaks at the elbow, 3 rounds dazed, arm cannot be used.", dmg, false);
            case 5 -> new StrikeResult(part, "Crushing Impact", "Brutal hit, arm breaks, 2 rounds stunned, arm cannot be used.", dmg, false);
            case 6 -> new StrikeResult(part, "Incapacitating Crush", "Arm is broken and fractured in multiple locations, incapacitated for the remainder of combat.", dmg, false); // Added Case 6
            default -> new StrikeResult(part, "Bruised", "Arm trauma.", dmg, false);
        };
    }

    private StrikeResult getBludgeoningLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Glancing Thud", "A heavy impact, -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Heavy Blow", "A painful hit, next round move half, stack -2 to combat rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Cracking Hit", "A significant hit, leg fractures, permanent half move, stack -2 to combat rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Breaking Strike", "Heavy hit, leg breaks, 3 rounds dazed, permanent half move, cannot perform mobility moves.", dmg, false);
            case 5 -> new StrikeResult(part, "Crushing Impact", "Brutal hit, leg breaks and splinters, 3 rounds stunned, leg cannot be used, movement reduced to 5 feet/round.", dmg, false);
            case 6 -> new StrikeResult(part, "Incapacitating Crush", "Leg is pulverized; incapacitated for the remainder of the combat.", dmg, false);
            default -> new StrikeResult(part, "Bruised", "Leg trauma.", dmg, false); // Fixed text
        };
    }
    
    private StrikeResult handleSlashing(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getSlashingHead(sev, dmg);
            case TORSO, ABDOMEN -> getSlashingTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getSlashingArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getSlashingLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Slicing Blow", "General cutting trauma.", dmg, false);
        };
    }

    private StrikeResult getSlashingHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Grazed Brow", "A shallow cut. Blood gets in the eyes; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Scalp Rip", "Painful flap of skin removed. Dazed 1 round, Bleeding (1/round).", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Deep Gash", "Blade bites deep. Stunned 1 round, Major Bleeding (2/round), -2 to rolls.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Severed Ear/Nose", "Brutal disfigurement. Stunned 3 rounds, Major Bleeding, -4 to rolls.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Facial Cleaving", "Skull is partially exposed. Unconscious 1d4 days, Permanent Stat Loss.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Decapitated", "The head is removed. Instant, silent death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Laceration", "Minor facial cut.", dmg, false);
        };
    }

    private StrikeResult getSlashingTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Flesh Wound", "A shallow horizontal cut. -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Long Slice", "A wide but shallow wound. Bleeding (1/round), Dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Deep Cut", "Blade finds muscle. Stunned 2 rounds, Major Bleeding (3/round).", dmg, false);
            case 4 -> new StrikeResult(part, "Disemboweling Strike", "Stomach wall is breached. Breathless, stunned 1d3+2 rounds, massive bleeding.", dmg, false);
            case 5 -> new StrikeResult(part, "Eviscerated", "Organs are exposed. Incapacitated for the encounter, Massive Bleeding (5/round).", dmg, false);
            case 6 -> new StrikeResult(part, "Gored", "The torso is nearly split in two. Death is certain without immediate divine intervention.", dmg, true);
            default -> new StrikeResult(part, "Slash", "Torso laceration.", dmg, false);
        };
    }

    private StrikeResult getSlashingArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Nicked Bicep", "Small cut. -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Muscle Cut", "Blade bites into the forearm. Drop held item, Bleeding (1/round).", dmg, false);
            case 3 -> new StrikeResult(part, "Severed Tendon", "The arm goes limp. -4 to rolls for this hand, Bleeding (2/round).", dmg, false);
            case 4 -> new StrikeResult(part, "Deep Hack", "Bone is reached. Arm cannot be used, 2 rounds dazed, Massive Bleeding.", dmg, false);
            case 5 -> new StrikeResult(part, "Severed Hand", "The hand is removed at the wrist. Stunned 2 rounds, Arm permanently disabled.", dmg, false);
            case 6 -> new StrikeResult(part, "Amputated", "The arm is removed at the shoulder. Incapacitated. Death from shock imminent.", dmg, true);
            default -> new StrikeResult(part, "Cut", "Arm laceration.", dmg, false);
        };
    }

    private StrikeResult getSlashingLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Calf Scratch", "Shallow cut. -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Thigh Slash", "Deep painful cut. Half move next round, Bleeding (1/round).", dmg, false);
            case 3 -> new StrikeResult(part, "Hamstrung", "The tendon is cut. Permanent half move, -2 to combat rolls, Bleeding (2/round).", dmg, false);
            case 4 -> new StrikeResult(part, "Ruptured Artery", "Blood sprays from the leg. 3 rounds dazed, Massive Bleeding (5/round).", dmg, false);
            case 5 -> new StrikeResult(part, "Severed Foot", "The foot is removed. Movement reduced to 5 feet/round, stunned 2 rounds.", dmg, false);
            case 6 -> new StrikeResult(part, "Leg Amputated", "Leg removed at the hip. Incapacitated. Rapid bleed out.", dmg, true);
            default -> new StrikeResult(part, "Cut", "Leg laceration.", dmg, false);
        };
    }

    private StrikeResult handlePiercing(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getPiercingHead(sev, dmg);
            case TORSO, ABDOMEN -> getPiercingTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getPiercingArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getPiercingLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Puncture", "Minor internal trauma.", dmg, false);
        };
    }

    private StrikeResult getPiercingHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Grazed Ear", "A shallow puncture; painful but superficial.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Jaw Puncture", "Pierced through the cheek; dazed 1 round, -1 to rolls.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Temple Strike", "Blade finds the temple; stunned 1d3 rounds, bleeding.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Eye-Shot (Partial)", "Vision obscured by blood; -4 to combat rolls, stunned 2 rounds.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Skull Puncture", "Bone is pierced; target falls unconscious for 1d6 hours.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Brain Puncture", "The weapon enters the cranial cavity; instant death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Stab", "Facial puncture.", dmg, false);
        };
    }

    private StrikeResult getPiercingTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Minor Poke", "Blade caught on a rib; minor bruising.", dmg, false);
            case 2 -> new StrikeResult(part, "Flesh Wound", "Deep puncture in the abdomen; dazed 1 round, bleeding.", dmg, false);
            case 3 -> new StrikeResult(part, "Lung Puncture", "Target gasps for air; Breathless, stunned 2 rounds, bleeding.", dmg, false);
            case 4 -> new StrikeResult(part, "Internal Hemorrhage", "Vitals are nicked; massive internal bleeding, stack -2 to rolls.", dmg, false);
            case 5 -> new StrikeResult(part, "Heart Clip", "Blade enters the chest cavity; target is incapacitated and dying.", dmg, false);
            case 6 -> new StrikeResult(part, "Heart Punctured", "The weapon pierces the heart directly; certain death.", dmg, true);
            default -> new StrikeResult(part, "Stab", "Torso puncture.", dmg, false);
        };
    }

    private StrikeResult getPiercingArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Nicked Wrist", "Sharp sting; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Bicep Puncture", "Muscle is pierced; drop hand, bleeding (1/round).", dmg, false);
            case 3 -> new StrikeResult(part, "Joint Puncture", "Blade enters the elbow; arm goes limp, stack -4 to rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Bone Marrow Strike", "Agonizing pain; stunned 3 rounds, arm cannot be used.", dmg, false);
            case 5 -> new StrikeResult(part, "Nerve Damage", "Weapon passes through the forearm; arm is permanently paralyzed.", dmg, false);
            case 6 -> new StrikeResult(part, "Main Artery Severed", "Blood sprays in a high-pressure jet; incapacitated, bleed out in 5 rounds.", dmg, false);
            default -> new StrikeResult(part, "Stab", "Arm puncture.", dmg, false);
        };
    }

    private StrikeResult getPiercingLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Glancing Prick", "Small puncture; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Muscle Pierce", "Pierced through the calf; half move next round, bleeding.", dmg, false);
            case 3 -> new StrikeResult(part, "Knee Puncture", "Blade enters the joint; permanent half move, -2 to combat rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Femur Chip", "Blade strikes bone; 3 rounds dazed, cannot perform mobility moves.", dmg, false);
            case 5 -> new StrikeResult(part, "Pinned Limb", "The weapon pierces through the leg; movement reduced to 0.", dmg, false);
            case 6 -> new StrikeResult(part, "Femoral Puncture", "Major artery hit; incapacitated, rapid bleed out.", dmg, false);
            default -> new StrikeResult(part, "Stab", "Leg puncture.", dmg, false);
        };
    }
    private StrikeResult handleRanged(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getRangedHead(sev, dmg);
            case TORSO, ABDOMEN -> getRangedTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getRangedArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getRangedLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Projectile Hit", "General ranged trauma.", dmg, false);
        };
    }

    private StrikeResult getRangedHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Grazed Temple", "Arrow nicks the side of the head; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Skull Embed", "Projectile lodged in the outer skull; dazed 1 round. Needs extraction.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Eye-Pierced", "Arrow enters the eye socket; blinded, stunned 2 rounds. Extreme healing difficulty.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Jaw Pin", "Bolt pins the jaw shut; cannot speak/cast, bleeding, -4 to rolls.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Deep Embed", "Projectile buried deep in the skull; unconscious, permanent brain damage.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Cranial Breach", "Projectile passes through the brain; instant death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Ranged Hit", "Head puncture.", dmg, false);
        };
    }

    private StrikeResult getRangedTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Glancing Arrow", "Projectile deflects off armor/bone; minor puncture.", dmg, false);
            case 2 -> new StrikeResult(part, "Shallow Lodge", "Projectile stuck in the shoulder/chest muscle; breathless, bleeding.", dmg, false);
            case 3 -> new StrikeResult(part, "Lung Puncture", "Arrow lodged in lung; breathless, stunned 2 rounds. Half healing until extracted.", dmg, false);
            case 4 -> new StrikeResult(part, "Spinal Nick", "Bolt lodged near the spine; partial paralysis, -4 to all rolls.", dmg, false);
            case 5 -> new StrikeResult(part, "Gut Puncture", "Projectile buried in the abdomen; massive internal trauma, septic risk.", dmg, false);
            case 6 -> new StrikeResult(part, "Vitals Pierced", "Projectile transfixes the heart or spine; certain death.", dmg, true);
            default -> new StrikeResult(part, "Ranged Hit", "Torso puncture.", dmg, false);
        };
    }

    private StrikeResult getRangedArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Forearm Nick", "Surface level puncture; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Muscle Lodge", "Arrow buried in the bicep; drop held item, arm cannot be used until extracted.", dmg, false);
            case 3 -> new StrikeResult(part, "Transfixed Arm", "Projectile passes through the forearm; arm disabled, major bleeding.", dmg, false);
            case 4 -> new StrikeResult(part, "Shoulder Pin", "Bolt pins the arm to the torso/armor; incapacitated, dazed 2 rounds.", dmg, false);
            case 5 -> new StrikeResult(part, "Joint Shatter", "Projectile explodes the elbow joint; arm permanently lost/paralyzed.", dmg, false);
            case 6 -> new StrikeResult(part, "Arterial Hit", "Arrow severs the brachial artery; incapacitated, bleed out in 6 rounds.", dmg, false);
            default -> new StrikeResult(part, "Ranged Hit", "Arm puncture.", dmg, false);
        };
    }

    private StrikeResult getRangedLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Shin Scrape", "Arrow nicks the bone; minor puncture.", dmg, false);
            case 2 -> new StrikeResult(part, "Calf Lodge", "Projectile stuck in muscle; half move until extracted, bleeding.", dmg, false);
            case 3 -> new StrikeResult(part, "Knee Pin", "Bolt lodged in the kneecap; permanent half move, mobility moves impossible.", dmg, false);
            case 4 -> new StrikeResult(part, "Thigh Transfix", "Arrow passes through the thigh; movement reduced to 5ft, stunned 2 rounds.", dmg, false);
            case 5 -> new StrikeResult(part, "Bone Shatter", "Femur is snapped by the impact; movement 0, bleeding (5/round).", dmg, false);
            case 6 -> new StrikeResult(part, "Femoral Piercing", "Projectile severs the main artery; rapid bleed out, incapacitated.", dmg, false);
            default -> new StrikeResult(part, "Ranged Hit", "Leg puncture.", dmg, false);
        };
    }


    private StrikeResult handleFire(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getFireHead(sev, dmg);
            case TORSO, ABDOMEN -> getFireTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getFireArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getFireLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Searing Burn", "Heat trauma.", dmg, false);
        };
    }

    private StrikeResult getFireHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Singed", "Hair and eyebrows singed; stinging pain, -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Blistered Face", "Second-degree burns; eyes watering, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Corneal Sear", "Vision obscured by heat; stunned 1 round, -4 to rolls.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Airway Scorched", "Inhaled flame; breathless, stunned 3 rounds, massive agony.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Facial Charring", "Third-degree burns; unconscious 1d4 days, permanent stat loss.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Incinerated", "Skull and brain consumed by the inferno; instant death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Burn", "Facial heat trauma.", dmg, false);
        };
    }

    private StrikeResult getFireTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Sizzling Skin", "Surface burns across the chest; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Seared Flesh", "Agonizing burns; breathless, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Internal Heat", "Severe chest burns; stunned 2 rounds, stack -2 to rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Torso Melt", "Deep tissue damage; breathless, stunned 1d3+1 rounds.", dmg, false);
            case 5 -> new StrikeResult(part, "Organ Cooking", "Massive internal heat shock; incapacitated for the encounter.", dmg, false);
            case 6 -> new StrikeResult(part, "Consumed to Ash", "The torso is engulfed; vital organs vaporized instantly.", dmg, true);
            default -> new StrikeResult(part, "Burn", "Torso heat trauma.", dmg, false);
        };
    }

    private StrikeResult getFireArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Scorched Hand", "Painful singe; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Blistered Forearm", "Second-degree burns; drop hand, bleeding (heat shock).", dmg, false);
            case 3 -> new StrikeResult(part, "Muscle Sear", "Deep tissue burns; arm goes limp, stack -4 to rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Nerve Burn", "Agonizing pain; stunned 3 rounds, arm cannot be used.", dmg, false);
            case 5 -> new StrikeResult(part, "Charred Limb", "The arm is blackened and useless; permanently disabled.", dmg, false);
            case 6 -> new StrikeResult(part, "Incinerated Arm", "The limb is reduced to charred bone; incapacitated from shock.", dmg, false);
            default -> new StrikeResult(part, "Burn", "Arm heat trauma.", dmg, false);
        };
    }

    private StrikeResult getFireLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Singed Calf", "Minor heat impact; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Blistered Thigh", "Painful burns; half move next round, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Muscle Sear", "Deep leg burns; permanent half move, -2 to combat rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Joint Charring", "The knee/ankle is seared; 3 rounds dazed, cannot perform mobility moves.", dmg, false);
            case 5 -> new StrikeResult(part, "Carbonized Limb", "The leg is blackened and brittle; movement reduced to 5ft.", dmg, false);
            case 6 -> new StrikeResult(part, "Incinerated Leg", "Limb consumed by fire; incapacitated, total movement failure.", dmg, false);
            default -> new StrikeResult(part, "Burn", "Leg heat trauma.", dmg, false);
        };
    }
    
    private StrikeResult handleCold(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getColdHead(sev, dmg);
            case TORSO, ABDOMEN -> getColdTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getColdArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getColdLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Frostbite", "Cryo-trauma.", dmg, false);
        };
    }

    private StrikeResult getColdHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Frost-nip", "Nose and ears turn white and numb; stinging pain, -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Neural Chill", "Brain freeze; migraine-like pain, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Ocular Frost", "Ice crystals form on the eyes; vision blurred, stunned 1 round.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Crystalized Sinus", "Facial tissue becomes brittle; bone-deep chill, stunned 2 rounds.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Deep Freeze", "Neural pathways slow down; unconscious/coma for 1d6 days.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Shattered Cranium", "The head freezes solid and cracks under the thermal stress; instant death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Chill", "Facial cold trauma.", dmg, false);
        };
    }

    private StrikeResult getColdTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "The Shivers", "Body temp drops slightly; uncontrolled shivering, -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Numbed Core", "Chest skin loses sensation; breathless, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Frost in Lungs", "Airway begins to freeze; breathless, stunned 2 rounds, stack -2 to rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Core Hypothermia", "Heart rate slows dangerously; 1d3+1 rounds stunned, half move.", dmg, false);
            case 5 -> new StrikeResult(part, "Internal Crystallization", "Organs begin to freeze; incapacitated for the encounter.", dmg, false);
            case 6 -> new StrikeResult(part, "Frozen Heart", "The core freezes solid; heart stops, target shatters into shards.", dmg, true);
            default -> new StrikeResult(part, "Chill", "Torso cryo-trauma.", dmg, false);
        };
    }

    private StrikeResult getColdArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Stiff Fingers", "Joints lose mobility; minor stiffness, -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Frozen Grip", "Hand loses all sensation; drop hand, stack -2 to rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Joint Freeze", "Elbow/Wrist is locked by ice; arm cannot be used for 1d4 rounds.", dmg, false);
            case 4 -> new StrikeResult(part, "Brittle Bone", "The limb becomes ice-fragile; any impact now will break it, 2 rounds dazed.", dmg, false);
            case 5 -> new StrikeResult(part, "Frost-Death", "Nerves and muscle are permanently destroyed by ice; arm is useless.", dmg, false);
            case 6 -> new StrikeResult(part, "Shattered Limb", "The arm freezes solid and snaps off at the shoulder; incapacitated from shock.", dmg, false);
            default -> new StrikeResult(part, "Chill", "Arm cryo-trauma.", dmg, false);
        };
    }

    private StrikeResult getColdLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Stiff Step", "Muscle fibers contract from cold; half move next round.", dmg, false);
            case 2 -> new StrikeResult(part, "Numbed Leg", "Loss of balance; dazed 1 round, stack -2 to combat rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Frozen Knee", "Joint ice-locked; movement reduced to 5ft, cannot perform mobility moves.", dmg, false);
            case 4 -> new StrikeResult(part, "Brittle Bone", "Leg becomes glass-fragile; next move requires a check or it snaps.", dmg, false);
            case 5 -> new StrikeResult(part, "Solid State", "The leg is frozen into a pillar of ice; movement 0, incapacitated.", dmg, false);
            case 6 -> new StrikeResult(part, "Leg Shatter", "Leg shatters into crystalline fragments; movement 0, rapid systemic shock.", dmg, false);
            default -> new StrikeResult(part, "Chill", "Leg cryo-trauma.", dmg, false);
        };
    }
    
    private StrikeResult handleAcid(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getAcidHead(sev, dmg);
            case TORSO, ABDOMEN -> getAcidTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getAcidArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getAcidLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Chemical Burn", "Caustic trauma.", dmg, false);
        };
    }

    private StrikeResult getAcidHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Sizzling Brow", "Acid splashes the forehead; skin smokes and reddens. -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Caustic Blisters", "The chemical eats into the cheek; agonizing stinging, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Ocular Melting", "Acid enters the eyes; vision is permanently clouded, stunned 1 round, -4 to rolls.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Jaw Dissolution", "The acid eats through the cheek to the bone; breathless, stunned 3 rounds.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Skull Corrosion", "The cranium is softened by the chemical; unconscious 1d6 days, permanent stat loss.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Liquefied Cranium", "The skull dissolves into a caustic slurry; instant and horrific death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Acid Burn", "Facial caustic trauma.", dmg, false);
        };
    }

    private StrikeResult getAcidTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Smoking Flesh", "Acid seeps into the chest layers; skin sizzles. -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Weeping Wound", "The chemical eats through the dermis; breathless, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Rib Exposure", "The acid melts through the muscle to the bone; stunned 2 rounds, persistent damage.", dmg, false);
            case 4 -> new StrikeResult(part, "Abdominal Breach", "The stomach wall is dissolved; internal organs are exposed and smoking.", dmg, false);
            case 5 -> new StrikeResult(part, "Core Liquefaction", "Organs begin to melt into a toxic sludge; incapacitated for the encounter.", dmg, false);
            case 6 -> new StrikeResult(part, "Dissolved Core", "The torso is hollowed out by chemical reaction; certain death.", dmg, true);
            default -> new StrikeResult(part, "Acid Burn", "Torso caustic trauma.", dmg, false);
        };
    }

    private StrikeResult getAcidArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Caustic Splash", "Minor stinging on the hand; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Deep Pitting", "Acid eats 'pits' into the forearm; drop hand, bleeding (chemical weeping).", dmg, false);
            case 3 -> new StrikeResult(part, "Muscle Melting", "The acid dissolves the bicep; arm goes limp, stack -4 to rolls.", dmg, false);
            case 4 -> new StrikeResult(part, "Joint Dissolution", "The elbow/wrist joint is melted through; 2 rounds stunned, arm unusable.", dmg, false);
            case 5 -> new StrikeResult(part, "Skeletal Exposure", "The flesh sloughs off, leaving only bone; arm is permanently disabled.", dmg, false);
            case 6 -> new StrikeResult(part, "Limb Liquefaction", "The arm melts into a slurry and falls away; incapacitated from chemical shock.", dmg, false);
            default -> new StrikeResult(part, "Acid Burn", "Arm caustic trauma.", dmg, false);
        };
    }

    private StrikeResult getAcidLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Smoking Boot", "Acid eats through the outer layer; stinging pain. -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Caustic Streak", "The acid runs down the leg; half move next round, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Knee Corrosion", "The chemical melts into the joint; permanent half move, mobility moves impossible.", dmg, false);
            case 4 -> new StrikeResult(part, "Bone Softening", "The acid reaches the femur; 3 rounds dazed, movement reduced to 5ft.", dmg, false);
            case 5 -> new StrikeResult(part, "Muscle Sloughing", "The leg muscle is dissolved away; movement 0, incapacitated.", dmg, false);
            case 6 -> new StrikeResult(part, "Leg Dissolution", "The leg is eaten away at the hip; movement 0, rapid chemical shock.", dmg, false);
            default -> new StrikeResult(part, "Acid Burn", "Leg caustic trauma.", dmg, false);
        };
    }
    
    private StrikeResult handleElectricity(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getElectricityHead(sev, dmg);
            case TORSO, ABDOMEN -> getElectricityTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getElectricityArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getElectricityLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Static Shock", "Neural trauma.", dmg, false);
        };
    }

    private StrikeResult getElectricityHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Static Jolt", "Hair stands on end; vision flashes white. -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Neural Snap", "A sharp arc to the temple; dazed 1 round, -2 to combat rolls.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Arc Flash", "Retina seared by the flash; stunned 1 round, blinded for 1d4 rounds.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Seizure", "Violent electrical discharge causes a grand mal seizure; stunned 4 rounds.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Neural Burnout", "Synapses fried; unconscious 1d6 days, permanent memory/stat loss.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Fulmination", "Brain matter is cooked by the current; instant neural death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Shock", "Facial neural trauma.", dmg, false);
        };
    }

    private StrikeResult getElectricityTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Chest Jolt", "A painful snap across the ribs; muscle twitching, -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Arrhythmia", "Heart skips a beat; breathless, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Violent Spasm", "Chest muscles lock up; target is knocked prone and stunned 2 rounds.", dmg, false);
            case 4 -> new StrikeResult(part, "Respiratory Failure", "Diaphragm paralyzes; breathless, stunned 1d3+2 rounds.", dmg, false);
            case 5 -> new StrikeResult(part, "Cardiac Trauma", "The heart stops but may be revived; incapacitated and unconscious.", dmg, false);
            case 6 -> new StrikeResult(part, "Fatal Discharge", "Current vaporizes the heart's electrical pathways; instant death.", dmg, true);
            default -> new StrikeResult(part, "Shock", "Torso neural trauma.", dmg, false);
        };
    }

    private StrikeResult getElectricityArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Finger Snap", "Current numbs the fingertips; minor stinging, -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Tetany", "Involuntary muscle contraction; drop hand, stack -2 to rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Nerve Fry", "Arm shakes uncontrollably with arc-fire; arm unusable for 1d4 rounds.", dmg, false);
            case 4 -> new StrikeResult(part, "Arc Burn", "Electrical entry/exit wounds at the elbow; 2 rounds dazed, arm disabled.", dmg, false);
            case 5 -> new StrikeResult(part, "Permanent Paralysis", "Nerve endings destroyed; arm is a dead weight, permanently disabled.", dmg, false);
            case 6 -> new StrikeResult(part, "Explosive Exit", "Internal steam pressure causes the limb to burst; incapacitated from shock.", dmg, false);
            default -> new StrikeResult(part, "Shock", "Arm neural trauma.", dmg, false);
        };
    }

    private StrikeResult getElectricityLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Leg Twitch", "Minor muscle spasm; -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Locked Joint", "Muscle tetany in the calf; half move, stack -2 to combat rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Violent Kick", "Leg kicks out uncontrollably; fall prone, stunned 1 round.", dmg, false);
            case 4 -> new StrikeResult(part, "Motor Control Loss", "Nervous system signal failure; movement reduced to 5ft, cannot dodge.", dmg, false);
            case 5 -> new StrikeResult(part, "Grounded Shock", "Leg turns black as current exits through the foot; move 0, incapacitated.", dmg, false);
            case 6 -> new StrikeResult(part, "Neural Failure", "The leg is effectively 'dead' from hip down; move 0, rapid systemic shock.", dmg, false);
            default -> new StrikeResult(part, "Shock", "Leg neural trauma.", dmg, false);
        };
    }
    
    private StrikeResult handleNegativeEnergy(HitLocation part, int sev, int dmg) {
        return switch (part) {
            case HEAD -> getNegativeEnergyHead(sev, dmg);
            case TORSO, ABDOMEN -> getNegativeEnergyTorso(part, sev, dmg);
            case LEFT_ARM, RIGHT_ARM -> getNegativeEnergyArm(part, sev, dmg);
            case LEFT_LEG, RIGHT_LEG -> getNegativeEnergyLeg(part, sev, dmg);
            default -> new StrikeResult(part, "Void Touch", "Necrotic trauma.", dmg, false);
        };
    }

    private StrikeResult getNegativeEnergyHead(int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(HitLocation.HEAD, "Hollow Gaze", "Target's eyes turn gray; a chill settles in the mind. -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(HitLocation.HEAD, "Whispers of the Void", "Target hears the voices of the dead; dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(HitLocation.HEAD, "Graying Senses", "Color drains from vision; stunned 1 round, -2 to combat rolls.", dmg, false);
            case 4 -> new StrikeResult(HitLocation.HEAD, "Mind-Wither", "The spark of life dims. FEAR EFFECT: Save or be Cowered. Stunned 4 rounds.", dmg, false);
            case 5 -> new StrikeResult(HitLocation.HEAD, "Spirit Fracture", "The soul begins to detach. FEAR EFFECT: Save or be Cowered. Unconscious 1d6 days.", dmg, false);
            case 6 -> new StrikeResult(HitLocation.HEAD, "Soul Extinguished", "The light in the eyes goes out instantly. Body remains standing but is empty; instant death.", dmg, true);
            default -> new StrikeResult(HitLocation.HEAD, "Necrosis", "Facial necrotic trauma.", dmg, false);
        };
    }

    private StrikeResult getNegativeEnergyTorso(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Chill of the Grave", "Internal organs feel frozen. -1 to combat rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Heart-Fade", "The heartbeat slows to a crawl; breathless, dazed 1 round.", dmg, false);
            case 3 -> new StrikeResult(part, "Necrotic Patch", "Flesh turns gray and sloughs off; max HP reduced, stunned 2 rounds.", dmg, false);
            case 4 -> new StrikeResult(part, "Rotting Core", "Vitality is consumed from within. FEAR EFFECT: Save or be Cowered. Stunned 1d3+2 rounds.", dmg, false);
            case 5 -> new StrikeResult(part, "Void Hollow", "A dark cavity opens where the heart was. FEAR EFFECT: Save or be Cowered. Incapacitated.", dmg, false);
            case 6 -> new StrikeResult(part, "Life Deleted", "The target's life force is sucked into the void. Certain death.", dmg, true);
            default -> new StrikeResult(part, "Necrosis", "Torso necrotic trauma.", dmg, false);
        };
    }

    private StrikeResult getNegativeEnergyArm(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Cold Numbness", "Fingertips turn black; minor stiffness, -1 to rolls.", dmg, false);
            case 2 -> new StrikeResult(part, "Atrophy", "Muscle mass visibly shrinks; drop hand, stack -2 to rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Muscle Decay", "Flesh rots away from the bone; arm unusable for 1d6 rounds.", dmg, false);
            case 4 -> new StrikeResult(part, "Skeletal Exposure", "Muscle is gone, leaving only bone. FEAR EFFECT: Save or be Cowered. Arm disabled.", dmg, false);
            case 5 -> new StrikeResult(part, "Withered Limb", "The arm becomes a mummified husk. FEAR EFFECT: Save or be Cowered. Permanently useless.", dmg, false);
            case 6 -> new StrikeResult(part, "Disintegrated Arm", "The arm turns to fine gray dust and vanishes; incapacitated from shock.", dmg, false);
            default -> new StrikeResult(part, "Necrosis", "Arm necrotic trauma.", dmg, false);
        };
    }

    private StrikeResult getNegativeEnergyLeg(HitLocation part, int sev, int dmg) {
        return switch (sev) {
            case 1 -> new StrikeResult(part, "Shuffling Gait", "Leg feels heavy and lifeless; half move next round.", dmg, false);
            case 2 -> new StrikeResult(part, "Bone-Deep Cold", "The marrow feels frozen; dazed 1 round, stack -2 to combat rolls.", dmg, false);
            case 3 -> new StrikeResult(part, "Necrotic Joint", "Knee/Ankle turns gray and locks; permanent half move.", dmg, false);
            case 4 -> new StrikeResult(part, "Muscle Sloughing", "Flesh falls away in wet clumps. FEAR EFFECT: Save or be Cowered. Movement reduced to 5ft.", dmg, false);
            case 5 -> new StrikeResult(part, "Grave-Bound", "The leg is a withered branch. FEAR EFFECT: Save or be Cowered. Movement 0, incapacitated.", dmg, false);
            case 6 -> new StrikeResult(part, "Leg Disintegrated", "The leg vanishes into a cloud of shadow; movement 0, rapid systemic failure.", dmg, false);
            default -> new StrikeResult(part, "Necrosis", "Leg necrotic trauma.", dmg, false);
        };
    }
    
}