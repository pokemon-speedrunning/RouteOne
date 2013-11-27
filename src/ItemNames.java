import java.util.HashMap;

public class ItemNames {
    private static final HashMap<String, Item> nameMap;
    static {
        nameMap = new HashMap<String, Item>();
        for(int i = 1; i <= Constants.numItems; i++) {
            Item it = Item.getItem(i);
            nameMap.put(Constants.hashName(it.getName()), it);
        }
    }

    public static Item getItemFromName(String name) {
        name = Constants.hashName(name);
        if(!nameMap.containsKey(name))
            return null;
        return nameMap.get(name);
    }
}
