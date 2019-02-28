package client;

import client.logicAI.*;
import client.model.HeroName;
import client.model.World;

import java.util.Random;

public class AI {
    int pickTurnCount = 0;
    private HeroName pickHeroName[]={
            HeroName.HEALER,
            HeroName.GUARDIAN,
            HeroName.GUARDIAN,
            HeroName.HEALER,
    };
    private Random random = new Random();


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

    }

    boolean firstGorize = false;

    public void actionTurn(World world) {
        System.out.println("action started");
        preArived.actionTurn(world, preProcess);
    }

}
