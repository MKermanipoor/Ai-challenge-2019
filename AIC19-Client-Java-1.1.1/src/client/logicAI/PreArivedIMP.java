package client.logicAI;

import client.model.Direction;
import client.model.Hero;
import client.model.World;

public class PreArivedIMP implements PreArived {



    @Override
    public void moveTurn(World world, preProcess preProcess) {
        Hero[] heroes = world.getMyHeroes();
        
        for (Hero hero : heroes){
            Direction[] dir = world.getPathMoveDirections(hero.getCurrentCell(), preProcess.getBestLocation(Values.getHeroTag(hero.getId())), Values.getMyHeroCells());
            System.out.println(dir.length);
            if (dir.length > 0)
                world.moveHero(hero, dir[0]);
        }
    }

    @Override
    public void actionTurn(World world) {

    }
}
