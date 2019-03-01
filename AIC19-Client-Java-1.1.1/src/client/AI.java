package client;

import client.logicAI.*;
import client.model.*;

import java.util.HashMap;
import java.util.function.BiConsumer;


public class AI {
    int pickTurnCount = 0;
    private HeroName pickHeroName[] = {
            HeroName.HEALER,
            HeroName.GUARDIAN,
            HeroName.GUARDIAN,
            HeroName.HEALER,
    };

    enum Mode {
        ACTION,
        BACK_TO_HEAL;
        public Mode change(){
            switch (this){
                case ACTION:
                    return BACK_TO_HEAL;
                case BACK_TO_HEAL:
                    return ACTION;
            }
            return ACTION;
        }
    }

    // TODO: 2/25/2019 create new object
    private PreProcess preProcess;
    private PreArived preArived;
    private ActionMode actionMode = new actionModeIMP();
    private BackToHeal backToHeal = new BackToHeal();
    private Mode mode = Mode.ACTION;

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

        if (isArived){
            checkHealerAndMove(world);
        }
        if (!isArived && !preArived.isAried(world, preProcess)) {
            System.out.println("Normal mode");
            preArived.moveTurn(world, preProcess);
        } else {
            isArived = true;
            if (isGuardianOK(world)) {
                actionMode.moveTurn(world);
                System.out.println("Action mode");
            }else {
                backToHeal.moveTurn(world);
                System.out.println("Heal mode");
            }
        }
    }

    boolean firstGorize = false;

    public void actionTurn(World world) {
        System.out.println("action started");
        if (!isArived)
            preArived.actionTurn(world, preProcess);
//        else
            actionMode.actionTurn(world);
    }

    private boolean isGuardianOK(World world){
        for (Hero hero : world.getMyHeroes()){
            if (hero.getName() != HeroName.GUARDIAN)
                continue;
            if (hero.getCurrentHP() == 0)
                continue;

            if (hero.getCurrentHP() * 3 < 2 * hero.getMaxHP())
                return false;
        }
        return true;
    }

    private void checkHealerAndMove(World world){
        for(Hero healer : world.getMyHeroes()){
            if (healer.getName() != HeroName.HEALER)
                continue;

            if (healer.getCurrentHP() == 0)
                continue;
            if (healer.getCurrentCell().equals(preProcess.getBestLocation(Values.getHeroTag(healer.getId()))))
                continue;

            Direction[] path = world.getPathMoveDirections(healer.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(healer.getId())), Values.getMyHeroCells(world));
            if (path == null || path.length == 0)
                continue;

            world.moveHero(healer, path[0]);
        }
    }

}