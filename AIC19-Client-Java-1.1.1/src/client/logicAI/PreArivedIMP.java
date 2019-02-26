package client.logicAI;

import client.model.Cell;
import client.model.Direction;
import client.model.Hero;
import client.model.World;

import java.util.*;

public class PreArivedIMP implements PreArived {

    private List<Hero> getSortedHeroWithPathLength(World world, preProcess destinations){
        Collection<Hero> t =new PriorityQueue<>(new Comparator<Hero>() {
            @Override
            public int compare(Hero o1, Hero o2) {
                int a = world.getPathMoveDirections(o1.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o1.getId()))).length;
                int b = world.getPathMoveDirections(o2.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o2.getId()))).length;
                return b-a;
            }
        });
        t.addAll(Arrays.asList(world.getMyHeroes()));
        return new ArrayList<>(t);
    }

    @Override
    public void moveTurn(World world, preProcess preProcess) {
        Hero[] heroes = world.getMyHeroes();
        int remainingAp = world.getAP();
        if (!checkMode(world, preProcess)){
            remainingAp -= getSortedHeroWithPathLength(world, preProcess).get(3).getDodgeAbilities()[0].getAPCost();
        }
        for (Hero hero : heroes){
            if (remainingAp >= hero.getMoveAPCost()) {
                world.moveHero(hero, world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId())))[0]);
                remainingAp -= hero.getMoveAPCost();
            }
        }
    }

    @Override
    public void actionTurn(World world, preProcess preProcess) {
        Hero hero = getSortedHeroWithPathLength(world, preProcess).get(3);
        int range = hero.getDodgeAbilities()[0].getRange();
        Cell best = null;
        Cell target = preProcess.getBestLocation(Values.getHeroTag(hero.getId()));
        int distance = Integer.MAX_VALUE;
        for(int i = -range ; i <= range ; i++){
            int temp = range - Math.abs(i);
            for (int j = -temp ; j <= temp ; j++){
                int row = hero.getCurrentCell().getRow() + i;
                int column = hero.getCurrentCell().getColumn() + j;
                if (!world.getMap().isInMap(row, column))
                    continue;
                if (world.getMap().getCell(row, column).isWall())
                    continue;
                if (world.manhattanDistance(target, world.getMap().getCell(row, column)) < distance){
                    distance = world.manhattanDistance(target, world.getMap().getCell(row, column));
                    best = world.getMap().getCell(row, column);
                }
            }
        }
        if (best != null)
            world.castAbility(hero, hero.getDodgeAbilities()[0], best);
    }

    public boolean checkMode(World world, preProcess preProcess){
        for (Hero hero:world.getMyHeroes()){
            if (world.manhattanDistance(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId()))) > 0)
                return false;
        }
        return true;
    }
}
