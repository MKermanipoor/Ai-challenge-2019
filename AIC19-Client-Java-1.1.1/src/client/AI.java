package client;

import client.logicAI.PreArived;
import client.logicAI.PreArivedIMP;
import client.logicAI.Values;
import client.logicAI.preProcess;
import client.model.Cell;
import client.model.HeroName;
import client.model.World;

import java.util.HashMap;
import java.util.Random;

public class AI {

    private Random random = new Random();


    // TODO: 2/25/2019 create new object
    private preProcess preProcess = new preProcess() {
        @Override
        public HashMap<String, Cell> getBestLocation() {
            return null;
        }

        @Override
        public Cell getBestLocation(String hero) {
            return Values.getWorld().getMap().getObjectiveZone()[0];
        }
    };
    private PreArived preArived;

    public void preProcess(World world) {

        System.out.println("pre process started");
    }

    int pickTurnCount = 0;

    public void pickTurn(World world) {
        System.out.println("pick started");
        switch (pickTurnCount) {
            case 0:
                world.pickHero(HeroName.HEALER);
                break;
            case 1:
                world.pickHero(HeroName.GUARDIAN);
                break;
            case 2:
                world.pickHero(HeroName.GUARDIAN);
                break;
            case 3:
                world.pickHero(HeroName.HEALER);
                preArived = new PreArivedIMP();
                break;
        }
        pickTurnCount++;
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
