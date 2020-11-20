import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//a trainer has a class and some pokemon, corresponding to some location in memory
public class Trainer implements Battleable, Iterable<Pokemon> {
    private TrainerClass t_class;
    private ArrayList<Pokemon> pokes;
    private int offset;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trainer)) {
            return false;
        } else {
            return offset == ((Trainer) o).offset; // TODO check for more?
        }
    }

    @Override
    public void battle(Pokemon p, BattleOptions options, int index) {
        int idx = 0;
        for (Pokemon tp : pokes) {
            tp.battle(p, options, idx++);
        }
    }

    @Override
    public Iterator<Pokemon> iterator() {
        return pokes.iterator();
    }

    public String toString() {
        return String.format("%s (0x%X: %s) Prize %d", t_class.getName(), offset, allPokes(), prizeMoney());
    }

    public String allPokes() {
        StringBuilder sb = new StringBuilder();
        for (Pokemon p : pokes) {
            sb.append(p.levelName() + ", ");
        }
        return sb.toString();
    }

    @Override
    public int prizeMoney() {
        Pokemon lastPokemon = pokes.get(pokes.size() - 1);
        return t_class.getBaseMoney() * lastPokemon.getLevel();
    }

    private static HashMap<Integer, Trainer> allTrainers;

    public static Trainer getTrainer(int offset) {
        if (!allTrainers.containsKey(offset))
            return null;
        else
            return allTrainers.get(offset);
    }

    // must be called before any other calls are made
    public static void initTrainers() {
        allTrainers = new HashMap<Integer, Trainer>();

        List<Trainer> trainerList = null;
        trainerList = getData("trainer_data_" + Settings.dataVersion + ".txt");

        for (Trainer t : trainerList) {
            allTrainers.put(new Integer(t.offset), t);
        }

        fixSpecialTrainers();
    }

    // reads trainer_data_(blue|yellow).txt to get trainer data
    private static List<Trainer> getData(String filename) {
        ArrayList<Trainer> trainers = new ArrayList<Trainer>();
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(System.class.getResource("/resources/" + filename)
                    .openStream()));

            TrainerClass currentClass = null;
            Trainer t;
            while (in.ready()) {
                String text = in.readLine();
                // names are formatted as [NAME]
                if (text.startsWith("[")) {
                    // TODO: error checking is for noobs
                    String currentName = text.substring(1, text.length() - 1);
                    currentClass = TrainerNames.getTrainerClassFromName(currentName);
                    continue;
                } else if (text.startsWith("0x")) { // line is a 0x(pointer):
                                                    // list of pokes
                    String[] parts = text.split(":"); // this should be length 2
                    int offset = Integer.parseInt(parts[0].substring(2), 16);

                    t = new Trainer();
                    t.t_class = currentClass;
                    t.offset = offset;
                    t.pokes = new ArrayList<Pokemon>();

                    // read off pokemon
                    String[] pokeStrs = parts[1].split(",");
                    for (String pokeStr : pokeStrs) {
                        pokeStr = pokeStr.trim();
                        if (pokeStr.isEmpty())
                            continue;
                        // the string should be "L# POKENAME"
                        // Pokemon p = new Pokemon();
                        String[] levelName = pokeStr.split(" ");
                        int level = Integer.parseInt(levelName[0].substring(1));
                        Species s = PokemonNames.getSpeciesFromName(levelName[1]);
                        t.pokes.add(new Pokemon(s, level, false));
                    }
                    trainers.add(t);
                }
            }
            in.close();
            return trainers;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    // manually fixes the movesets of special trainers
    private static void fixSpecialTrainers() {
        if(Settings.dataVersion.equals("yellow")) {
            // ref https://github.com/pret/pokeyellow/blob/48dc6a8cf4da512273e79e49346b35dcc225f773/data/trainers/special_moves.asm#L1
            
            // BUG_CATCHER, 15
            setMove(0x39EA5, 2, 2, "TACKLE");
            setMove(0x39EA5, 2, 3, "STRING SHOT");
            
            // YOUNGSTER, 14
            setMove(0x39E64, 1, 4, "FISSURE");
            
            // BROCK, 1
            setMove(0x3A454, 2, 3, "BIND");
            setMove(0x3A454, 2, 4, "BIDE");
            
            // MISTY, 1
            setMove(0x3A45A, 2, 4, "BUBBLEBEAM");
            
            // LT_SURGE, 1
            setMove(0x3A460, 1, 1, "THUNDERBOLT");
            setMove(0x3A460, 1, 2, "MEGA PUNCH");
            setMove(0x3A460, 1, 3, "MEGA KICK");
            setMove(0x3A460, 1, 4, "GROWL");
            
            // ERIKA, 1
            setMove(0x3A464, 1, 3, "MEGA DRAIN");
            setMove(0x3A464, 2, 1, "RAZOR LEAF");
            setMove(0x3A464, 3, 1, "PETAL DANCE");
            
            // KOGA, 1
            setMove(0x3A46C, 1, 1, "TOXIC");
            setMove(0x3A46C, 1, 2, "TACKLE");
            setMove(0x3A46C, 2, 1, "TOXIC");
            setMove(0x3A46C, 2, 3, "SUPERSONIC");
            setMove(0x3A46C, 3, 1, "TOXIC");
            setMove(0x3A46C, 3, 2, "DOUBLE-EDGE");
            setMove(0x3A46C, 4, 1, "LEECH LIFE");
            setMove(0x3A46C, 4, 2, "DOUBLE TEAM");
            setMove(0x3A46C, 4, 3, "PSYCHIC");
            setMove(0x3A46C, 4, 4, "TOXIC");
            
            // BLAINE, 1
            setMove(0x3A476, 1, 1, "FLAMETHROWER");
            setMove(0x3A476, 1, 4, "CONFUSE RAY");
            setMove(0x3A476, 3, 1, "FLAMETHROWER");
            setMove(0x3A476, 3, 2, "FIRE BLAST");
            setMove(0x3A476, 3, 3, "REFLECT");
            
            // SABRINA, 1
            setMove(0x3A47E, 1, 1, "FLASH");
            setMove(0x3A47E, 2, 1, "KINESIS");
            setMove(0x3A47E, 2, 4, "PSYWAVE");
            setMove(0x3A47E, 3, 1, "PSYWAVE");
            
            // GIOVANNI, 3
            setMove(0x3A30F, 1, 3, "FISSURE");
            setMove(0x3A30F, 2, 2, "DOUBLE TEAM");
            setMove(0x3A30F, 3, 1, "EARTHQUAKE");
            setMove(0x3A30F, 3, 3, "THUNDER");
            setMove(0x3A30F, 4, 1, "EARTHQUAKE");
            setMove(0x3A30F, 4, 2, "LEER");
            setMove(0x3A30F, 4, 3, "THUNDER");
            setMove(0x3A30F, 5, 1, "ROCK SLIDE");
            setMove(0x3A30F, 5, 4, "EARTHQUAKE");
            
            // LORELEI, 1
            setMove(0x3A53F, 1, 1, "BUBBLEBEAM");
            setMove(0x3A53F, 2, 3, "ICE BEAM");
            setMove(0x3A53F, 3, 1, "PSYCHIC");
            setMove(0x3A53F, 3, 2, "SURF");
            setMove(0x3A53F, 4, 3, "LOVELY KISS");
            setMove(0x3A53F, 5, 3, "BLIZZARD");
            
            // BRUNO, 1
            setMove(0x3A448, 1, 1, "ROCK SLIDE");
            setMove(0x3A448, 1, 2, "SCREECH");
            setMove(0x3A448, 1, 4, "DIG");
            setMove(0x3A448, 2, 3, "FIRE PUNCH");
            setMove(0x3A448, 2, 4, "DOUBLE TEAM");
            setMove(0x3A448, 3, 1, "DOUBLE KICK");
            setMove(0x3A448, 3, 2, "MEGA KICK");
            setMove(0x3A448, 3, 4, "DOUBLE TEAM");
            setMove(0x3A448, 4, 1, "ROCK SLIDE");
            setMove(0x3A448, 4, 2, "SCREECH");
            setMove(0x3A448, 4, 4, "EARTHQUAKE");
            setMove(0x3A448, 5, 2, "KARATE CHOP");
            setMove(0x3A448, 5, 3, "STRENGTH");
            
            // AGATHA, 1
            setMove(0x3A59A, 1, 2, "SUBSTITUTE");
            setMove(0x3A59A, 1, 3, "LICK");
            setMove(0x3A59A, 1, 4, "MEGA DRAIN");
            setMove(0x3A59A, 2, 2, "TOXIC");
            setMove(0x3A59A, 2, 4, "LEECH LIFE");
            setMove(0x3A59A, 3, 2, "LICK");
            setMove(0x3A59A, 4, 1, "WRAP");
            setMove(0x3A59A, 5, 2, "PSYCHIC");
            
            // LANCE, 1
            setMove(0x3A5A6, 1, 1, "DRAGON RAGE");
            setMove(0x3A5A6, 2, 1, "THUNDER WAVE");
            setMove(0x3A5A6, 2, 3, "THUNDERBOLT");
            setMove(0x3A5A6, 3, 1, "BUBBLEBEAM");
            setMove(0x3A5A6, 3, 2, "WRAP");
            setMove(0x3A5A6, 3, 3, "ICE BEAM");
            setMove(0x3A5A6, 4, 1, "WING ATTACK");
            setMove(0x3A5A6, 4, 2, "SWIFT");
            setMove(0x3A5A6, 4, 3, "FLY");
            setMove(0x3A5A6, 5, 1, "BLIZZARD");
            setMove(0x3A5A6, 5, 2, "FIRE BLAST");
            setMove(0x3A5A6, 5, 3, "THUNDER");
            
            // RIVAL3, 1 (JOLTEON)
            setMove(0x3A515, 1, 3, "EARTHQUAKE");
            setMove(0x3A515, 2, 4, "KINESIS");
            setMove(0x3A515, 3, 4, "LEECH SEED");
            setMove(0x3A515, 4, 1, "ICE BEAM");
            setMove(0x3A515, 5, 1, "CONFUSE RAY");
            setMove(0x3A515, 5, 4, "FIRE SPIN");
            setMove(0x3A515, 6, 3, "QUICK ATTACK");
            
            // RIVAL3, 2 (FLAREON)
            setMove(0x3A523, 1, 3, "EARTHQUAKE");
            setMove(0x3A523, 2, 4, "KINESIS");
            setMove(0x3A523, 3, 4, "LEECH SEED");
            setMove(0x3A523, 4, 1, "THUNDERBOLT");
            setMove(0x3A523, 5, 1, "ICE BEAM");
            setMove(0x3A523, 6, 2, "REFLECT");
            setMove(0x3A523, 6, 3, "QUICK ATTACK");
            
            // RIVAL3, 3 (VAPOREON)
            setMove(0x3A531, 1, 3, "EARTHQUAKE");
            setMove(0x3A531, 2, 4, "KINESIS");
            setMove(0x3A531, 3, 4, "LEECH SEED");
            setMove(0x3A531, 4, 1, "CONFUSE RAY");
            setMove(0x3A531, 4, 4, "FIRE SPIN");
            setMove(0x3A531, 5, 1, "THUNDERBOLT");
            setMove(0x3A531, 6, 1, "AURORA BEAM");
            setMove(0x3A531, 6, 3, "QUICK ATTACK");
        }
        else {
            // lonemoves (gym leaders)
            setLoneMove(0x3A3B5, 1, "BIDE");
            setLoneMove(0x3A3BB, 1, "BUBBLEBEAM");
            setLoneMove(0x3A3C1, 2, "THUNDERBOLT");
            setLoneMove(0x3A3C9, 2, "MEGA DRAIN");
            setLoneMove(0x3A3D1, 3, "TOXIC");
            setLoneMove(0x3A3E5, 3, "PSYWAVE");
            setLoneMove(0x3A3DB, 3, "FIRE BLAST");
            setLoneMove(0x3A290, 4, "FISSURE");
            
            // teammoves (e4)
            // in theory this is a different system, in practice we can use the same one
            setLoneMove(0x3A4BB, 4, "BLIZZARD");
            setLoneMove(0x3A3A9, 4, "FISSURE");
            setLoneMove(0x3A516, 4, "TOXIC");
            setLoneMove(0x3A522, 4, "BARRIER");
            
            // champion rival
            // two moves per roster: pidgeot sky attack, starter elemental move
            // ref https://github.com/pret/pokered/blob/47cd734276eade428671f720e8d01a45c4fd2bc2/engine/battle/read_trainer_party.asm#L126
            setLoneMove(0x3A491, 0, "SKY ATTACK");
            setLoneMove(0x3A49F, 0, "SKY ATTACK");
            setLoneMove(0x3A4AD, 0, "SKY ATTACK");
            
            setLoneMove(0x3A491, 5, "BLIZZARD");
            setLoneMove(0x3A49F, 5, "MEGA DRAIN");
            setLoneMove(0x3A4AD, 5, "FIRE BLAST");
        }
    }
    
    // uses 0-index for pokemon to match pokered
    private static void setLoneMove(int offset, int pokemon, String move) {
        setMove(offset, pokemon + 1, 3, move);
    }

    // uses 1-index for pokemon to match pokeyellow
    private static void setMove(int offset, int pokemon, int slot, String move) {
        Pokemon pk = getTrainer(offset).pokes.get(pokemon - 1);
        pk.setMoveset(pk.getMoveset().gymLeaderClone(move, slot));
    }
}
