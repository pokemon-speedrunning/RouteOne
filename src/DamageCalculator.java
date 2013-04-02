//calculates damage (durr)
public class DamageCalculator {
    private static int MIN_RANGE = 217;
    private static int MAX_RANGE = 255;
    //min is true for calculating min damage, false for max
    //crit indicates if there is a crit or not
    private static int damage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod, boolean min, boolean crit) {
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
        
        boolean STAB = attack.getType() == attacker.getSpecies().getType1() || 
                    attack.getType() == attacker.getSpecies().getType2();
        double effectiveMult = Type.effectiveness(attack.getType(), defender.getSpecies().getType1(),
             defender.getSpecies().getType2());
     
        if(Type.isPhysicalType(attack.getType())) {
            return (int) (((int)((attacker.getLevel() * 0.4 * (crit ? 2 : 1)) + 2) * (crit ? aa_orig : atk_atk) * 
                   attack.getPower() / 50 / (crit ? dd_orig : def_def) + 2) *
                   (STAB ? 1.5 : 1) * effectiveMult) * (min ? MIN_RANGE : MAX_RANGE) / 255;                   
        } else {
            return (int) (((int)((attacker.getLevel() * 0.4 * (crit ? 2 : 1)) + 2) * (crit ? as_orig : atk_spc) * 
                   attack.getPower() / 50 / (crit ? ds_orig : def_spc) + 2) *
                   (STAB ? 1.5 : 1) * effectiveMult) * (min ? MIN_RANGE : MAX_RANGE) / 255;              
        }
            
    }
    public static int minDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, true, false);
    }
    public static int maxDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, false, false);
    }
    public static int minCritDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, true, true);
    }
    public static int maxCritDamage(Move attack, Pokemon attacker, Pokemon defender,
            StatModifier atkMod, StatModifier defMod) {
        return damage(attack, attacker, defender, atkMod, defMod, false, true);
    }
    
    //printout of move damages between the two pokemon
    //assumes you are p1
    public static String summary(Pokemon p1, Pokemon p2,
            StatModifier mod1, StatModifier mod2) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        
        sb.append(p1.levelName() + " vs " + p2.levelName() + endl);
        //sb.append(String.format("EXP to next level: %d EXP gained: %d", p1.expToNextLevel(), p2.expGiven()) + endl);
        if(mod1.hasMods()) {
            sb.append(String.format("%s (%s) %s -> (%s): ", p1.pokeName(), p1.statsStr(),
                    mod1.summary(), mod1.modSummary(p1)) + endl);
        } else {
            sb.append(String.format("%s (%s): ", p1.pokeName(), p1.statsStr()) + endl);
        }
        
        sb.append(summary_help(p1,p2,mod1,mod2));
        
        sb.append(endl);
        
        if(mod2.hasMods()) {
        sb.append(String.format("%s (%s) %s -> (%s): ", p2.pokeName(), p2.statsStr(),
                mod2.summary(), mod2.modSummary(p2)) + endl);
        } else {
            sb.append(String.format("%s (%s): ", p2.pokeName(), p2.statsStr()) + endl);
        }
        sb.append(summary_help(p2,p1,mod2,mod1));
        
        return sb.toString();
    }
    
    //String summary of all of p1's moves used on p2
    //(would be faster if i didn't return intermediate strings)
    private static String summary_help(Pokemon p1, Pokemon p2,
            StatModifier mod1, StatModifier mod2) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        
        int enemyHP = p2.getHP();
        
        for(Move m : p1.getMoveset()) {
            sb.append(m.getName() + "\t");
            //calculate damage of this move, and its percentages on opposing pokemon
            int minDmg = minDamage(m, p1, p2, mod1, mod2);
            int maxDmg = maxDamage(m, p1, p2, mod1, mod2);
            
            //don't spam if the move doesn't do damage
            //TODO: better test of damaging move, to be done when fixes are made
            if (maxDmg == 0) {
                sb.append(endl);
                continue;
            }
            double minPct = 100.0 * minDmg / enemyHP;
            double maxPct = 100.0 * maxDmg / enemyHP;
            sb.append(String.format("%d-%d %.02f-%.02f",minDmg,maxDmg,minPct,maxPct));
            sb.append("%\t(crit: ");
            //do it again, for crits
            minDmg = minCritDamage(m, p1, p2, mod1, mod2);
            maxDmg = maxCritDamage(m, p1, p2, mod1, mod2);
            
            minPct = 100.0 * minDmg / enemyHP;
            maxPct = 100.0 * maxDmg / enemyHP;
            sb.append(String.format("%d-%d %.02f-%.02f",minDmg,maxDmg,minPct,maxPct));
            sb.append("%)" + endl);
            
        }
        
        return sb.toString();
    }
    
    //used for the less verbose option
    public static String shortSummary(Pokemon p1, Pokemon p2,
            StatModifier mod1, StatModifier mod2) {
        StringBuilder sb = new StringBuilder();
        String endl = Constants.endl;
        
        sb.append(p1.levelName() + " vs " + p2.levelName() + endl);
        //sb.append(String.format("EXP to next level: %d EXP gained: %d", p1.expToNextLevel(), p2.expGiven()) + endl);
        if(mod1.hasMods()) {
            sb.append(String.format("%s (%s) %s -> (%s): ", p1.pokeName(), p1.statsStr(),
                    mod1.summary(), mod1.modSummary(p1)) + endl);
        } else {
            sb.append(String.format("%s (%s): ", p1.pokeName(), p1.statsStr()) + endl);
        }
        
        sb.append(summary_help(p1,p2,mod1,mod2) + endl);
        if(mod2.hasMods()) {
            sb.append(String.format("%s (%s) %s -> (%s): ", p2.pokeName(), p2.statsStr(),
                    mod2.summary(), mod2.modSummary(p2)));
            } else {
                sb.append(String.format("%s (%s): ", p2.pokeName(), p2.statsStr()));
            }
            
        sb.append(" " + p2.getMoveset().toString() + endl);
        return sb.toString();
    }
}
