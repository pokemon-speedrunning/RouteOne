import java.util.HashMap;

public class TrainerNames {
    private static final HashMap<String, TrainerClass> nameMap;
    static {
        nameMap = new HashMap<String, TrainerClass>();
        for(int i = 1; i<= Constants.numTrainerClasses; i++) {
            TrainerClass t = TrainerClass.getTrainerClass(i);
            nameMap.put(Constants.hashName(t.getName()),t);
        }
    }

    public static TrainerClass getTrainerClassFromName(String name) {
        name = Constants.hashName(name);
        if(!nameMap.containsKey(name))
            return null;
        return nameMap.get(name);
    }
}
