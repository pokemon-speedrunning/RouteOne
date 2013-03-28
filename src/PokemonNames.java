import java.util.HashMap;


public class PokemonNames {
    private static final HashMap<String, Species> nameMap;
    static {
        nameMap = new HashMap<String, Species>();
        for(int i = 1; i <= Constants.numPokes; i++) {
            Species s = Species.getSpecies(i);
            nameMap.put(s.getName().toUpperCase(),s);
        }
        //a few extra for mr.mime and farfetchd, just in case
        nameMap.put("MR.MIME", Species.getSpecies(122));
        nameMap.put("FARFETCH'D", Species.getSpecies(83));
    }
    
    //returns the species with this name, or null if it does not exist
    public static Species getSpeciesFromName(String name) {
        name = name.toUpperCase();
        if(!nameMap.containsKey(name))
            return null;
        return nameMap.get(name);
    }
}
