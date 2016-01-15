import java.util.ArrayList;
import java.util.List;


public enum Type {
    NORMAL, FIGHTING, FLYING, POISON, GROUND, ROCK, BUG, GHOST, FIRE, WATER, GRASS, ELECTRIC, PSYCHIC, ICE, DRAGON, NONE;
    //returns the type effectiveness multiplier.
    //defType2 should be Type.NONE if the defending pokemon has only one type
    public static double effectiveness(Type atkType, Type defType1, Type defType2) {
        return effectiveness(atkType, defType1) * effectiveness(atkType, defType2);
    }
    
    private static double effectiveness(Type atkType, Type defType) {
        if(defType == NONE || atkType == NONE) {
            return 1;
        } else {
            int val = typeTable[typeIndex(atkType)][typeIndex(defType)];
            return (val == 5) ? 0.5 : val; //i am coding master
        }
        
    }
    //returns index associated with this type (in the order written)
    //Type.NONE will return -1
    private static int typeIndex(Type t) {
        switch(t) {
        case NORMAL:
            return 0;
        case FIGHTING:
            return 1;
        case FLYING:
            return 2;
        case POISON:
            return 3;
        case GROUND:
            return 4;
        case ROCK:
            return 5;
        case BUG:
            return 6;
        case GHOST:
            return 7;
        case FIRE:
            return 8;
        case WATER:
            return 9;
        case GRASS:
            return 10;
        case ELECTRIC:
            return 11;
        case PSYCHIC:
            return 12;
        case ICE:
            return 13;
        case DRAGON:
            return 14;
        default: //NONE
            return -1;
        }
    }
    
    //typeTable[i][j] is type i's effectiveness against type j, with 5 representing 0.5
    private static final int[][] typeTable = {
        {1, 1, 1, 1, 1, 5, 1, 0, 1, 1, 1, 1, 1, 1, 1},
        {2, 1, 5, 5, 1, 2, 5, 0, 1, 1, 1, 1, 5, 2, 1},
        {1, 2, 1, 1, 1, 5, 2, 1, 1, 1, 2, 5, 1, 1, 1},
        {1, 1, 1, 5, 5, 5, 2, 5, 1, 1, 2, 1, 1, 1, 1},
        {1, 1, 0, 2, 1, 2, 5, 1, 2, 1, 5, 2, 1, 1, 1},
        {1, 5, 2, 1, 5, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1},
        {1, 5, 5, 2, 1, 1, 1, 5, 5, 1, 2, 1, 2, 1, 1},
        {0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 0, 1, 1},
        {1, 1, 1, 1, 1, 5, 2, 1, 5, 5, 2, 1, 1, 2, 5},
        {1, 1, 1, 1, 2, 2, 1, 1, 2, 5, 5, 1, 1, 1, 5},
        {1, 1, 5, 5, 2, 2, 5, 1, 5, 2, 5, 1, 1, 1, 5},
        {1, 1, 2, 1, 0, 1, 1, 1, 1, 2, 5, 5, 1, 1, 5},
        {1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1},
        {1, 1, 2, 1, 2, 1, 1, 1, 1, 5, 2, 1, 1, 5, 2},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2}
        };

    public static boolean isPhysicalType(Type t) {
        return(typeIndex(t) >= typeIndex(Type.NORMAL) && typeIndex(t) <= typeIndex(Type.GHOST));
    }
    
    public static int applyTypeEffectiveness(int damage, Type atkType,
			Type defType1, Type defType2) {
		for (TypeEffectiveness t : effectList) {
			if (t.atkType == atkType
					&& (t.defType == defType1 || t.defType == defType2)) {
				damage = damage * t.multiplier / 10;
			}
		}
		return damage;
	}

	private static List<TypeEffectiveness> effectList = new ArrayList<TypeEffectiveness>();

	static {
		effectList.add(new TypeEffectiveness(Type.WATER, Type.FIRE, 20));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.GRASS, 20));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.ICE, 20));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.WATER, 20));
		effectList.add(new TypeEffectiveness(Type.ELECTRIC, Type.WATER, 20));
		effectList.add(new TypeEffectiveness(Type.WATER, Type.ROCK, 20));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.FLYING, 0));
		effectList.add(new TypeEffectiveness(Type.WATER, Type.WATER, 5));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.FIRE, 5));
		effectList.add(new TypeEffectiveness(Type.ELECTRIC, Type.ELECTRIC, 5));
		effectList.add(new TypeEffectiveness(Type.ICE, Type.ICE, 5));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.GRASS, 5));
		effectList.add(new TypeEffectiveness(Type.PSYCHIC, Type.PSYCHIC, 5));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.WATER, 5));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.FIRE, 5));
		effectList.add(new TypeEffectiveness(Type.WATER, Type.GRASS, 5));
		effectList.add(new TypeEffectiveness(Type.ELECTRIC, Type.GRASS, 5));
		effectList.add(new TypeEffectiveness(Type.NORMAL, Type.ROCK, 5));
		effectList.add(new TypeEffectiveness(Type.NORMAL, Type.GHOST, 0));
		effectList.add(new TypeEffectiveness(Type.GHOST, Type.GHOST, 20));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.BUG, 20));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.ROCK, 5));
		effectList.add(new TypeEffectiveness(Type.WATER, Type.GROUND, 20));
		effectList.add(new TypeEffectiveness(Type.ELECTRIC, Type.GROUND, 0));
		effectList.add(new TypeEffectiveness(Type.ELECTRIC, Type.FLYING, 20));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.GROUND, 20));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.BUG, 5));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.POISON, 5));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.ROCK, 20));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.FLYING, 5));
		effectList.add(new TypeEffectiveness(Type.ICE, Type.WATER, 5));
		effectList.add(new TypeEffectiveness(Type.ICE, Type.GRASS, 20));
		effectList.add(new TypeEffectiveness(Type.ICE, Type.GROUND, 20));
		effectList.add(new TypeEffectiveness(Type.ICE, Type.FLYING, 20));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.NORMAL, 20));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.POISON, 5));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.FLYING, 5));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.PSYCHIC, 5));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.BUG, 5));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.ROCK, 20));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.ICE, 20));
		effectList.add(new TypeEffectiveness(Type.FIGHTING, Type.GHOST, 0));
		effectList.add(new TypeEffectiveness(Type.POISON, Type.GRASS, 20));
		effectList.add(new TypeEffectiveness(Type.POISON, Type.POISON, 5));
		effectList.add(new TypeEffectiveness(Type.POISON, Type.GROUND, 5));
		effectList.add(new TypeEffectiveness(Type.POISON, Type.BUG, 20));
		effectList.add(new TypeEffectiveness(Type.POISON, Type.ROCK, 5));
		effectList.add(new TypeEffectiveness(Type.POISON, Type.GHOST, 5));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.FIRE, 20));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.ELECTRIC, 20));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.GRASS, 5));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.BUG, 5));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.ROCK, 20));
		effectList.add(new TypeEffectiveness(Type.GROUND, Type.POISON, 20));
		effectList.add(new TypeEffectiveness(Type.FLYING, Type.ELECTRIC, 5));
		effectList.add(new TypeEffectiveness(Type.FLYING, Type.FIGHTING, 20));
		effectList.add(new TypeEffectiveness(Type.FLYING, Type.BUG, 20));
		effectList.add(new TypeEffectiveness(Type.FLYING, Type.GRASS, 20));
		effectList.add(new TypeEffectiveness(Type.FLYING, Type.ROCK, 5));
		effectList.add(new TypeEffectiveness(Type.PSYCHIC, Type.FIGHTING, 20));
		effectList.add(new TypeEffectiveness(Type.PSYCHIC, Type.POISON, 20));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.FIRE, 5));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.GRASS, 20));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.FIGHTING, 5));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.FLYING, 5));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.PSYCHIC, 20));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.GHOST, 5));
		effectList.add(new TypeEffectiveness(Type.BUG, Type.POISON, 20));
		effectList.add(new TypeEffectiveness(Type.ROCK, Type.FIRE, 20));
		effectList.add(new TypeEffectiveness(Type.ROCK, Type.FIGHTING, 5));
		effectList.add(new TypeEffectiveness(Type.ROCK, Type.GROUND, 5));
		effectList.add(new TypeEffectiveness(Type.ROCK, Type.FLYING, 20));
		effectList.add(new TypeEffectiveness(Type.ROCK, Type.BUG, 20));
		effectList.add(new TypeEffectiveness(Type.ROCK, Type.ICE, 20));
		effectList.add(new TypeEffectiveness(Type.GHOST, Type.NORMAL, 0));
		effectList.add(new TypeEffectiveness(Type.GHOST, Type.PSYCHIC, 0));
		effectList.add(new TypeEffectiveness(Type.FIRE, Type.DRAGON, 5));
		effectList.add(new TypeEffectiveness(Type.WATER, Type.DRAGON, 5));
		effectList.add(new TypeEffectiveness(Type.ELECTRIC, Type.DRAGON, 5));
		effectList.add(new TypeEffectiveness(Type.GRASS, Type.DRAGON, 5));
		effectList.add(new TypeEffectiveness(Type.ICE, Type.DRAGON, 20));
		effectList.add(new TypeEffectiveness(Type.DRAGON, Type.DRAGON, 20));
		// uncomment this if you want to check inverse battles for whatever reason
//		Collections.reverse(effectList);
//		for(TypeEffectiveness te : effectList) {
//			if(te.multiplier==20) {
//				te.multiplier=5;
//			}
//			else {
//				te.multiplier=20;
//			}
//		}
	}

	private static class TypeEffectiveness {
		Type atkType;
		Type defType;
		int multiplier;

		TypeEffectiveness(Type atkType, Type defType, int multiplier) {
			this.atkType = atkType;
			this.defType = defType;
			this.multiplier = multiplier;
		}
	}
    
}
