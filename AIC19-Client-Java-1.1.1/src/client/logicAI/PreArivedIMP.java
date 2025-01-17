package client.logicAI;

import client.model.*;
import client.model.Map;

import java.util.*;
import java.util.function.Consumer;

public class PreArivedIMP implements PreArived {

    private List<Hero> getSortedHeroWithPathLength(World world, PreProcess destinations){
        Collection<Hero> t =new PriorityQueue<>(new Comparator<Hero>() {
            @Override
            public int compare(Hero o1, Hero o2) {
                int a = world.getPathMoveDirections(o1.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o1.getId())), Values.getMyHeroCells(world)).length;
                int b = world.getPathMoveDirections(o2.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o2.getId())), Values.getMyHeroCells(world)).length;
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
        Set<Integer> movedHero = new HashSet<>();
        List<Cell> isBlocked  = Values.getMyHeroCells(world);

        if (!checkMode(world, preProcess)){
            remainingAp -= getSortedHeroWithPathLength(world, preProcess).get(3).getDodgeAbilities()[0].getAPCost();
        }
        for (Hero hero : heroes){
            if (Values.getMyHeroCells(world).size() != 4)
                System.out.print("\n************\n\n\nerror !!!!!\n************\n\n\n");
            Direction[] directions = world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId())), Values.getMyHeroCells(world));
            if (directions.length == 0) {
                System.out.println(Values.getHeroTag(hero.getId()) + "Aried");
                continue;
            }
            if (remainingAp >= hero.getMoveAPCost()) {
                remainingAp -= hero.getMoveAPCost();
                movedHero.add(hero.getId());
                isBlocked.remove(hero.getCurrentCell());
                world.moveHero(hero, directions[0]);
                System.out.println("***************\n" + Values.getHeroTag(hero.getId()) + "\t" + directions[0] + "\n***************\n");
            }
        }

        movedHero.forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                Hero hero = world.getHero(integer);
                Direction d = world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(integer)), isBlocked)[0];
                world.moveHero(hero, d);
            }
        });
    }

    @Override
    public void actionTurn(World world, PreProcess preProcess) {
        System.out.println(world.getAP());
        Hero hero = null;
        for(Hero h:getSortedHeroWithPathLength(world, preProcess)){
            if (h.getDodgeAbilities()[0].isReady()) {
                hero = h;
            }
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
                int row = hero.getCurrentCell().getRow() + i;
                int column = hero.getCurrentCell().getColumn() + j;
                Cell cell = world.getMap().getCell(row, column);
                if (!world.getMap().isInMap(row, column))
                    continue;
                if (cell.isWall())
                    continue;
                if (world.getMyHero(row, column) != null)
                    continue;

                Direction[] path = world.getPathMoveDirections(target, cell);
                if (path.length < distance){
                    distance = path.length;
                    best = cell;
                }
                if (path.length == distance && !cell.isInVision()){
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

    @Override
    public boolean isAried(World world, PreProcess preProcess) {

        for (Hero hero:world.getMyHeroes()){
            if (hero.getName() == HeroName.HEALER && !hero.getCurrentCell().equals(preProcess.getBestLocation(Values.getHeroTag(hero.getId()))))
                return false;
        }
        return true;
    }

    public boolean checkMode(World world, PreProcess preProcess){
        for (Hero hero:world.getMyHeroes()){
            if (world.manhattanDistance(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId()))) > 0)
                return false;
        }
        return true;
    }
}
