package client;

import client.logicAI.*;
import client.model.HeroName;
import client.model.World;


public class AI {
    int pickTurnCount = 0;
    private HeroName pickHeroName[] = {
            HeroName.HEALER,
            HeroName.GUARDIAN,
            HeroName.GUARDIAN,
            HeroName.HEALER,
    };

    private ActionMode actionMode = new actionModeIMP();


    // TODO: 2/25/2019 create new object
    private PreProcess preProcess;
    private PreArived preArived;

    public void preProcess(World world) {

        System.out.println("pre process started");
        preProcess = new PreProcessIMP(world);
    }

    public void pickTurn(World world) {
        world.pickHero(pickHeroName[pickTurnCount]);
        pickTurnCount++;
        System.out.println("pick number : " + pickTurnCount);
        if (pickTurnCount == pickHeroName.length) {
            preArived = new PreArivedIMP();
        }
    }

    boolean first = false;
    boolean isArived = false;

    public void moveTurn(World world) {

        System.out.println("move started");
        if (!first)
            Values.initial(world);

        if (!isArived && !preArived.isAried(world, preProcess)) {
            System.out.println("Normal mode");
            preArived.moveTurn(world, preProcess);
        } else {
            isArived = true;
            actionMode.moveTurn(world);
            System.out.println("Action mode");
        }
    }

    boolean firstGorize = false;

    public void actionTurn(World world) {
        System.out.println("action started");
        if (isArived)
            actionMode.actionTurn(world);
        else
            preArived.actionTurn(world, preProcess);
    }


}
