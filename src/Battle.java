//represents a battle, with planned statmods
public class Battle extends GameAction {
    private Battleable opponent;
    private BattleOptions options;
    
    
    public Battle(Battleable b) {
        opponent = b;
        options = new BattleOptions();
    }
    public Battle(Battleable b, BattleOptions options) {
        opponent = b;
        this.options = options;
    }
    
    public BattleOptions getOptions() {
        return options;
    }
    public StatModifier getMod1() {
        return options.getMod1();
    }
    public StatModifier getMod2() {
        return options.getMod2();
    }
    public int getVerbose() {
        return options.getVerbose();
    }
    
    public static Battle makeBattle(int offset) {
        return new Battle(Trainer.getTrainer(offset));
    }
    public static Battle makeBattle(int offset, BattleOptions options) {
        return new Battle(Trainer.getTrainer(offset), options);
    }
    public static Battle makeBattle(Pokemon p) {
        return new Battle(p);
    }
    public static Battle makeBattle(Pokemon p, BattleOptions options) {
        return new Battle(p, options);
    }
    
    @Override
    public void performAction(Pokemon p, Inventory inv) {
        doBattle(p);
        inv.addMoney(opponent.prizeMoney());
        // Use appropriate xitems
        boolean xacc = options.getMod1().getUsedXAcc();
        int xatk = options.getMod1().getAtkStage();
        int xdef = options.getMod1().getDefStage();
        int xspd = options.getMod1().getSpdStage();
        int xspc = options.getMod1().getSpcStage();
        if(xacc) inv.removeItem("XAccuracy");
        inv.removeItem("XAttack", xatk);
        inv.removeItem("XDefend", xdef);
        inv.removeItem("XSpeed", xspd);
        inv.removeItem("XSpecial", xspc);
        
        //check for special gym leader badges
        if(Settings.dataVersion.equals("blue")) {
            if (Trainer.getTrainer(0x3A3B5).equals(opponent)) { //brock boulder badge
                p.setAtkBadge(true);
                inv.addItem("TM34");
            } else if (Trainer.getTrainer(0x3A3BB).equals(opponent)) { // misty
                inv.addItem("TM11");
            } else if (Trainer.getTrainer(0x3A3C1).equals(opponent)) { //surge thunder badge
                p.setDefBadge(true);
                inv.addItem("TM24");
            } else if (Trainer.getTrainer(0x3A3C9).equals(opponent)) { // erika
                inv.addItem("TM21");
            } else if (Trainer.getTrainer(0x3A3D1).equals(opponent)
            			|| Trainer.getTrainer(0x3A3D2).equals(opponent)) { //koga soul badge
                p.setSpdBadge(true);
                inv.addItem("TM06");
            } else if (Trainer.getTrainer(0x3A3E5).equals(opponent)) { // sabrina
                inv.addItem("TM46");
            } else if (Trainer.getTrainer(0x3A3DB).equals(opponent)) { //blaine volcano badge
                p.setSpcBadge(true);
                inv.addItem("TM38");
            } else if (Trainer.getTrainer(0x3A290).equals(opponent)) {
                inv.addItem("TM27");
            }
        } else if(Settings.dataVersion.equals("yellow")) {
            if (Trainer.getTrainer(0x3A454).equals(opponent)) { //brock boulder badge
                p.setAtkBadge(true);
                inv.addItem("TM34");
            } else if (Trainer.getTrainer(0x3A45A).equals(opponent)) { //misty
                inv.addItem("TM11");
            } else if (Trainer.getTrainer(0x3A460).equals(opponent)) { //surge thunder badge
                p.setDefBadge(true);
                inv.addItem("TM24");
            } else if (Trainer.getTrainer(0x3A464).equals(opponent)) { //erika
                inv.addItem("TM21");
            } else if (Trainer.getTrainer(0x3A46C).equals(opponent)) { //koga soul badge
                p.setSpdBadge(true);
                inv.addItem("TM06");
            } else if (Trainer.getTrainer(0x3A47E).equals(opponent)) { //sabrina
                inv.addItem("TM46");
            } else if (Trainer.getTrainer(0x3A476).equals(opponent)) { //blaine volcano badge
                p.setSpcBadge(true);
                inv.addItem("TM38");
            } else if (Trainer.getTrainer(0x3A30F).equals(opponent)) { //giovanni
                inv.addItem("TM27");
            }
        }
    }
    
    private void doBattle(Pokemon p) {
        //TODO: automatically determine whether or not to print
        if (opponent instanceof Pokemon) {
            if(getVerbose() == BattleOptions.ALL || getVerbose() == BattleOptions.EVERYTHING) printBattle(p, (Pokemon) opponent);
            else if (getVerbose() == BattleOptions.SOME) printShortBattle(p, (Pokemon) opponent);
            
            opponent.battle(p, options, 0);
            Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                    p.expToNextLevel(), p.expForLevel()));

        } else { //is a Trainer
            Trainer t = (Trainer) opponent;
            if(getVerbose() == BattleOptions.ALL || getVerbose() == BattleOptions.SOME || getVerbose() == BattleOptions.EVERYTHING) Main.appendln(t.toString());
            int lastLvl = p.getLevel();
            int idx = 0;
            for(Pokemon opps : t) {
                if(getVerbose() == BattleOptions.ALL || getVerbose() == BattleOptions.EVERYTHING) printBattle(p, (Pokemon) opps);
                else if (getVerbose() == BattleOptions.SOME) printShortBattle(p, (Pokemon) opps);
                if (getVerbose() != BattleOptions.NONE) {
					if (options.getMod1().modSpdWithIV(p, 0) <= options.getMod2().modSpd(opps)
							&& options.getMod1().modSpdWithIV(p, 15) >= options.getMod2().modSpd(opps)) {
						int tieDV = 16, outspeedDV = 16;
						int oppSpd = options.getMod2().modSpd(opps);
						for (int sDV = 0; sDV < 16; sDV++) {
							int mySpd = options.getMod1().modSpdWithIV(p, sDV);
							if (mySpd == oppSpd && sDV < tieDV) {
								tieDV = sDV;
							}
							if (mySpd > oppSpd && sDV < outspeedDV) {
								outspeedDV = sDV;
								break;
							}
						}
						Main.append("(Speed DV required");
						if (tieDV != 16 && outspeedDV != 16 && (tieDV != outspeedDV)) {
							Main.append(" to outspeed: " + outspeedDV + ", to speedtie: " + tieDV);
						} else if (outspeedDV != 16) {
							Main.append(" to outspeed: " + outspeedDV);
						} else {
							Main.append(" to speedtie: " + tieDV);
						}
						Main.appendln(")");
						Main.appendln("");
					}
				}
                opps.battle(p, options, idx++);
                //test if you leveled up on this pokemon
                if(p.getLevel() > lastLvl) {
                    lastLvl = p.getLevel();
                    if(options.isPrintSRsOnLvl()) {
                        Main.appendln(p.statRanges(false));
                    }
                    if(options.isPrintSRsBoostOnLvl()) {
                        Main.appendln(p.statRanges(true));
                    }
                }
                Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                        p.expToNextLevel(), p.expForLevel()));
            }
        }

    }
    
    //does not actually do the battle, just prints summary
    public void printBattle(Pokemon us, Pokemon them) {
        Main.appendln(DamageCalculator.summary(us, them, options));
    }
    
    //does not actually do the battle, just prints short summary
    public void printShortBattle(Pokemon us, Pokemon them) {
        Main.appendln(DamageCalculator.shortSummary(us, them, options));
    }
}

class Encounter extends Battle {
    Encounter(Species s, int lvl, BattleOptions options) {
        super(new Pokemon(s, lvl), options);
    }
    Encounter(String s, int lvl) { this(PokemonNames.getSpeciesFromName(s), lvl, new BattleOptions()); }
    Encounter(String s, int lvl, BattleOptions options) {
        this(PokemonNames.getSpeciesFromName(s), lvl, options);
    }
}

class TrainerPoke extends Battle {
    TrainerPoke(Species s, int lvl, BattleOptions options) {
        super(new Pokemon(s, lvl), options);
        }
    TrainerPoke(String s, int lvl) { this(PokemonNames.getSpeciesFromName(s),lvl, new BattleOptions()); }
    TrainerPoke(String s, int lvl, BattleOptions options) {
        this(PokemonNames.getSpeciesFromName(s), lvl, options);
    }
}
