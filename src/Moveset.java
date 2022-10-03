import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//represents a list of moves (doesn't care if there are > 4 moves)
public class Moveset implements Iterable<Move>{
    private ArrayList<Move> moves = new ArrayList<Move>();;
    //private int numMoves;
    //private Move[] moves;
    
    public Moveset() {
    }
    
    public Moveset(List<Move> newMoves) {
        if (newMoves == null)
            return;          
        moves = new ArrayList<Move>(newMoves);
    }
    
    //returns the 4 most recently learned moves for a pokemon of this level
    public static Moveset defaultMoveset(Species species, int level, String dataVersion){
        ArrayList<Move> moves = new ArrayList<Move>();
        Learnset l = Learnset.getLearnset(species.getPokedexNum(), dataVersion);
        if (l == null)
            return new Moveset();
        LevelMove[] lms = l.getLevelMoves();
        for(int i =  0; i < lms.length; i++) {
            Move m = lms[i].getMove();
            if (!moves.contains(m) && lms[i].getLevel() <= level) {
                if (moves.size() == 4)
                    moves.remove(0);
                moves.add(m);
            }
        }
        return new Moveset(moves);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for(Move m : moves) {
            sb.append(m.getName() + ", ");
        }
        
        return sb.toString();
    }

    @Override
    public Iterator<Move> iterator() {
        return moves.iterator();
    }
    
    public void addMove(Move m) {
        if (!moves.contains(m))
            moves.add(m);
    }
    
    public void addMove(String s) {
        addMove(Move.getMoveByName(s));
    }
    
    public boolean delMove(Move m) {
        return moves.remove(m);
    }
    
    public void delMove(String s) {
        delMove(Move.getMoveByName(s));
    }
    
    public Moveset gymLeaderClone(String thirdMove) {
        return gymLeaderClone(thirdMove, 3);
    }
    
    public Moveset gymLeaderClone(String move, int moveSlot) {
        Moveset newSet = new Moveset(this.moves);
        if(newSet.moves.size() >= moveSlot) {
            newSet.moves.set(moveSlot-1, Move.getMoveByName(move));
        }
        else {
            newSet.addMove(move);
        }
        return newSet;
    }
    
}
