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
    public void performAction(Pokemon p) {        
        doBattle(p);
        
        //check for special gym leader badges
        if(Settings.isRB) {
            if (Trainer.getTrainer(0x3A3B5).equals(opponent)) //brock boulder badge
                p.setAtkBadge(true);
            else if (Trainer.getTrainer(0x3A3C1).equals(opponent)) //surge thunder badge
                p.setDefBadge(true);
            else if (Trainer.getTrainer(0x3A3D1).equals(opponent)) //koga soul badge
                p.setSpdBadge(true);
            else if (Trainer.getTrainer(0x3A3DB).equals(opponent)) //blaine volcano badge
                p.setSpcBadge(true);
        } else {
            if (Trainer.getTrainer(0x3A454).equals(opponent)) //brock boulder badge
                p.setAtkBadge(true);
            else if (Trainer.getTrainer(0x3A460).equals(opponent)) //surge thunder badge
                p.setDefBadge(true);
            else if (Trainer.getTrainer(0x3A46C).equals(opponent)) //koga soul badge
                p.setSpdBadge(true);
            else if (Trainer.getTrainer(0x3A476).equals(opponent)) //blaine volcano badge
                p.setSpcBadge(true);
        }
    }
    
    private void doBattle(Pokemon p) {
        //TODO: automatically determine whether or not to print
        if (opponent instanceof Pokemon) {
            if(getVerbose() == BattleOptions.ALL) printBattle(p, (Pokemon) opponent);
            else if (getVerbose() == BattleOptions.SOME) printShortBattle(p, (Pokemon) opponent);
            
            opponent.battle(p, options);
        } else { //is a Trainer
            Trainer t = (Trainer) opponent;
            if(getVerbose() == BattleOptions.ALL || getVerbose() == BattleOptions.SOME) Main.appendln(t.toString());
            int lastLvl = p.getLevel();
            for(Pokemon opps : t) {
                if(getVerbose() == BattleOptions.ALL) printBattle(p, (Pokemon) opps);
                else if (getVerbose() == BattleOptions.SOME) printShortBattle(p, (Pokemon) opps);
                opps.battle(p, options);
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
            }
        }
        if(getVerbose() == BattleOptions.ALL || getVerbose() == BattleOptions.SOME) {
            Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                    p.expToNextLevel(), p.expForLevel()));
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
