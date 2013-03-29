//represents a battle, with planned statmods
public class Battle extends GameAction {
    private Battleable opponent;
    private StatModifier mod;
    private int verbose;
    //verbose options
    public static final int NONE = 0;
    public static final int SOME = 1;
    public static final int ALL = 2;
    
    public Battle(Battleable b) {
        opponent = b;
        mod = new StatModifier();
        verbose = 0;
    }
    
    public Battle(Battleable b, int verbose) {
        opponent = b;
        mod = new StatModifier();
        this.verbose = verbose;
    }
    
    public Battle(Battleable b, StatModifier new_mod) {
        opponent = b;
        mod = new_mod;
        verbose = 0;
    }
    
    public Battle(Battleable b, StatModifier new_mod, int verbose) {
        opponent = b;
        mod = new_mod;
        this.verbose = verbose;
    }
    
    public StatModifier getMod() {
        return mod;
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
    
    public static Battle makeBattle(Pokemon p) {
        return new Battle(p);
    }
    
    @Override
    public void performAction(Pokemon p) {
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
            if(verbose == ALL || verbose == SOME) System.out.println(t);
            for(Pokemon opps : t) {
                if(verbose == ALL) printBattle(p, (Pokemon) opps);
                else if (verbose == SOME) printShortBattle(p, (Pokemon) opps);
                opps.battle(p);
            }
        }
        if(verbose == ALL || verbose == SOME) {
            System.out.println(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                    p.expToNextLevel(), p.expForLevel()));
        }
    }
    
    //does not actually do the battle, just prints summary
    public void printBattle(Pokemon us, Pokemon them) {
        System.out.println(DamageCalculator.summary(us, them, mod, new StatModifier()));
    }
    
    //does not actually do the battle, just prints short summary
    public void printShortBattle(Pokemon us, Pokemon them) {
        System.out.println(DamageCalculator.shortSummary(us, them, mod, new StatModifier()));
    }
    //TODO: do battle with lots of possible opponent stat modifier
}
