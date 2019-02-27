package client;

import client.logicAI.preProcess;
import client.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class AI {

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
    public void moveTurn(World world) {
        System.out.println("move started");
        this.world = world;
        Hero[] heroes = world.getMyHeroes();

        if (!isValidData){
            for (Hero hero:heroes){
                if (hero.getName() == HeroName.GUARDIAN){
                    if (heroMap.containsKey(TANK_1)){
                        heroMap.put(TANK_2, hero.getId());
                    }else {
                        heroMap.put(TANK_1, hero.getId());
                    }
                }else if(hero.getName() == HeroName.HEALER){
                    if (heroMap.containsKey(HEALER_1)){
                        heroMap.put(HEALER_2, hero.getId());
                    }else{
                        heroMap.put(HEALER_1, hero.getId());
                    }
                }
            }
            isValidData = true;
        }

        for (Hero hero : heroes){
            Direction[] dir = world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(getHeroTag(hero.getId())), getMyHeroCells());
            System.out.println(dir.length);
            if (dir.length > 0)
                world.moveHero(hero, dir[0]);
        }
    }

    boolean firstGorize = false;

    public void actionTurn(World world) {
        System.out.println("action started");

        Hero[] heroesEnemy =world.getOppHeroes();

        Hero[] myHeroes = world.getMyHeroes();
        Map map = world.getMap();

        int middleRow = 0;
        for (Hero hero : myHeroes) {
            middleRow += hero.getCurrentCell().getRow();

        }

        //saeid begin

        for (Hero myHero:myHeroes){
            int myRowHero=myHero.getCurrentCell().getRow();
            int myColumnHero=myHero.getCurrentCell().getColumn();
            if(myHero.getName()==HeroName.GUARDIAN){

                for (Hero enemyHero:heroesEnemy) {
                    if (enemyHero.getCurrentCell().getRow() != -1) {

                        int enemyRowHero = enemyHero.getCurrentCell().getRow();
                        int enemyColumnHero = enemyHero.getCurrentCell().getColumn();


                       // if (Math.abs((myRowHero - enemyRowHero) + (myColumnHero - enemyColumnHero)) <= 2) {
                        if(world.manhattanDistance(myHero.getCurrentCell(),enemyHero.getCurrentCell())<=2){
                            //// TODO: 2/27/19 bebini kodom hero hpish kamtare ono bezani
                            Direction[] dir = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell(), getMyHeroCells());
                            System.out.println("guardian attack");
                            switch (dir[0]){
                                case DOWN:
                                    world.castAbility(myHero,AbilityName.GUARDIAN_ATTACK,myRowHero-1,myColumnHero);
                                    break;
                                case UP:
                                    world.castAbility(myHero,AbilityName.GUARDIAN_ATTACK,myRowHero+1,myColumnHero);
                                    break;
                                case LEFT:
                                    world.castAbility(myHero,AbilityName.GUARDIAN_ATTACK,myRowHero,myColumnHero-1);
                                    break;
                                case RIGHT:
                                    world.castAbility(myHero,AbilityName.GUARDIAN_ATTACK,myRowHero,myColumnHero+1);
                                    break;

                            }
                            //in break bara ine ke vaghti ability ro estefade kard for time hadar nade
                            break;
                        }

                    }

                }
            }

            if(myHero.getName()==HeroName.HEALER){
                //begin of ability for heal
                // TODO: 2/27/19 mishe olaviat bandi kard vali inam khobe
                int heroId=Integer.MAX_VALUE;
                int hpRatio=Integer.MAX_VALUE;
                for (Hero needHeroHp:myHeroes){
                    if(world.manhattanDistance(myHero.getCurrentCell(),needHeroHp.getCurrentCell())<=4) {
                        int heroHpRaito = needHeroHp.getCurrentHP() / needHeroHp.getMaxHP();
                        if (heroHpRaito < hpRatio) {
                            hpRatio = heroHpRaito;
                            heroId = needHeroHp.getId();
                        }


                    }

                }
                if(heroId!=Integer.MAX_VALUE){
                if(hpRatio!=1){
                    System.out.println("healer heal");
                    world.castAbility(heroId,AbilityName.HEALER_HEAL,world.getHero(heroId).getCurrentCell());
                }

                }
                //end of ability for heal

                //begin of ability for attack
                 heroId=Integer.MAX_VALUE;
                 hpRatio=Integer.MAX_VALUE;
                for(Hero enemyHero:heroesEnemy){
                    if(enemyHero.getCurrentCell().getRow()!=-1){
                        int enemyHpRatio=enemyHero.getCurrentHP()/enemyHero.getMaxHP();
                        if(world.manhattanDistance(myHero.getCurrentCell(),enemyHero.getCurrentCell())<=4){
                            if(enemyHpRatio<hpRatio){
                                hpRatio=enemyHpRatio;
                                heroId=enemyHero.getId();
                            }

                        }


                    }

                }
                if(heroId!=Integer.MAX_VALUE) {
                    System.out.println("healer attack");
                    world.castAbility(myHero, AbilityName.HEALER_ATTACK,world.getHero(heroId).getCurrentCell());
                }

                //end of ability for attack

            }



        }


        //saeid end







    }

}
