import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> item_types;
    private ArrayList<Integer> item_counts;
    private int money;

    public Inventory() {
        item_types = new ArrayList<Item>();
        item_counts = new ArrayList<Integer>();
        money = 3000;
    }
    public Inventory(int i_money) {
        item_types = new ArrayList<Item>();
        item_counts = new ArrayList<Integer>();
        money = i_money;
    }

    public int getMoney() {
        return money;
    }
    public String moneyString() {
        return String.format("Money: %d", money);
    }
    public String toString() {
        return inventoryString();
    }
    public String inventoryString() {
        String endl = Constants.endl;
        StringBuilder sb = new StringBuilder();
        sb.append(moneyString() + endl);
        for(int i = 0; i<item_types.size(); i++) {
            sb.append(String.format("%2d: %2dx %s", i+1, item_counts.get(i), item_types.get(i).getName()) + endl);
        }
        return sb.toString();
    }

    public void swap(Item it1, Item it2) {
        if(it1.equals(it2)) return;
        int i1 = -1, i2 = -1;
        for(int i = 0; i < item_types.size(); i++) {
            if(it1.equals(item_types.get(i))) {
                i1 = i;
            }
            if(it2.equals(item_types.get(i))) {
                i2 = i;
            }
        }
        if(i1 == -1 || i2 == -1) return;
        Item tmp_type = item_types.get(i1);
        int tmp_count = item_counts.get(i1);
        item_types.set(i1, item_types.get(i2));
        item_counts.set(i1, item_counts.get(i2));
        item_types.set(i2, tmp_type);
        item_counts.set(i2, tmp_count);
    }

    public void swap(String it1, String it2) {
        swap(ItemNames.getItemFromName(it1), ItemNames.getItemFromName(it2));
    }

    public int addItem(Item it) {
        return multiAddItem(it, 1);
    }

    public int addItem(String s) {
        return multiAddItem(ItemNames.getItemFromName(s), 1);
    }

    public int addItem(String s, int count) {
        return multiAddItem(ItemNames.getItemFromName(s), count);
    }

    // Returns the cost of the items
    public int multiAddItem(Item it, int count) {
        for(int i = 0; i < item_types.size(); i++) {
            if(it.equals(item_types.get(i))) {
                item_counts.set(i, item_counts.get(i) + count);
                return it.getPrice() * count;
            }
        }
        // Item not already in inventory, append it if the inventory isn't full
        // Use != rather than < to emulate the bug present in the game.
        if(item_types.size() != 20) {
            item_types.add(it);
            item_counts.add(count);
            return it.getPrice() * count;
        }
        return 0;
    }

    public int removeItem(Item it) {
        return multiRemoveItem(it, 1);
    }

    public int removeItem(String s) {
        return multiRemoveItem(ItemNames.getItemFromName(s), 1);
    }

    public int removeItem(String s, int count) {
        return multiRemoveItem(ItemNames.getItemFromName(s), count);
    }

    // Returns the money you get if this is a sell
    public int multiRemoveItem(Item it, int count) {
        for(int i = 0; i < item_types.size(); i++) {
            if(it.equals(item_types.get(i))) {
                int curCount = item_counts.get(i);
                if(count < curCount) {
                    item_counts.set(i, curCount - count);
                    return it.getSellPrice() * count;
                } else {
                    item_types.remove(i);
                    item_counts.remove(i);
                    return it.getSellPrice() * curCount;
                }
            }
        }
        return 0;
    }

    public void addMoney(int dmoney) {
        money += dmoney;
    }
    public void subMoney(int dmoney) {
        money -= dmoney;
    }
    public void buyItem(Item it, int count) {
        money -= multiAddItem(it, count);
    }
    public void sellItem(Item it, int count) {
        money += multiRemoveItem(it, count);
    }
    public void getItem(Item it, int count) {
        multiAddItem(it, count);
    }
    public void loseItem(Item it, int count) {
        multiRemoveItem(it, count);
    }
}
