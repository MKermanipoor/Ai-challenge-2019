package client;

import client.logicAI.*;
import client.model.*;

import java.util.HashMap;
import java.util.Random;


public class AI {
    int pickTurnCount = 0;
    private HeroName pickHeroName[]={
            HeroName.HEALER,
            HeroName.GUARDIAN,
            HeroName.GUARDIAN,
            HeroName.HEALER,
    };

    private ActionMode actionMode = new actionModeIMP();

    private Random random = new Random();


    public static String TANK_1 = "tank01";
    public static String TANK_2 = "tank02";
    public static String HEALER_1 = "healer01";
    public static String HEALER_2 = "healer02";
    private java.util.Map<String, Integer> heroMap = new HashMap<>();


    // TODO: 2/25/2019 create new object
    private PreProcess preProcess;
    private PreArived preArived;

    public void preProcess(World world) {

        System.out.println("pre process started");
        preProcess = new PreProcessIMP(world);
    }

    public void pickTurn(World world) {
//        switch (pickTurnCount) {
//            case 0:
//                world.pickHero(HeroName.HEALER);
//                break;
//            case 1:
//                world.pickHero(HeroName.GUARDIAN);
//                break;
//            case 2:
//                world.pickHero(HeroName.GUARDIAN);
//                break;
//            case 3:
//                world.pickHero(HeroName.HEALER);
//                preArived = new PreArivedIMP();
//                break;
//        }
        world.pickHero(pickHeroName[pickTurnCount]);
        pickTurnCount++;
        System.out.println("pick number : "+pickTurnCount);
        if(pickTurnCount==pickHeroName.length){
            preArived = new PreArivedIMP();
        }
    }

    boolean first = false;

    public void moveTurn(World world) {




        System.out.println("move started");
        if (!first)
            Values.initial(world);

        preArived.moveTurn(world, preProcess);

        //actionMode.moveTurn(world);
    }

    boolean firstGorize = false;

    public void actionTurn(World world) {





        System.out.println("action started");
        preArived.actionTurn(world, preProcess);
    }



}
