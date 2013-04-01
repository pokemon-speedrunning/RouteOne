//represents a battle, with planned statmods
public class Battle extends GameAction {
    private Battleable opponent;
    private StatModifier mod1 = new StatModifier(); //our mod
    private StatModifier mod2 = new StatModifier(); //their mod
    private int verbose = Battle.NONE;
    //verbose options
    public static final int NONE = 0;
    public static final int SOME = 1;
    public static final int ALL = 2;
    
    public Battle(Battleable b) {
        opponent = b;
    }
    
    public Battle(Battleable b, int verbose) {
        opponent = b;
        this.verbose = verbose;
    }
    
    public Battle(Battleable b, StatModifier mod1) {
        opponent = b;
        this.mod1 = mod1;
    }
    
    public Battle(Battleable b, StatModifier mod1, int verbose) {
        opponent = b;
        this.mod1 = mod1;
        this.verbose = verbose;
    }
    
    public Battle(Battleable b, StatModifier mod1, StatModifier mod2, int verbose) {
        opponent = b;
        this.mod1 = mod1;
        this.mod2 = mod2;
        this.verbose = verbose;
    }
    
    public StatModifier getMod1() {
        return mod1;
    }
    
    public StatModifier getMod2() {
        return mod2;
    }
    
    public static Battle makeBattle(int offset) {
        return new Battle(Trainer.getTrainer(offset));
    }
    
    public static Battle makeBattle(int offset, StatModifier sm) {
        return new Battle(Trainer.getTrainer(offset), sm);
    }
    
    public static Battle makeBattle(int offset, int verbose) {
        return new Battle(Trainer.getTrainer(offset), verbose);
    }
    
    public static Battle makeBattle(int offset, StatModifier sm, int verbose) {
        return new Battle(Trainer.getTrainer(offset), sm, verbose);
    }
    
    public static Battle makeBattle(int offset, StatModifier mod1, StatModifier mod2, int verbose) {
        return new Battle(Trainer.getTrainer(offset), mod1, mod2, verbose);
    }
    
    public static Battle makeBattle(Pokemon p) {
        return new Battle(p);
    }
    
    @Override
    public void performAction(Pokemon p) {
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
        
        doBattle(p);
    }
    
    private void doBattle(Pokemon p) {
        //TODO: automatically determine whether or not to print
        if (opponent instanceof Pokemon) {
            if(verbose == ALL) printBattle(p, (Pokemon) opponent);
            else if (verbose == SOME) printShortBattle(p, (Pokemon) opponent);
            
            opponent.battle(p);
        } else { //is a Trainer
            Trainer t = (Trainer) opponent;
            if(verbose == ALL || verbose == SOME) Main.appendln(t.toString());
            for(Pokemon opps : t) {
                if(verbose == ALL) printBattle(p, (Pokemon) opps);
                else if (verbose == SOME) printShortBattle(p, (Pokemon) opps);
                opps.battle(p);
            }
        }
        if(verbose == ALL || verbose == SOME) {
            Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                    p.expToNextLevel(), p.expForLevel()));
        }
    }
    
    //does not actually do the battle, just prints summary
    public void printBattle(Pokemon us, Pokemon them) {
        Main.appendln(DamageCalculator.summary(us, them, mod1, mod2));
    }
    
    //does not actually do the battle, just prints short summary
    public void printShortBattle(Pokemon us, Pokemon them) {
        Main.appendln(DamageCalculator.shortSummary(us, them, mod1, mod2));
    }
}

class Encounter extends Battle {
    Encounter(Species s, int lvl, StatModifier mod1, StatModifier mod2, int verbose) {
        super(new Pokemon(s, lvl), mod1, mod2, verbose);
    }
    Encounter(String s, int lvl) { this(PokemonNames.getSpeciesFromName(s),lvl,
            new StatModifier(), new StatModifier(), Battle.NONE); }
    Encounter(String s, int lvl, StatModifier mod1, StatModifier mod2, int verbose) {
        this(PokemonNames.getSpeciesFromName(s), lvl, mod1, mod2, verbose);
    }
}

class TrainerPoke extends Battle {
    TrainerPoke(Species s, int lvl, StatModifier mod1, StatModifier mod2, int verbose) {
        super(new Pokemon(s, lvl, false), mod1, mod2, verbose);
        }
    TrainerPoke(String s, int lvl) { this(PokemonNames.getSpeciesFromName(s),lvl,
            new StatModifier(), new StatModifier(), Battle.NONE); }
    TrainerPoke(String s, int lvl, StatModifier mod1, StatModifier mod2, int verbose) {
        this(PokemonNames.getSpeciesFromName(s), lvl, mod1, mod2, verbose);
    }
}
