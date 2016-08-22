import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

//represents a sequence of moves and levels which a species learns moves at
public class Learnset{
    private LevelMove[] levelMoves;
    
    private static final Map<String, Learnset[]> allLearnsets;
    
    public Learnset(LevelMove[] new_levelMoves) {
        if (new_levelMoves == null) {
            levelMoves = new LevelMove[0];
        } else {
            int n = new_levelMoves.length;
            levelMoves = new LevelMove[n];
            System.arraycopy(new_levelMoves, 0, levelMoves, 0, n);
        }
    }
    public Learnset() {
        levelMoves = new LevelMove[0];
    }
    
    //get species #i's learnset, for RB if useRB = true, for Y if useRB = false
    public static Learnset getLearnset(int i, String dataVersion) {
        Learnset[] set = allLearnsets.get(dataVersion);
        if(i < 0 || i >= set.length)
            return null;
        else
            return set[i];
    }
    
    static {
        allLearnsets = new HashMap<String, Learnset[]>();
        allLearnsets.put("blue", getData("moveset_blue.txt"));
        allLearnsets.put("brown", getData("moveset_brown.txt"));
        allLearnsets.put("yellow", getData("moveset_yellow.txt"));
    }
    
    private static Learnset[] getData(String filename) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.class.getResource("/resources/" + filename).openStream()));
            String text = in.readLine(); 
            int n = Integer.parseInt(text);
            Learnset[] output = new Learnset[n+1];
            output[0] = null;
            Learnset l;
            LevelMove[] lms;
            for(int i = 1; i <= n; i++) {
                String[] moves = in.readLine().split("\\s+");
                int k = moves.length / 2;
                lms = new LevelMove[k];
                for(int j = 0; j < k; j++) {
                    int lvl = Integer.parseInt(moves[2*j]);
                    Move move = Move.getMove(Integer.parseInt(moves[2*j + 1]));
                    lms[j] = new LevelMove(lvl, move);
                }
                l = new Learnset(lms);
                output[i] = l;
            }
            in.close();
            return output;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }   

    public LevelMove[] getLevelMoves() {
        return levelMoves.clone();
    }
    
    public String toString() {
        String output = "";
        for(LevelMove lm : levelMoves) {
            output += " " + lm.toString(); //string buffers are for noobs
        }
        return output;
    }
}
