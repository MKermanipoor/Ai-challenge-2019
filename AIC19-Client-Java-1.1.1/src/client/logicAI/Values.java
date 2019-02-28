package client.logicAI;

import client.model.Cell;
import client.model.Hero;
import client.model.HeroName;
import client.model.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class Values {
    public static final String TANK_1 = "tank01";
    public static final String TANK_2 = "tank02";
    public static final String HEALER_1 = "healer01";
    public static final String HEALER_2 = "healer02";
    private static java.util.Map<String, Integer> heroMap = new HashMap<>();
    private static World world;

    public static String getHeroTag(Integer id) {
        final String[] result = new String[1];
        heroMap.forEach(new BiConsumer<String, Integer>() {
            @Override
            public void accept(String s, Integer integer) {
                if (id.intValue() == integer.intValue()) {
                    result[0] = s;
                }
            }
        });
        return result[0];
    }

    public static List<Cell> getMyHeroCells(World world) {
        List<Cell> result = new ArrayList<>();
        for (Hero hero : world.getMyHeroes())
            result.add(hero.getCurrentCell());
        return result;
    }

    public static void initial(World world) {
        if (Values.world == null) {
            Values.world = world;
            for (Hero hero : Values.world.getMyHeroes()) {
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
        }
    }

    public static World getWorld() {
        return world;
    }
}
