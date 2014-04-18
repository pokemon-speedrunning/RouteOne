public class Item {
    private String name;
    private int index;
    private int cost;

    public Item(String i_name, int i_index, int i_cost) {
        name = i_name;
        index = i_index;
        cost = i_cost;
    }

    private static Item[] allItems;
    static {
        allItems = new Item[Constants.numItems + 1];
        for(int i = 0; i<allItems.length; i++) {
            allItems[i] = new Item(Constants.item_names[i], i, Constants.item_prices[i]);
        }
    }

    public static Item getItem(int i) {
        if(i < 0 || i >= allItems.length)
            return null;
        return allItems[i];
    }

    public boolean equals(Object o) {
        if(!(o instanceof Item)) {
            return false;
        } else {
            return index == ((Item) o).index;
        }
    }
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return cost;
    }
    public int getSellPrice() {
        return cost/2;
    }
}
