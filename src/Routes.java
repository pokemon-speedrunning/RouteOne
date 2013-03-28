
public class Routes {
    public static final GameAction[] blueClefableRoute = {
        //lvl9bug
        //Battle.makeBattle(0x3A3B5), //brock
        //4 ppl at route 3
        GameAction.getBoulderBadge,
        new Evolve("Clefable"),
        GameAction.eatRareCandy,
        new LearnMove("MEGA PUNCH"),
        new LearnMove("WATER GUN"),
//        GameAction.printAllStats, 
        Battle.makeBattle(0x3A29C), //mt moon rocket
        Battle.makeBattle(0x39F2A), //mt moon nerd
        new Encounter("RATTATA",8),
        Battle.makeBattle(0x3A209), //cerulean rival fight (if you are squirtle)

        //NUGGET BRIDGE
        Battle.makeBattle(0x39DF2), //1: bug catcher
        Battle.makeBattle(0x39E27), //2: lass
        Battle.makeBattle(0x39DA5), //3: youngster
        Battle.makeBattle(0x39E23), //4: lass
        Battle.makeBattle(0x39E80), //5: jrtrainerm
        Battle.makeBattle(0x3A2B0), //rocket
        
        Battle.makeBattle(0x39F63), //elixir hiker
        Battle.makeBattle(0x39E2B), //lass
        Battle.makeBattle(0x39E7C), //jrtrainerm
        Battle.makeBattle(0x39E2F), //lass
        
        //MISTY
        Battle.makeBattle(0x39E9D), //goldeen
        Battle.makeBattle(0x3A3BB), //MISTY
        new UnlearnMove("WATER GUN"),
        new LearnMove("BUBBLEBEAM"),
        
        //BETWEEN CERULEAN AND VERMILLION
        Battle.makeBattle(0x3A2AC), //rocket
        Battle.makeBattle(0x39EA4), //pidgey girl
        Battle.makeBattle(0x39E86), //jrtrainerm
        
        //SS ANNE
        Battle.makeBattle(0x39DB5), //BODY SLAM TM
        new UnlearnMove("POUND"),
        new LearnMove("BODY SLAM"),
        Battle.makeBattle(0x3A40B), //ss anne rival fight (for squirtle)
        
        //SURGE
        Battle.makeBattle(0x3A3C1), //SURGE fight
        GameAction.getThunderBadge,
        new UnlearnMove("GROWL"),
        new LearnMove("THUNDERBOLT"),

        //EAST OF CERULEAN (ROUTE 9)
        Battle.makeBattle(0x39EAC), //jrtrainerf (4thrash for squirtles)
        Battle.makeBattle(0x39E07), //bug catcher
        
        //ROCK TUNNEL
        Battle.makeBattle(0x39F22), //maniac
        Battle.makeBattle(0x39F1A), //maniac
        Battle.makeBattle(0x39EC2), //jrtrainerf
        Battle.makeBattle(0x39F81), //hiker
        Battle.makeBattle(0x39EE8), //jrtrainerf
        
        //WEST OF LAVENDER (ROUTE 8)
        Battle.makeBattle(0x3A0CD), //gambler
        
        new UnlearnMove("MEGA PUNCH"),
        new LearnMove("PSYCHIC"),
        
        //LAVENDER TOWER
        Battle.makeBattle(0x3A42B), //rival fight (for squirtles)
        Battle.makeBattle(0x3A4CD), // (not sure about actual offset for these)
        GameAction.eatRareCandy,
        Battle.makeBattle(0x3A4CA), // gastlies
        Battle.makeBattle(0x3A4CA), //
        //GameAction.eatRareCandy,
        Battle.makeBattle(0x3A2ED), //rockets
        Battle.makeBattle(0x3A2F2), //
        Battle.makeBattle(0x3A2F6), //
        
        //KOGA
        Battle.makeBattle(0x3A13A, new StatModifier(1,0,0,0)), //juggler, 1 X ATK
        Battle.makeBattle(0x3A140, new StatModifier(1,0,0,0)), //juggler, 1 X ATK
        Battle.makeBattle(0x3A3D1, new StatModifier(0,0,1,3,true)), //KOGA
        GameAction.getSoulBadge,
        
        //SILPH
        new UnlearnMove("BUBBLEBEAM"),
        new LearnMove("BLIZZARD"),
        Battle.makeBattle(0x3A319), //rocket
        GameAction.eatRareCandy,
        GameAction.eatRareCandy,
        Battle.makeBattle(0x3A44F, new StatModifier(0,0,1,1,true)), //rival silph co (for squirtle)
        Battle.makeBattle(0x3A355), //rocket
        Battle.makeBattle(0x3A286, new StatModifier(0,0,0,1)), //giovanni
    
        //SABRINA
        Battle.makeBattle(0x3A3E5, new StatModifier(1,0,1,0)), //SABRINA
    
        //BLAINE
        Battle.makeBattle(0x3A3DB, new StatModifier(2,0,1,0)), //BLAINE
        GameAction.getVolcanoBadge,
        
        //ERIKA
        Battle.makeBattle(0x3A0DB), //eggs
        Battle.makeBattle(0x3A3C9, new StatModifier(0,0,0,0), true), //ERIKA
        
        //GIOVANNI
        Battle.makeBattle(0x3A382), //
        Battle.makeBattle(0x3A1DA), //douche
        Battle.makeBattle(0x3A290, new StatModifier(0,0,1,1)), //GIOVANNI
    
        //WEST OF VIRIDIAN
        Battle.makeBattle(0x3A475, new StatModifier(1,0,1,1)), //rival fight (for squirtle)
    
        //E4
        Battle.makeBattle(0x3A4BB, new StatModifier(0,0,0,3)), //LORELEI
        Battle.makeBattle(0x3A3A9, new StatModifier(0,0,1,0)), //DERP
        Battle.makeBattle(0x3A516, new StatModifier(0,0,1,3)), //TROLL
        new UnlearnMove("PSYCHIC"),
        new LearnMove("ICE BEAM"),
        Battle.makeBattle(0x3A522, new StatModifier(0,0,2,1)), //LANCE
        Battle.makeBattle(0x3A49F, new StatModifier(2,0,1,2)), //GARY MOTHERFUCKIN OAK
    };

    //the 2:02 WR from werster
    public static final GameAction[] yellowRoute = {
        //forest
        Battle.makeBattle(0x39E67),
        new LearnMove("HORN ATTACK"), //lvl 8
        Battle.makeBattle(0x39EA5),
        //lvl 9
        Battle.makeBattle(0x39E70),
        //lvl 10
        
        //brock gym
        Battle.makeBattle(0x39F17),
        new LearnMove("DOUBLE KICK"), //lvl 12
        Battle.makeBattle(0x3A454), //BROCK
        GameAction.getBoulderBadge,
        //lvl 13

        //route 3
        Battle.makeBattle(0x39E73),
        //lvl 14
        Battle.makeBattle(0x39E2F),
        //lvl 15
        Battle.makeBattle(0x39EAD), //lass
        Battle.makeBattle(0x39E7E),
        
        new Evolve("NIDORINO"), //lvl 16
        new Evolve("NIDOKING"),
        //mt moon
        Battle.makeBattle(0x39FCF),
        Battle.makeBattle(0x3A3D9),
        
        //gary + nugget bridge
        //lvl 18
        Battle.makeBattle(0x3A292), //rival fight
        
        Battle.makeBattle(0x39E8B),
        Battle.makeBattle(0x39EC4),
        Battle.makeBattle(0x39E3B),
        Battle.makeBattle(0x39EC0),
        Battle.makeBattle(0x39F1F),
        Battle.makeBattle(0x3A32F),

        //bill route
        //Battle.makeBattle(0x3A008), //elixir hiker
        Battle.makeBattle(0x3A012), // not elixir hiker
        GameAction.eatRareCandy,
        new UnlearnMove("TACKLE"),
        new LearnMove("THRASH"),
        GameAction.printStatRanges, //STAT CHECK
        Battle.makeBattle(0x39EC8), //lass
        Battle.makeBattle(0x39F1B),
        // 0x3A00C guy with 4
        Battle.makeBattle(0x39ECC),

        //vermillion before misty
        //rocket in cerulean
        Battle.makeBattle(0x3A32B),
        //route 6
        Battle.makeBattle(0x39F46),
        Battle.makeBattle(0x39F25),
        //anne gary
        Battle.makeBattle(0x3A499),

        //misty
        Battle.makeBattle(0x39F3F),
        GameAction.printAllStats,
        Battle.makeBattle(0x3A45A), //MISTY


        //surge
        Battle.makeBattle(0x3A460), //SURGE

        //route 9
        Battle.makeBattle(0x39F4E),
        Battle.makeBattle(0x39EA0),

        //rock tunnel
        Battle.makeBattle(0x39FC7),
        Battle.makeBattle(0x39FBF),
        Battle.makeBattle(0x39F64),
        Battle.makeBattle(0x3A026),
        Battle.makeBattle(0x39F8A),

        //gambler
        Battle.makeBattle(0x3A172),

        //tower gary
        //vaporeon (0)
        Battle.makeBattle(0x3A4BB),
        //flareon (1)
        //0x3A4AF

        //tower
        Battle.makeBattle(0x3A551),
        Battle.makeBattle(0x3A558),
        Battle.makeBattle(0x3A558),
        Battle.makeBattle(0x3A3E3),

        //silph
        //earthquake
        //0x3A3CA
        Battle.makeBattle(0x3A398),
        //gary
        //vaporeon
        Battle.makeBattle(0x3A4DF),
        //flareon (1)
        //0x3A4D3
        Battle.makeBattle(0x3A3E8),
        //giovanni
        Battle.makeBattle(0x3A305),

        //erika gym
        Battle.makeBattle(0x3A180),
        Battle.makeBattle(0x3A464),

        //koga gym
        Battle.makeBattle(0x3A1DF),
        Battle.makeBattle(0x3A1E5),
        Battle.makeBattle(0x3A46C),

        //sabrina
        Battle.makeBattle(0x3A47E),

        //blaine
        Battle.makeBattle(0x3A476),

        //giovanni gym
        Battle.makeBattle(0x3A421),
        Battle.makeBattle(0x3A27F),
        Battle.makeBattle(0x3A30F),

        //viridian gary
        //vaporeon (0)
        Battle.makeBattle(0x3A507),
        //flareon (1)
        //0x3A4F9

        //e4

        Battle.makeBattle(0x3A53F),
        Battle.makeBattle(0x3A448),
        Battle.makeBattle(0x3A59A),
        Battle.makeBattle(0x3A5A6),

        //e4 gary
        //vaporeon (0)
        Battle.makeBattle(0x3A531),
        //flareon (1)
        //0x3A523

    };
}
