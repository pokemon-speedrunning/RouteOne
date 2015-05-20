
public abstract class GameAction {
    abstract void performAction(Pokemon p, Inventory inv);
    
    public static final GameAction eatRareCandy = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.eatRareCandy(); inv.removeItem("Rarecandy"); }
    };
    public static final GameAction eatHPUp = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.eatHPUp(); inv.removeItem("HP UP"); }
    };
    public static final GameAction eatIron = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.eatIron(); inv.removeItem("Iron"); }
    };
    public static final GameAction eatProtein = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.eatProtein(); inv.removeItem("Protein"); }
    };
    public static final GameAction eatCalcium = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.eatCalcium(); inv.removeItem("Calcium"); }
    };
    public static final GameAction eatCarbos = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.eatCarbos(); inv.removeItem("Carbos"); }
    };
    
    //badges
    public static final GameAction getBoulderBadge = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.setAtkBadge(true); }
    };
    public static final GameAction getSoulBadge = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.setSpdBadge(true); } //gen 1 is buggy as fuck
    };
    public static final GameAction getVolcanoBadge = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.setSpcBadge(true); }
    };
    public static final GameAction getThunderBadge = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { p.setDefBadge(true); }
    };
    
    
    //not really a game action, but it's a nice hack?
    public static final GameAction printAllStats = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { Main.appendln(p.statsWithBoost()); }
    };
    public static final GameAction printAllStatsNoBoost = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { Main.appendln(p.statsWithoutBoost()); }
    };
    public static final GameAction printStatRanges = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { 
			Main.appendln(p.statRanges(true)); 
            Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                    p.expToNextLevel(), p.expForLevel()));
		}
    };
    public static final GameAction printStatRangesNoBoost = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { 
			Main.appendln(p.statRanges(false)); 
        	Main.appendln(String.format("LVL: %d EXP NEEDED: %d/%d", p.getLevel(),
                p.expToNextLevel(), p.expForLevel()));
		}
    };
    public static final GameAction printMoney = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { Main.appendln(inv.moneyString()); }
    };
    public static final GameAction printInventory = new GameAction() {
        void performAction(Pokemon p, Inventory inv) { Main.appendln(inv.inventoryString()); }
    };

}

class LearnMove extends GameAction {
    private Move move;
    LearnMove(Move m) { move = m; }
    LearnMove(String s) { move = Move.getMoveByName(s); }
    public Move getMove() { return move; }
    @Override
    void performAction(Pokemon p, Inventory inv) { p.getMoveset().addMove(move); }
}


class UnlearnMove extends GameAction {
    private Move move;
    UnlearnMove(Move m) { move = m; }
    UnlearnMove(String s) { move = Move.getMoveByName(s); }
    public Move getMove() { return move; }
    @Override
    void performAction(Pokemon p, Inventory inv) { p.getMoveset().delMove(move); }
}

class Evolve extends GameAction {
    private Species target;
    Evolve(Species s) { target = s; }
    Evolve(String s) { target = PokemonNames.getSpeciesFromName(s); }
    @Override
    void performAction(Pokemon p, Inventory inv) {
        p.evolve(target);
        p.calculateStats();
    }
}

class GetItem extends GameAction {
    private Item type;
    private int count;
    GetItem(Item it) { type = it; count = 1; }
    GetItem(Item it, int cnt) { type = it; count = cnt; }
    GetItem(String s) { type = ItemNames.getItemFromName(s); count = 1; }
    GetItem(String s, int cnt) { type = ItemNames.getItemFromName(s); count = cnt; }

    @Override
    void performAction(Pokemon p, Inventory inv) {
        inv.getItem(type, count);
    }
}

class LoseItem extends GameAction {
    private Item type;
    private int count;
    LoseItem(Item it) { type = it; count = 1; }
    LoseItem(Item it, int cnt) { type = it; count = cnt; }
    LoseItem(String s) { type = ItemNames.getItemFromName(s); count = 1; }
    LoseItem(String s, int cnt) { type = ItemNames.getItemFromName(s); count = cnt; }

    @Override
    void performAction(Pokemon p, Inventory inv) {
        inv.loseItem(type, count);
    }
}

class BuyItem extends GameAction {
    private Item type;
    private int count;
    BuyItem(Item it) { type = it; count = 1; }
    BuyItem(Item it, int cnt) { type = it; count = cnt; }
    BuyItem(String s) { type = ItemNames.getItemFromName(s); count = 1; }
    BuyItem(String s, int cnt) { type = ItemNames.getItemFromName(s); count = cnt; }

    @Override
    void performAction(Pokemon p, Inventory inv) {
        inv.buyItem(type, count);
    }
}

class SellItem extends GameAction {
    private Item type;
    private int count;
    SellItem(Item it) { type = it; count = 1; }
    SellItem(Item it, int cnt) { type = it; count = cnt; }
    SellItem(String s) { type = ItemNames.getItemFromName(s); count = 1; }
    SellItem(String s, int cnt) { type = ItemNames.getItemFromName(s); count = cnt; }

    @Override
    void performAction(Pokemon p, Inventory inv) {
        inv.sellItem(type, count);
    }
}

class AddMoney extends GameAction {
    private int amount;
    AddMoney(int amt) { amount = amt; }
    @Override
    void performAction(Pokemon p, Inventory inv) {
        inv.addMoney(amount);
    }
}

class SwapItems extends GameAction {
    private Item type1, type2;
    SwapItems(Item it1, Item it2) { type1 = it1; type2 = it2; }
    SwapItems(String it1, String it2) { type1 = ItemNames.getItemFromName(it1); type2 = ItemNames.getItemFromName(it2); }
    @Override
    void performAction(Pokemon p, Inventory inv) {
        inv.swap(type1, type2);
    }
}
