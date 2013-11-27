import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;


public class RouteParser {
    public static int lineNum = 0;
    
    public static List<GameAction> parseFile(String fileName) {
        lineNum = 0;
        ArrayList<GameAction> actions = new ArrayList<GameAction>();
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            while(in.ready()) {
                lineNum++;
                String wholeLine = in.readLine();
                String[] lines = wholeLine.split("//"); //remove comments
                String line = lines[0];
                GameAction a = null;
                try {
                    a = parseLine(line);
                } catch(Exception e) {
                    Main.appendln("Error in line " + lineNum);
                }
                if (a != null)
                    actions.add(a);
                
            }
            in.close();
        } catch (FileNotFoundException e) {
            Main.appendln("Could not find Route file: `" + fileName + "`");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return actions;
    }
    
    //assumes no comments
    private static GameAction parseLine(String line) throws Exception {
        String[] tokens = line.split(" ");
        int n = tokens.length;
        if (n == 0) return null;
        
        String firstToken = tokens[0];
        //trainer offset
        if(firstToken.startsWith("0x") || firstToken.startsWith("0X")) {
            Battleable b = Trainer.getTrainer(Integer.parseInt(firstToken.substring(2),16));
            if(b == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": that offset doesn't exist. Are you sure you're setting the right game?");
                return null;
            }
            String[] flagTokens = (String[]) Arrays.copyOfRange(tokens, 1, n);
            return addFlagsToBattleable(b, flagTokens);
        } 
        //L(num), to indicate pokemon
        else if(firstToken.matches("[Ll][0-9]+")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            int lvl = Integer.parseInt(firstToken.substring(1));
            String species = tokens[1];
            Pokemon b = new Pokemon(PokemonNames.getSpeciesFromName(species),lvl); //default to wild pokemon
            if (b.getSpecies() == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": bad pokemon name");
                return null;
            }
            
            String[] flagTokens = (String[]) Arrays.copyOfRange(tokens, 2, n);
            return addFlagsToBattleable(b, flagTokens);
        }
        //evolve
        else if(firstToken.equalsIgnoreCase("e") || firstToken.equalsIgnoreCase("evolve")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String species = tokens[1];
            return new Evolve(species);
        }
        //learnmove
        else if(firstToken.equalsIgnoreCase("lm") || firstToken.equalsIgnoreCase("learnmove")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String move = tokens[1];
            LearnMove l = new LearnMove(move);
            if (l.getMove() == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": bad move name");
                return null;
            }
            return l;
        }
        //unlearnmove
        else if(firstToken.equalsIgnoreCase("um") || firstToken.equalsIgnoreCase("unlearnmove")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String move = tokens[1];
            UnlearnMove l = new UnlearnMove(move);
            if (l.getMove() == null) {
                Main.appendln("ERROR ON LINE " + lineNum + ": bad move name");
                return null;
            }
            return l;
        }
        //candies, etc
        else if(firstToken.equalsIgnoreCase("rc") || firstToken.equalsIgnoreCase("rarecandy")) {
            return GameAction.eatRareCandy;
        }
        else if(firstToken.equalsIgnoreCase("hpup")) {
            return GameAction.eatHPUp;
        }
        else if(firstToken.equalsIgnoreCase("iron")) {
            return GameAction.eatIron;
        }
        else if(firstToken.equalsIgnoreCase("protein")) {
            return GameAction.eatProtein;
        }
        else if(firstToken.equalsIgnoreCase("calcium")) {
            return GameAction.eatCalcium;
        }
        else if(firstToken.equalsIgnoreCase("carbos")) {
            return GameAction.eatCarbos;
        }
        //inventory manipulation
        else if(firstToken.equalsIgnoreCase("get")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String item;
            int count = 1;
            if(n == 2) {
                item = tokens[1];
            } else {
                count = Integer.parseInt(tokens[1]);
                item = "";
                for(int i = 2; i<tokens.length; i++) {
                    item = item + tokens[i];
                }
            }
            return new GetItem(item, count);
        }
        else if(firstToken.equalsIgnoreCase("buy")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String item;
            int count = 1;
            if(n == 2) {
                item = tokens[1];
            } else {
                count = Integer.parseInt(tokens[1]);
                item = "";
                for(int i = 2; i<tokens.length; i++) {
                    item = item + tokens[i];
                }
            }
            return new BuyItem(item, count);
        }
        else if(firstToken.equalsIgnoreCase("use") || firstToken.equalsIgnoreCase("toss")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String item;
            int count = 1;
            if(n == 2) {
                item = tokens[1];
            } else {
                count = Integer.parseInt(tokens[1]);
                item = "";
                for(int i = 2; i<tokens.length; i++) {
                    item = item + tokens[i];
                }
            }
            return new LoseItem(item, count);
        }
        else if(firstToken.equalsIgnoreCase("sell")) {
            if(n < 2) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            String item;
            int count = 1;
            if(n == 2) {
                item = tokens[1];
            } else {
                count = Integer.parseInt(tokens[1]);
                item = "";
                for(int i = 2; i<tokens.length; i++) {
                    item = item + tokens[i];
                }
            }
            return new SellItem(item, count);
        }
        else if(firstToken.equalsIgnoreCase("swap")) {
            if(n < 3) {
                Main.appendln("ERROR ON LINE " + lineNum);
                return null;
            }
            return new SwapItems(tokens[1], tokens[2]);
        }
        else if(firstToken.equalsIgnoreCase("addmoney")) {
            int amount = Integer.parseInt(tokens[1]);
            return new AddMoney(amount);
        }
        //printing commands
        else if(firstToken.equalsIgnoreCase("stats")) {
            if(n == 1) {
                return GameAction.printAllStatsNoBoost;
            } else if(tokens[1].equalsIgnoreCase("-b")) {
                return GameAction.printAllStats;
            } else {
                return GameAction.printAllStatsNoBoost;
            }
        }
        else if(firstToken.equalsIgnoreCase("ranges")) {
            if(n == 1) {
                return GameAction.printStatRangesNoBoost;
            } else if(tokens[1].equalsIgnoreCase("-b")) {
                return GameAction.printStatRanges;
            } else {
                return GameAction.printStatRangesNoBoost;
            }
        }
        else if(firstToken.equalsIgnoreCase("inventory")) {
            return GameAction.printInventory;
        }
        
        return null;
    }
    
    enum NextFlag {
        ANY_FLAG, XITEMS, YITEMS, XATK, YATK,
        XDEF, YDEF, XSPD, YSPD, XSPC, YSPC,
        XACC, VERBOSE, SXP, BBS,
    }
    
    private static GameAction addFlagsToBattleable(Battleable b, String[] flagTokens) throws Exception {
        NextFlag nf = NextFlag.ANY_FLAG;
        BattleOptions options = new BattleOptions();
        
        for(String s : flagTokens) {
            //we're looking for a flag
            if (nf == NextFlag.ANY_FLAG) {
                //set this pokemon to wild
                if(s.equalsIgnoreCase("-w") || s.equalsIgnoreCase("-wild")) {
                    if(b instanceof Trainer) {
                        Main.appendln("ERROR ON LINE " + lineNum);
                        return null;
                        //can't use -wild or -trainer flag on trainers
                    }
                    ((Pokemon) b).setWild(true);
                    nf = NextFlag.ANY_FLAG;
                    continue;
                }
                //set this pokemon to trainer
                else if(s.equalsIgnoreCase("-t") || s.equalsIgnoreCase("-trainer")) {
                    if(b instanceof Trainer) {
                        Main.appendln("ERROR ON LINE " + lineNum);
                        return null;
                        //can't use -wild or -trainer flag on trainers
                    }
                    ((Pokemon) b).setWild(false);
                    nf = NextFlag.ANY_FLAG;
                    continue;
                }
                //xitems (sm1)
                else if(s.equalsIgnoreCase("-x") || s.equalsIgnoreCase("-xitems")) {
                    nf = NextFlag.XITEMS;
                    continue;
                }
                //yitems (sm2)
                else if(s.equalsIgnoreCase("-y") || s.equalsIgnoreCase("-yitems")) {
                    nf = NextFlag.YITEMS;
                    continue;
                }
                //all the x items (and y items)
                else if(s.equalsIgnoreCase("-xatk")) {
                    nf = NextFlag.XATK;
                    continue;
                }
                else if(s.equalsIgnoreCase("-yatk")) {
                    nf = NextFlag.YATK;
                    continue;
                }
                else if(s.equalsIgnoreCase("-xdef")) {
                    nf = NextFlag.XDEF;
                    continue;
                }
                else if(s.equalsIgnoreCase("-ydef")) {
                    nf = NextFlag.YDEF;
                    continue;
                }
                else if(s.equalsIgnoreCase("-xspd")) {
                    nf = NextFlag.XSPD;
                    continue;
                }
                else if(s.equalsIgnoreCase("-yspd")) {
                    nf = NextFlag.YSPD;
                    continue;
                }
                else if(s.equalsIgnoreCase("-xspc")) {
                    nf = NextFlag.XSPC;
                    continue;
                }
                else if(s.equalsIgnoreCase("-yspc")) {
                    nf = NextFlag.YSPC;
                    continue;
                }
                //xacc
                else if(s.equalsIgnoreCase("-xacc")) {
                    options.getMod1().useXAcc();
                    nf = NextFlag.ANY_FLAG;
                    continue;
                }
                //verbose
                else if(s.equalsIgnoreCase("-v") || s.equalsIgnoreCase("-verbose")) {
                    nf = NextFlag.VERBOSE;
                    continue;
                }
                //split exp
                else if(s.equalsIgnoreCase("-sxp")) {
                    nf = NextFlag.SXP;
                    continue;
                }
                //print stat ranges if level
                else if(s.equalsIgnoreCase("-lvranges")) {
                    options.setPrintSRsOnLvl(true);
                    nf = NextFlag.ANY_FLAG;
                    continue;
                }
                else if(s.equalsIgnoreCase("-lvrangesb")) {
                    options.setPrintSRsBoostOnLvl(true);
                    nf = NextFlag.ANY_FLAG;
                    continue;
                }
                //badge boosts
                else if(s.equalsIgnoreCase("-bbs")) {
                    nf = NextFlag.BBS;
                    continue;
                }
            }
            //-x flag
            else if(nf == NextFlag.XITEMS) {
                String[] nums = s.split("/");
                if(nums.length != 4 && nums.length != 5) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                }
                options.getMod1().incrementAtkStage(Integer.parseInt(nums[0]));
                options.getMod1().incrementDefStage(Integer.parseInt(nums[1]));
                options.getMod1().incrementSpdStage(Integer.parseInt(nums[2]));
                options.getMod1().incrementSpcStage(Integer.parseInt(nums[3]));
                if(nums.length == 5) {
                    if(Integer.parseInt(nums[4]) != 0) {
                        options.getMod1().useXAcc();
                    }
                }
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            //-y flag
            else if(nf == NextFlag.YITEMS) {
                String[] nums = s.split("/");
                if(nums.length != 4 && nums.length != 5) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                }
                options.getMod2().incrementAtkStage(Integer.parseInt(nums[0]));
                options.getMod2().incrementDefStage(Integer.parseInt(nums[1]));
                options.getMod2().incrementSpdStage(Integer.parseInt(nums[2]));
                options.getMod2().incrementSpcStage(Integer.parseInt(nums[3]));
                nf = NextFlag.ANY_FLAG;
                continue;
                //ignore y accuracy
            }
            //all xitem and yitem flags
            else if(nf == NextFlag.XATK){
                options.getMod1().incrementAtkStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.YATK){
                options.getMod2().incrementAtkStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.XDEF){
                options.getMod1().incrementDefStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.YDEF){
                options.getMod2().incrementDefStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.XSPD){
                options.getMod1().incrementSpdStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.YSPD){
                options.getMod2().incrementSpdStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.XSPC){
                options.getMod1().incrementSpcStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            else if(nf == NextFlag.YSPC){
                options.getMod2().incrementSpcStage(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            //verbose
            else if(nf == NextFlag.VERBOSE){
                if(s.matches("[0-9]+")) {
                    options.setVerbose(Integer.parseInt(s));
                } else if(s.equalsIgnoreCase("NONE")) {
                    options.setVerbose(BattleOptions.NONE);
                } else if(s.equalsIgnoreCase("SOME")) {
                    options.setVerbose(BattleOptions.SOME);
                } else if(s.equalsIgnoreCase("ALL")) {
                    options.setVerbose(BattleOptions.ALL);
                }
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            //sxp
            else if(nf == NextFlag.SXP){
                options.setParticipants(Integer.parseInt(s));
                nf = NextFlag.ANY_FLAG;
                continue;
            }
            //badge boosts
            else if(nf == NextFlag.BBS){
                String[] nums = s.split("/");
                if(nums.length != 4) {
                    Main.appendln("ERROR ON LINE " + lineNum);
                    return null;
                }
                int atkBB = Integer.parseInt(nums[0]);
                int defBB = Integer.parseInt(nums[1]);
                int spdBB = Integer.parseInt(nums[2]);
                int spcBB = Integer.parseInt(nums[3]);
                options.getMod1().setBadgeBoosts(atkBB, defBB, spdBB, spcBB);
                nf = NextFlag.ANY_FLAG;
                continue;
            }
        }
        if (nf != NextFlag.ANY_FLAG) {
            //TODO: error check
        }
         return new Battle(b, options);
    }
}


