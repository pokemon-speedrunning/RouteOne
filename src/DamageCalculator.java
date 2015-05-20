//calculates damage (durr)
import java.util.TreeMap;

public class DamageCalculator {
    private static int MIN_RANGE = 217;
    private static int MAX_RANGE = 255;
    //rangeNum should range from 217 to 255 
    //crit indicates if there is a crit or not
    private static int damage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod, int rangeNum, boolean crit) {
        if (rangeNum < MIN_RANGE) {
            rangeNum = MIN_RANGE;
        }
        if (rangeNum > MAX_RANGE) {
            rangeNum = MAX_RANGE;
        }
        
        if(attack.getPower() <= 0) {
            //TODO: special cases
            return 0;
        }
        //stat modifiers
        int aa_orig = attacker.getTrueAtk();
        int atk_atk = atkMod.modAtk(attacker);
        int dd_orig = defender.getTrueDef();
        int def_def = defMod.modDef(defender);
        int as_orig = attacker.getTrueSpc();
        int atk_spc = atkMod.modSpc(attacker);
        int ds_orig = defender.getTrueSpc();
        int def_spc = defMod.modSpc(defender);
        
        boolean STAB = attack.getType() == attacker.getSpecies().getType1()
				|| attack.getType() == attacker.getSpecies().getType2();
		int lev = (attacker.getLevel() * (crit ? 2 : 1)) % 256;
		if (Type.isPhysicalType(attack.getType())) {
			int a = (lev * 2 / 5 + 2);
			a *= crit ? aa_orig : atk_atk;
			a *= attack.getPower();
			a /= 50;
			a /= crit ? dd_orig : def_def;
			a += 2;
			a = STAB ? a * 3 / 2 : a;
			a = Type.applyTypeEffectiveness(a, attack.getType(), defender.getSpecies().getType1(), defender
					.getSpecies().getType2());
			// if damage is 0 after type effectiveness, the move misses
			// this covers immunities and the weird 4x NVE miss
			if (a == 0) {
				return 0;
			}
			a *= rangeNum;
			a /= 255;
			return Math.max(a, 1);
		} else {
			int a = (lev * 2 / 5 + 2);
			a *= crit ? as_orig : atk_spc;
			a *= attack.getPower();
			a /= 50;
			a /= crit ? ds_orig : def_spc;
			a += 2;
			a = STAB ? a * 3 / 2 : a;
			a = Type.applyTypeEffectiveness(a, attack.getType(), defender.getSpecies().getType1(), defender
					.getSpecies().getType2());
			// if damage is 0 after type effectiveness, the move misses
			// this covers immunities and the weird 4x NVE miss
			if (a == 0) {
				return 0;
			}
			a *= rangeNum;
			a /= 255;
			return Math.max(a, 1);
		}
            
    }
    public static int minDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, MIN_RANGE, false);
    }
    public static int maxDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, MAX_RANGE, false);
    }
    public static int minCritDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, MIN_RANGE, true);
    }
    public static int maxCritDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, MAX_RANGE, true);
    }
    
    //printout of move damages between the two pokemon
    //assumes you are p1
    public static String summary(Pokemon p1, Pokemon p2, BattleOptions options) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        StatModifier mod1 = options.getMod1();
        StatModifier mod2 = options.getMod2();
        
		sb.append(p1.levelName() + " vs " + p2.levelName() + "          >>> EXP GIVEN: " + p2.expGiven(1) + endl);
        //sb.append(String.format("EXP to next level: %d EXP gained: %d", p1.expToNextLevel(), p2.expGiven()) + endl);
        sb.append(String.format("%s (%s) ", p1.pokeName(), p1.statsStr()));       
        if(mod1.hasMods() || mod1.hasBBs()) {
            sb.append(String.format("%s -> (%s) ", mod1.summary(), mod1.modStatsStr(p1)) + endl);    
        }  else {
            sb.append(endl);
        }
        
        sb.append(summary_help(p1,p2,mod1,mod2));
        
        sb.append(endl);
        
        if(options.getVerbose() == BattleOptions.EVERYTHING)
        {
	
	       	for(Move move : p1.getMoveset())
	    	{
	       		int minDmg = Math.min(p2.getHP(), minDamage(move, p1, p2, mod1, mod2));
	       		if(minDmg > 0)
	       		{
		       		int minCritDmg = Math.min(p2.getHP(), minCritDamage(move, p1, p2, mod1, mod2));
		        	TreeMap<Integer,Double> dmgMap = detailedDamage(move, p1, p2, mod1, mod2, false);
		        	TreeMap<Integer,Double> critMap = detailedDamage(move, p1, p2, mod1, mod2, true);
		//        	sb.append(String.format("%s (%s) ", p1.pokeName(), p1.statsStr()));
		        	sb.append(move.getName());
		        	sb.append(endl);
		        	sb.append("          NON-CRITS");
		        	for(Integer i : dmgMap.keySet())
		        	{
		        		if((i - minDmg) % 7 == 0)
		        		{
		        			sb.append(endl);
		        			if(i.intValue() == p2.getHP() && minDmg != p2.getHP())
		        			{
		        				sb.append(endl);
		        			}
		        			sb.append("            ");
		        		}
		        		else if(i.intValue() == p2.getHP() && minDmg != p2.getHP())
		        		{
		        			sb.append(endl);
		        			sb.append(endl);
		        			sb.append("            ");		        			
		        		}
		        		
		        		sb.append(String.format("%3d: %6.02f%%     ", i, dmgMap.get(i)));
		        	}
		        	sb.append(endl);
		        	sb.append(endl);
		        	sb.append("          CRITS");
		        	for(Integer i : critMap.keySet())
		        	{
		        		if((i - minCritDmg) % 7 == 0)
		        		{
		        			sb.append(endl);
		        			if(i.intValue() == p2.getHP() && minCritDmg != p2.getHP())
		        			{
		        				sb.append(endl);
		        			}
		        			sb.append("            ");
		        		}
		        		else if(i.intValue() == p2.getHP() && minCritDmg != p2.getHP())
		        		{
		        			sb.append(endl);
		        			sb.append(endl);
		        			sb.append("            ");		        			
		        		}

		        		sb.append(String.format("%3d: %6.02f%%     ", i, critMap.get(i)));
		        	}
		        	sb.append(endl);  
		        	sb.append(endl);
	      		}
	    	}
        }

        if(mod2.hasMods()) {
        sb.append(String.format("%s (%s) %s -> (%s): ", p2.pokeName(), p2.statsStr(),
                mod2.summary(), mod2.modStatsStr(p2)) + endl);
        } else {
            sb.append(String.format("%s (%s): ", p2.pokeName(), p2.statsStr()) + endl);
        }
        sb.append(summary_help(p2,p1,mod2,mod1));
        
        if(options.getVerbose() == BattleOptions.EVERYTHING)
        {        	       	
            sb.append(endl);
	        for(Move move : p2.getMoveset())
	    	{
	       		int minDmg = Math.min(p1.getHP(), minDamage(move, p2, p1, mod2, mod1));
	       		if(minDmg > 0)
	       		{
		       		int minCritDmg = Math.min(p1.getHP(), minCritDamage(move, p2, p1, mod2, mod1));
		        	TreeMap<Integer,Double> dmgMap = detailedDamage(move, p2, p1, mod2, mod1, false);
		        	TreeMap<Integer,Double> critMap = detailedDamage(move, p2, p1, mod2, mod1, true);
		//        	sb.append(String.format("%s (%s) ", p1.pokeName(), p1.statsStr()));
		        	sb.append(move.getName());
		        	sb.append(endl);
		        	sb.append("          NON-CRITS");
		        	for(Integer i : dmgMap.keySet())
		        	{
		        		if((i - minDmg) % 7 == 0)
		        		{
		        			sb.append(endl);
		        			if(i.intValue() == p1.getHP() && minDmg != p1.getHP())
		        			{
		        				sb.append(endl);
		        			}
		        			sb.append("            ");
		        		}
		        		else if(i.intValue() == p1.getHP() && minDmg != p1.getHP())
		        		{
		        			sb.append(endl);
		        			sb.append(endl);
		        			sb.append("            ");		        			
		        		}
		        		
		        		sb.append(String.format("%3d: %6.02f%%     ", i, dmgMap.get(i)));
		        	}
		        	sb.append(endl);
		        	sb.append(endl);
		        	sb.append("          CRITS");
		        	for(Integer i : critMap.keySet())
		        	{
		        		if((i - minCritDmg) % 7 == 0)
		        		{
		        			sb.append(endl);
		        			if(i.intValue() == p1.getHP() && minCritDmg != p1.getHP())
		        			{
		        				sb.append(endl);
		        			}
		        			sb.append("            ");
		        		}
		        		else if(i.intValue() == p1.getHP() && minCritDmg != p1.getHP())
		        		{
		        			sb.append(endl);
		        			sb.append(endl);
		        			sb.append("            ");		        			
		        		}

		        		sb.append(String.format("%3d: %6.02f%%     ", i, critMap.get(i)));
		        	}
		        	sb.append(endl);  
		        	sb.append(endl);
	      		}

	    	}
        }         
        
        return sb.toString();
    }
    
    private static TreeMap<Integer,Double> detailedDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod, boolean crit)
    {
       	TreeMap<Integer,Double> dmgMap = new TreeMap<Integer,Double>();
        for(int i=MIN_RANGE; i<=MAX_RANGE; i++)
        {
        	int dmg = Math.min(defender.getHP(), damage(attack, attacker, defender, atkMod, defMod, i, crit));
        	if(dmgMap.containsKey(dmg))
        	{
        		dmgMap.put(dmg,100.0/((double)(MAX_RANGE-MIN_RANGE+1))+dmgMap.get(dmg));
        	}
        	else
        	{
        		dmgMap.put(dmg,100.0/((double)(MAX_RANGE-MIN_RANGE+1)));
        	}
        }
     	return dmgMap;
    }
    
    //String summary of all of p1's moves used on p2
    //(would be faster if i didn't return intermediate strings)
    private static String summary_help(Pokemon p1, Pokemon p2, StatModifier mod1, StatModifier mod2) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        
        int enemyHP = p2.getHP();
        
        for(Move m : p1.getMoveset()) {
            sb.append(m.getName() + "\t");
            //calculate damage of this move, and its percentages on opposing pokemon
            int minDmg = minDamage(m, p1, p2, mod1, mod2);
            int maxDmg = maxDamage(m, p1, p2, mod1, mod2);
            int critMinDmg = minCritDamage(m, p1, p2, mod1, mod2);
			int critMaxDmg = maxCritDamage(m, p1, p2, mod1, mod2);
            
            //don't spam if the move doesn't do damage
            //TODO: better test of damaging move, to be done when fixes are made
            if (maxDmg == 0 && critMaxDmg == 0) {
                sb.append(endl);
                continue;
            }
            double minPct = 100.0 * minDmg / enemyHP;
            double maxPct = 100.0 * maxDmg / enemyHP;
            if (maxDmg != 0) {
				sb.append(String.format("%d-%d %.02f-%.02f%%", minDmg, maxDmg, minPct, maxPct));
			} else {
				sb.append("low dmg miss");
			}
            sb.append("\t(crit: ");
            
            //do it again, for crits
            double critMinPct = 100.0 * critMinDmg / enemyHP;
            double critMaxPct = 100.0 * critMaxDmg / enemyHP;
            sb.append(String.format("%d-%d %.02f-%.02f",critMinDmg,critMaxDmg,critMinPct,critMaxPct));
            sb.append("%)" + endl);
            
            int oppHP = p2.getHP();
            //test if noncrits can kill in 1shot
            if(maxDmg >= oppHP && minDmg < oppHP) {
                double oneShotPct = oneShotPercentage(m, p1, p2, mod1, mod2, false);
                sb.append(String.format("\t(One shot prob.: %.02f%%)",oneShotPct) + endl);
            }
            //test if crits can kill in 1shot
            if(critMaxDmg >= oppHP && critMinDmg < oppHP) {
                double oneShotPct = oneShotPercentage(m, p1, p2, mod1, mod2, true);
                sb.append(String.format("\t(Crit one shot prob.: %.02f%%)",oneShotPct) + endl);
            }
            
            // n-shot
            int minDmgWork = minDmg;
			int maxDmgWork = maxDmg;
			int hits = 1;
			while (minDmgWork < oppHP && hits < 10) {
				hits++;
				minDmgWork += minDmg;
				maxDmgWork += maxDmg;
				if (maxDmgWork >= oppHP && minDmgWork < oppHP) {
					System.out.println("working out a " + hits + "-shot");
					double nShotPct = nShotPercentage(m, p1, p2, mod1, mod2, hits, 0);
					sb.append(String.format("\t(%d shot prob.: %.04f%%)", hits, nShotPct) + endl);
				}
			}

			// n-crit-shot
			minDmgWork = critMinDmg;
			maxDmgWork = critMaxDmg;
			hits = 1;
			while (minDmgWork < oppHP && hits < 10) {
				hits++;
				minDmgWork += critMinDmg;
				maxDmgWork += critMaxDmg;
				if (maxDmgWork >= oppHP && minDmgWork < oppHP) {
					System.out.println("working out a " + hits + "-crit-shot");
					double nShotPct = nShotPercentage(m, p1, p2, mod1, mod2, 0, hits);
					sb.append(String.format("\t(%d crits death prob.: %.04f%%)", hits, nShotPct) + endl);
				}
			}

			// mixed a-noncrit and b-crit shot
			int realminDmg = Math.min(minDmg, critMinDmg);
			for (int non = 1; non <= 5 && realminDmg * (non + 1) < oppHP; non++) {
				for (int crit = 1; crit <= 5 && realminDmg * (non + crit) < oppHP; crit++) {
					int sumMin = critMinDmg * crit + minDmg * non;
					int sumMax = critMaxDmg * crit + maxDmg * non;
					if (sumMin < oppHP && sumMax >= oppHP) {
						System.out.printf("working out %d non-crits + %d crits\n", non, crit);
						double nShotPct = nShotPercentage(m, p1, p2, mod1, mod2, non, crit);
						sb.append(String.format("\t(%d non-crit%s + %d crit%s death prob.: %.04f%%)", non,
								non > 1 ? "s" : "", crit, crit > 1 ? "s" : "", nShotPct)
								+ endl);
					}
				}
			}
            
        }
        
        return sb.toString();
    }
    
    //used for the less verbose option
    public static String shortSummary(Pokemon p1, Pokemon p2, BattleOptions options) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        
        StatModifier mod1 = options.getMod1();
        StatModifier mod2 = options.getMod2();
        
        sb.append(p1.levelName() + " vs " + p2.levelName() + endl);
        //sb.append(String.format("EXP to next level: %d EXP gained: %d", p1.expToNextLevel(), p2.expGiven()) + endl);
        sb.append(String.format("%s (%s) ", p1.pokeName(), p1.statsStr()));       
        if(mod1.hasMods() || mod1.hasBBs()) {
            sb.append(String.format("%s -> (%s) ", mod1.summary(), mod1.modStatsStr(p1)) + endl);    
        }  else {
            sb.append(endl);
        }
        
        sb.append(summary_help(p1,p2,mod1,mod2) + endl);
        if(mod2.hasMods()) {
            sb.append(String.format("%s (%s) %s -> (%s): ", p2.pokeName(), p2.statsStr(),
                    mod2.summary(), mod2.modStatsStr(p2)));
            } else {
                sb.append(String.format("%s (%s): ", p2.pokeName(), p2.statsStr()));
            }
            
        sb.append(" " + p2.getMoveset().toString() + endl);
        return sb.toString();
    }
    
    private static double oneShotPercentage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod, boolean crit) {
        //iterate until damage is big enough
        int rangeNum = MIN_RANGE;
        while(damage(attack, attacker, defender, atkMod, defMod, rangeNum, crit) < defender.getHP()) {
            rangeNum++;
        }
        return 100.0 * (MAX_RANGE - rangeNum + 1) / (MAX_RANGE - MIN_RANGE + 1);
    }
    
    private static double nShotPercentage(Move attack, Pokemon attacker, Pokemon defender, StatModifier atkMod,
			StatModifier defMod, int numHitsNonCrit, int numHitsCrit) {
		int rawHitDamageNC = damage(attack, attacker, defender, atkMod, defMod, MAX_RANGE, false);
		int minDamageNC = rawHitDamageNC * MIN_RANGE / 255;
		int[] probsNC = new int[rawHitDamageNC - minDamageNC + 1];
		for (int i = MIN_RANGE; i <= MAX_RANGE; i++) {
			int dmg = rawHitDamageNC * i / 255;
			probsNC[dmg - minDamageNC]++;
		}
		int rawHitDamageCR = damage(attack, attacker, defender, atkMod, defMod, MAX_RANGE, true);
		int minDamageCR = rawHitDamageCR * MIN_RANGE / 255;
		int[] probsCR = new int[rawHitDamageCR - minDamageCR + 1];
		for (int i = MIN_RANGE; i <= MAX_RANGE; i++) {
			int dmg = rawHitDamageCR * i / 255;
			probsCR[dmg - minDamageCR]++;
		}
		double chances = 0;
		int rawHP = defender.getHP();
		if (numHitsNonCrit > 0) {
			for (int i = minDamageNC; i <= rawHitDamageNC; i++) {
				chances += nShotPctInner(minDamageNC, rawHitDamageNC, minDamageCR, rawHitDamageCR, rawHP, 0, i,
						numHitsNonCrit, numHitsCrit, probsNC, probsCR);
			}
		} else {
			for (int i = minDamageCR; i <= rawHitDamageCR; i++) {
				chances += nShotPctInner(minDamageNC, rawHitDamageNC, minDamageCR, rawHitDamageCR, rawHP, 0, i,
						numHitsNonCrit, numHitsCrit, probsNC, probsCR);
			}
		}
		return 100.0 * chances / Math.pow(MAX_RANGE - MIN_RANGE + 1, numHitsNonCrit + numHitsCrit);
	}

	private static double nShotPctInner(int minDamageNC, int maxDamageNC, int minDamageCR, int maxDamageCR, int hp,
			int stackedDmg, int rolledDamage, int hitsLeftNonCrit, int hitsLeftCrit, int[] probsNC, int[] probsCR) {
		boolean wasCritical = false;
		if (hitsLeftNonCrit > 0) {
			hitsLeftNonCrit--;
		} else {
			hitsLeftCrit--;
			wasCritical = true;
		}
		stackedDmg += rolledDamage;
		if (stackedDmg >= hp || (stackedDmg + hitsLeftNonCrit * minDamageNC + hitsLeftCrit * minDamageCR) >= hp) {
			return Math.pow(MAX_RANGE - MIN_RANGE + 1, hitsLeftNonCrit + hitsLeftCrit)
					* (wasCritical ? probsCR[rolledDamage - minDamageCR] : probsNC[rolledDamage - minDamageNC]);
		} else if (hitsLeftNonCrit == 0 && hitsLeftCrit == 0) {
			return 0;
		} else if (stackedDmg + hitsLeftNonCrit * maxDamageNC + hitsLeftCrit * maxDamageCR < hp) {
			return 0;
		} else {
			double chances = 0;
			if (hitsLeftNonCrit > 0) {
				for (int i = minDamageNC; i <= maxDamageNC; i++) {
					chances += nShotPctInner(minDamageNC, maxDamageNC, minDamageCR, maxDamageCR, hp, stackedDmg, i,
							hitsLeftNonCrit, hitsLeftCrit, probsNC, probsCR);
				}
			} else {
				for (int i = minDamageCR; i <= maxDamageCR; i++) {
					chances += nShotPctInner(minDamageNC, maxDamageNC, minDamageCR, maxDamageCR, hp, stackedDmg, i,
							hitsLeftNonCrit, hitsLeftCrit, probsNC, probsCR);
				}
			}
			return chances * (wasCritical ? probsCR[rolledDamage - minDamageCR] : probsNC[rolledDamage - minDamageNC]);
		}
	}
}
