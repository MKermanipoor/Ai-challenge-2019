package client.logicAI;

import client.model.*;

import java.util.*;
import java.util.Map;
import java.util.function.Consumer;

public class NpreArivedIMP implements PreArived {
    private PreProcess preProcess;
    private ArrayList<Map.Entry<Hero, Integer>> getSortedHeroWithPathLength(World world, PreProcess destinations) {
//        Collection<Hero> t = new PriorityQueue<>(new Comparator<Hero>() {
//            @Override
//            public int compare(Hero o1, Hero o2) {
//                int a = world.getPathMoveDirections(o1.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o1.getId())), Values.getMyHeroCells(world)).length;
//                int b = world.getPathMoveDirections(o2.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(o2.getId())), Values.getMyHeroCells(world)).length;
//                return a - b;
//            }
//        });

        HashMap<Hero, Integer> t = new HashMap<>();
        for (Hero mHero :
                world.getMyHeroes()) {
            int fasele = world.getPathMoveDirections(mHero.getCurrentCell(), destinations.getBestLocation(Values.getHeroTag(mHero.getId())), Values.getMyHeroCells(world)).length;
            t.put(mHero, fasele);
        }
        ArrayList<Map.Entry<Hero, Integer>> l = new ArrayList(t.entrySet());
        l.sort(new Comparator<Map.Entry<?, Integer>>() {
            public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
//        t.addAll(Arrays.asList());
        return l;
    }

    @Override
    public void moveTurn(World world, PreProcess preProcess) {
        Hero[] heroes = world.getMyHeroes();
        int remainingAp = world.getAP();
        Set<Integer> movedHero = new HashSet<>();
        List<Cell> isBlocked = Values.getMyHeroCells(world);

        if (!checkMode(world, preProcess)) {
            remainingAp -= getSortedHeroWithPathLength(world, preProcess).get(3).getDodgeAbilities()[0].getAPCost();
        }
        for (Hero hero : heroes) {
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
        this.preProcess=preProcess;
        Hero hero = null;
        ArrayList<Map.Entry<Hero, Integer>> sortHero = getSortedHeroWithPathLength(world, preProcess);
//        hero =selectOnlyHEALER(sortHero);
        hero = selectBest(sortHero, numberOfReachTheGoal(world, preProcess));
        if (hero == null)
            return;


        if (best != null) {
            world.castAbility(hero, hero.getDodgeAbilities()[0], best);
            System.out.println("Jump");
            System.out.println(hero.getName());
            System.out.println(hero.getCurrentCell());
            System.out.println(best);
        }
    }

    private Map.Entry<Hero, Integer> selectBest(ArrayList<Map.Entry<Hero, Integer>> sortHero, int numberOfReach) {
        int sumDistance = 0;
        Map.Entry<Hero, Integer> res = null;
        for (Map.Entry<Hero, Integer> keyValueHero : sortHero) {
            sumDistance += keyValueHero.getValue();
        }
        int minDistance = sumDistance - numberOfReach;
        for (Map.Entry<Hero, Integer> keyValueHero : sortHero) {
            Hero h = keyValueHero.getKey();
            if (h.getDodgeAbilities()[0].isReady()) {
                Cell bestLocationSelect = getBestCellForJump(keyValueHero);
                int sumForThisJump = sumDistance - keyValueHero.getValue();
                sumForThisJump += Values.getWorld().getPathMoveDirections(h.getCurrentCell(), bestLocationSelect).length;
                if (minDistance > sumForThisJump) {
                    res = keyValueHero;
                }
            }
        }
        return res;
    }

    private int numberOfReachTheGoal(World world, PreProcess preProcess) {
        int number = 0;
        for (Hero hero :
                world.getMyHeroes()) {
            if (hero.getCurrentCell().equals(preProcess.getBestLocation(Values.getHeroTag(hero.getId())))) {
                number++;
            }
        }
        return 0;
    }

    private Cell getBestCellForJump(Map.Entry<Hero, Integer> keyValueHero) {
        Hero hero = keyValueHero.getKey();
        int range = hero.getDodgeAbilities()[0].getRange();
        int distance = keyValueHero.getValue();

        Cell best = null;
        Cell target = preProcess.getBestLocation(Values.getHeroTag(hero.getId()));

        Cell leftTalk = getCellFromNotWall(hero.getCurrentCell(), 0, -range);
        Cell rightTalk = getCellFromNotWall(hero.getCurrentCell(), 0, range);
        Cell topTalk = getCellFromNotWall(hero.getCurrentCell(), -range, 0);
        Cell downTalk = getCellFromNotWall(hero.getCurrentCell(), +range, 0);

        HashMap<Cell, Integer> candidaHashCell = new HashMap<>();
        candidaHashCell.put(leftTalk, Values.getWorld().getPathMoveDirections(leftTalk, target).length);
        candidaHashCell.put(rightTalk, Values.getWorld().getPathMoveDirections(rightTalk, target).length);
        candidaHashCell.put(downTalk, Values.getWorld().getPathMoveDirections(downTalk, target).length);
        candidaHashCell.put(topTalk, Values.getWorld().getPathMoveDirections(topTalk, target).length);

        Set<Cell> keySet = candidaHashCell.keySet();
        for (Cell cell :
                keySet) {
            if (distance > candidaHashCell.get(cell)) {
                best = cell;
            }
        }
//        best = l.get(0).getKey();
        return best;
    }

    private Cell getCellFromNotWall(Cell currentCell, int row, int column) {
        int length = row + column;
        for (int i = 0; i < length; i++) {
            Cell w = Values.getWorld().getMap().getCell(currentCell.getRow() + row, currentCell.getColumn() + column);
            if (!w.isWall() && Values.getWorld().getMap().isInMap(w.getRow(), w.getColumn())) {
                return w;
            }
            if (row - 1 > 0) {
                row--;
            } else {
                column--;
            }
        }
        return currentCell;
    }

    private Hero selectOnlyHEALER(ArrayList<Map.Entry<Hero, Integer>> sortHero) {
        for (Map.Entry<Hero, Integer> keyValueHero : sortHero) {
            Hero h = keyValueHero.getKey();
            if (h.getDodgeAbilities()[0].isReady() && h.getName() == HeroName.HEALER) {
                return h;
            }
        }
        return null;
    }

    @Override
    public boolean isAried(World world, PreProcess preProcess) {

        for (Hero hero : world.getMyHeroes()) {
            if (hero.getName() == HeroName.HEALER && !hero.getCurrentCell().equals(preProcess.getBestLocation(Values.getHeroTag(hero.getId()))))
                return false;
        }
        return true;
    }

    private boolean checkMode(World world, PreProcess preProcess) {
        for (Hero hero : world.getMyHeroes()) {
            if (world.manhattanDistance(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId()))) > 0)
                return false;
        }
        return true;
    }
}
