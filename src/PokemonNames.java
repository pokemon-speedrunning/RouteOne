import java.util.HashMap;


public class PokemonNames {
    private static final HashMap<String, Species> nameMap;
    static {
        nameMap = new HashMap<String, Species>();
        for(int i = 1; i <= Constants.numPokes; i++) {
            Species s = Species.getSpecies(i);
            nameMap.put(Constants.hashName(s.getName()),s);
        }
    }
    
    //returns the species with this name, or null if it does not exist
    public static Species getSpeciesFromName(String name) {
        name = Constants.hashName(name);
        if(!nameMap.containsKey(name))
            return null;
        return nameMap.get(name);
    }
}
