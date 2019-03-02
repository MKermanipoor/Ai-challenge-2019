package client.logicAI;

import client.model.*;

public class BackToHeal {
    public void moveTurn(World world){
        for (Hero guardian:world.getMyHeroes()){
            if (guardian.getName() != HeroName.GUARDIAN)
                continue;

            int remainingAp = world.getAP();
            for(Hero h:world.getMyHeroes()){
                if (h.getName() != HeroName.GUARDIAN)
                    continue;
                if (h.getCurrentHP() == 0)
                    continue;
                if (h.getCurrentHP() * 5 < 3 * h.getMaxHP())
                    remainingAp -= Values.getAbilityConstance(AbilityName.HEALER_HEAL, world).getAPCost();
            }
            if (remainingAp < guardian.getMoveAPCost())
                continue;

            Hero target = null;
            int pathDistance = Integer.MAX_VALUE;
            for (Hero healer : world.getMyHeroes()){
                if (healer.getName() != HeroName.HEALER)
                    continue;

                if (world.manhattanDistance(healer.getCurrentCell(), guardian.getCurrentCell()) <= Values.getAbilityConstance(AbilityName.HEALER_HEAL, world).getRange() - 1){
                    target = null;
                    break;
                }

                Direction[] path = world.getPathMoveDirections(guardian.getCurrentCell(), healer.getCurrentCell());
                if (path.length < pathDistance){
                    pathDistance = path.length;
                    target = healer;
                }
            }

            if (target == null)
                continue;

            Direction[] path = world.getPathMoveDirections(guardian.getCurrentCell(), target.getCurrentCell());
            if (path == null || path.length == 0)
                continue;

            world.moveHero(guardian, path[0]);
        }
    }
}
