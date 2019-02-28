package client;

import client.logicAI.ActionMode;
import client.logicAI.actionModeIMP;
import client.logicAI.preProcess;
import client.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class AI {

    private ActionMode actionMode = new actionModeIMP();

    private Random random = new Random();

    private World world;

    public static String TANK_1 = "tank01";
    public static String TANK_2 = "tank02";
    public static String HEALER_1 = "healer01";
    public static String HEALER_2 = "healer02";
    private java.util.Map<String, Integer> heroMap = new HashMap<>();

    private String getHeroTag(Integer id){
        final String[] result = new String[1];
        heroMap.forEach(new BiConsumer<String, Integer>() {
            @Override
            public void accept(String s, Integer integer) {
                if (id.intValue() == integer.intValue()){
                    result[0] = s;
                }
            }
        });
        return result[0];
    }

    private List<Cell> getMyHeroCells(){
        List<Cell> result = new ArrayList<>();
        for (Hero hero:world.getMyHeroes())
            result.add(hero.getCurrentCell());
        return result;
    }


    // TODO: 2/25/2019 create new object
    private preProcess preProcess = new preProcess() {
        @Override
        public HashMap<String, Cell> getBestLocation() {
            return null;
        }

        @Override
        public Cell getBestLocation(String hero) {
            return world.getMap().getObjectiveZone()[0];
        }
    };

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
                break;
        }
        pickTurnCount++;
    }

    boolean isValidData = false;

    //saeid start




    int countMoveCounter =0;




    //saeid end

    public void moveTurn(World world) {


        countMoveCounter++;

        System.out.println("move started");
        this.world = world;
        Hero[] heroes = world.getMyHeroes();


        if (!isValidData) {
            for (Hero hero : heroes) {
                if (hero.getName() == HeroName.GUARDIAN) {
                    if (heroMap.containsKey(TANK_1)) {
                        heroMap.put(TANK_2, hero.getId());
                    } else {
                        heroMap.put(TANK_1, hero.getId());
                    }
                } else if (hero.getName() == HeroName.HEALER) {
                    if (heroMap.containsKey(HEALER_1)) {
                        heroMap.put(HEALER_2, hero.getId());
                    } else {
                        heroMap.put(HEALER_1, hero.getId());
                    }
                }
            }
            isValidData = true;
        }

        for (Hero hero : heroes) {
            Direction[] dir = world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(getHeroTag(hero.getId())), getMyHeroCells());
            System.out.println(dir.length);
            if (dir.length > 0)
                world.moveHero(hero, dir[0]);
        }

        actionMode.moveTurn(world);




    }

    boolean firstGorize = false;

    public void actionTurn(World world) {




        countMoveCounter=0;
        System.out.println("action started");


        Map map = world.getMap();



        actionMode.actionTurn(world);







    }



}
