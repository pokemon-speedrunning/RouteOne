public class Main {
    public static void main(String[] args) {
//        for(int i = 0; i <= Constants.numMoves; i++) {
//            System.out.println(Move.getMove(i));
//        }
//        for(int i = 0; i <= Constants.numPokes; i++) {
//            System.out.println(Species.getSpecies(i).getName());
//            System.out.println(Learnset.getLearnset(i,true));
//        }
//        for(int i = 0; i <= Constants.numPokes; i++) {
//            Species s = Species.getSpecies(i);
//            System.out.println(s.getName());
//            System.out.println(Moveset.defaultMoveset(s, 29, true));
//        }
          
//        Pokemon p1 = new Pokemon(Species.getSpecies(6),50);
//        Pokemon p2 = new Pokemon(Species.getSpecies(46),100);
//        StatModifier s1 = new StatModifier();
//        s1.incrementSpcStage(7);
//        s1.useXAcc();
//        //s1.incrementDefStage(-2);
//        StatModifier s2 = new StatModifier();
//        System.out.println(DamageCalculator.summary(p1, p2, s1, s2));
//        System.out.println(p1.getSpecies());
//        System.out.println(p2.getSpecies());
//        
//        System.out.println(Trainer.getTrainer(0x3A1DA).allPokes());
        //System.out.println(Integer.parseInt("1FF",16));
        
        //be sure to print summary before displaying
        IVs god = new IVs(15,15,15,15);
        IVs dog = new IVs(0,0,0,0);
        IVs mid = new IVs(9,8,8,8);
        IVs test = new IVs(14,13,8,15);
        Pokemon p = null;
        GameAction[] actions = null;
        
        if(Settings.isRB) {
            p = new Pokemon(PokemonNames.getSpeciesFromName("clefairy"),10,dog,false);
            actions = Routes.blueClefableRoute;
        } else {
            p = new Pokemon(PokemonNames.getSpeciesFromName("NIDORANM"),6,test,false);
            actions = Routes.yellowRoute;
        }
        
        
        //Battle.makeBattle(0xZZZZZ, true), //
        //Battle.makeBattle(0xZZZZZ, new StatModifier(0,0,0,0), true), //
        
        int[] XItems = {0,0,0,0,0}; //atk,def,spd,spc,acc
        int numBattles = 0;
        int rareCandies = 0;
        for(GameAction a : actions) {        
            a.performAction(p);
            if (a instanceof Battle) {
                StatModifier sm = ((Battle) a).getMod();
                XItems[0] += Math.max(0, sm.getAtkStage());
                XItems[1] += Math.max(0, sm.getDefStage());
                XItems[2] += Math.max(0, sm.getSpdStage());
                XItems[3] += Math.max(0, sm.getSpcStage());
                XItems[4] += sm.getUsedXAcc() ? 1 : 0;
                numBattles++;
            } else if (a == GameAction.eatRareCandy) {
                rareCandies++;
            }
        }
        
        
        //System.out.println(p.levelName() + " " + p.statsStr());
        /*
        System.out.println("X ATTACKS: " + XItems[0]);
        System.out.println("X DEFENDS: " + XItems[1]);
        System.out.println("X SPEEDS: " + XItems[2]);
        System.out.println("X SPECIALS: " + XItems[3]);
        System.out.println("X ACCURACYS: " + XItems[4]);
        int cost = XItems[0] * 500 + XItems[1] * 550 + XItems[2] * 350 + XItems[3] * 350 + XItems[4] * 950;
        System.out.println("X item cost: " + cost);
        */
        
        System.out.println("Total Battles: " + numBattles);
        System.out.println("Total Rare Candies: " + rareCandies);
    }
}
