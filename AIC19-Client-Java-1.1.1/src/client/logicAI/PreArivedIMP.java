package client.logicAI;

import client.model.Cell;
import client.model.Direction;
import client.model.Hero;
import client.model.World;

import java.util.*;

public class PreArivedIMP implements PreArived {

    private List<Hero> getSortedHeroWithPathLength(World world, PreProcess destinations){
        Collection<Hero> t =new PriorityQueue<>(new Comparator<Hero>() {
            @Override
            public int compare(Hero o1, Hero o2) {
                int a = world.getPathMoveDirections(o1.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o1.getId()))).length;
                int b = world.getPathMoveDirections(o2.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o2.getId()))).length;
                return a-b;
            }
        });
        t.addAll(Arrays.asList(world.getMyHeroes()));
        return new ArrayList<>(t);
    }

    @Override
    public void moveTurn(World world, PreProcess preProcess) {
        Hero[] heroes = world.getMyHeroes();
        int remainingAp = world.getAP();
        if (!checkMode(world, preProcess)){
            remainingAp -= getSortedHeroWithPathLength(world, preProcess).get(3).getDodgeAbilities()[0].getAPCost();
        }
        for (Hero hero : heroes){
            Direction[] directions = world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId())), Values.getMyHeroCells());
            if (directions.length == 0) {
                System.out.println("Aried");
                continue;
            }
            if (remainingAp >= hero.getMoveAPCost()) {
                world.moveHero(hero, directions[0]);
                remainingAp -= hero.getMoveAPCost();
            }
        }
    }

    @Override
    public void actionTurn(World world, PreProcess preProcess) {
        System.out.println(world.getAP());
        Hero hero = null;
        for(Hero h:getSortedHeroWithPathLength(world, preProcess)){
            if (h.getDodgeAbilities()[0].isReady())
                hero = h;
        }
        if (hero == null)
            return;

        int range = hero.getDodgeAbilities()[0].getRange();
        Cell best = null;
        Cell target = preProcess.getBestLocation(Values.getHeroTag(hero.getId()));
        int distance = world.getPathMoveDirections(hero.getCurrentCell(), target).length;
        for(int i = -range ; i <= range ; i++){
            int temp = range - Math.abs(i);
            for (int j = -temp ; j <= temp ; j++){
//                if (Math.abs(i + j) == 1)
//                    continue;
                int row = hero.getCurrentCell().getRow() + i;
                int column = hero.getCurrentCell().getColumn() + j;
                Cell cell = world.getMap().getCell(row, column);
                if (!world.getMap().isInMap(row, column))
                    continue;
                if (cell.isWall())
                    continue;
                if (world.getMyHero(row, column) != null)
                    continue;
                if (world.getPathMoveDirections(target, cell).length < distance){
                    distance = world.manhattanDistance(target, cell);
                    best = cell;
                }
                if (world.getPathMoveDirections(target, cell).length == distance &&
                        !cell.isInVision()){
                    best = cell;
                }
            }
        }
        if (best != null) {
            world.castAbility(hero, hero.getDodgeAbilities()[0], best);
            System.out.println("Jump");
            System.out.println(hero.getName());
            System.out.println(hero.getCurrentCell());
            System.out.println(best);
        }
    }

    public boolean checkMode(World world, PreProcess preProcess){
        for (Hero hero:world.getMyHeroes()){
            if (world.manhattanDistance(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId()))) > 0)
                return false;
        }
        return true;
    }
}
