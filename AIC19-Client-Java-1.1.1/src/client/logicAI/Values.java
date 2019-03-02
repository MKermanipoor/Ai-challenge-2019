package client.logicAI;

import client.model.*;

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

    public static List<Cell> getMyHeroCells(World world, Cell maghsad) {
        List<Cell> result = new ArrayList<>();
        for (Hero hero : world.getMyHeroes())
            if (maghsad == null || !hero.getCurrentCell().equals(maghsad))
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

    public static HeroConstants getHeroConstance(HeroName heroName, World world) {
        for (HeroConstants hero : world.getHeroConstants()) {
            if (hero.getName() == heroName)
                return hero;
        }
        return null;
    }

    public static AbilityConstants getAbilityConstance(AbilityName abilityName, World world) {
        for (AbilityConstants ability : world.getAbilityConstants()) {
            if (ability.getName() == abilityName)
                return ability;
        }
        return null;
    }

    public static World getWorld() {
        return world;
    }


}
