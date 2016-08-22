
public interface Battleable {
    //makes pokemon p get exp from this object
    void battle(Pokemon p, BattleOptions options, int pokemonIndex);
    int prizeMoney();
}
