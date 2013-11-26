public class TrainerClass {
    private String name;
    private int baseMoney;
    // AI modifications?

    public TrainerClass(String t_name, int t_baseMoney) {
        name = t_name;
        baseMoney = t_baseMoney;
    }

    private static TrainerClass[] allTrainerClasses;
    static {
        allTrainerClasses = new TrainerClass[Constants.numTrainerClasses + 1];
        TrainerClass t;
        for(int i = 0; i < allTrainerClasses.length; i++) {
            String t_name = Constants.trainer_class_names[i];
            int t_baseMoney = Constants.trainer_base_money[i];
            t = new TrainerClass(t_name, t_baseMoney);
            allTrainerClasses[i] = t;
        }
    }

    public static TrainerClass getTrainerClass(int i) {
        if(i < 0 || i >= allTrainerClasses.length)
            return null;
        return allTrainerClasses[i];
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getBaseMoney() {
        return baseMoney;
    }
}
