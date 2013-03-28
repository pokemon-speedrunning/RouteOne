//represents a battle, with planned statmods
public class Battle extends GameAction {
    private Battleable opponent;
    private StatModifier mod;
    private boolean verbose;
    
    public Battle(Battleable b) {
        opponent = b;
        mod = new StatModifier();
        verbose = false;
    }
    
    public Battle(Battleable b, boolean verbose) {
        opponent = b;
        mod = new StatModifier();
        this.verbose = verbose;
    }
    
    public Battle(Battleable b, StatModifier new_mod) {
        opponent = b;
        mod = new_mod;
        verbose = false;
    }
    
    public Battle(Battleable b, StatModifier new_mod, boolean verbose) {
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
    
    public static Battle makeBattle(int offset, boolean verbose) {
        return new Battle(Trainer.getTrainer(offset), verbose);
    }
    
    public static Battle makeBattle(int offset, StatModifier sm, boolean verbose) {
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
            if(verbose) printBattle(p, (Pokemon) opponent);
            opponent.battle(p);
        } else { //is a Trainer
            Trainer t = (Trainer) opponent;
            if(verbose) System.out.println(t);
            for(Pokemon opps : t) {
                if(verbose) printBattle(p, (Pokemon) opps);
                opps.battle(p);
            }
        }
        if(verbose) {
            System.out.println(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                    p.expToNextLevel(), p.expForLevel()));
        }
    }
    
    //does not actually do the battle, just prints summary
    public void printBattle(Pokemon us, Pokemon them) {
        System.out.println(DamageCalculator.summary(us, them, mod, new StatModifier()));
    }
    
    //TODO: do battle with lots of possible opponent stat modifier
}
