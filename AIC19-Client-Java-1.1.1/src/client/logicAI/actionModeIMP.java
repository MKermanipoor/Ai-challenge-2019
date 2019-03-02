package client.logicAI;

import client.model.*;

/**
 * Created by saeidbahmani on 2/28/19.
 */
public class actionModeIMP implements ActionMode {
    @Override
    public void actionTurn(World world) {

        Hero[] heroesEnemy = world.getOppHeroes();

        Hero[] myHeroes = world.getMyHeroes();

        //saeid begin

        //begin of guardian attack
        for (Hero myHero : myHeroes) {
            int myRowHero = myHero.getCurrentCell().getRow();
            int myColumnHero = myHero.getCurrentCell().getColumn();
            if (myHero.getName() == HeroName.GUARDIAN) {

                for (Hero enemyHero : heroesEnemy) {
                    if (enemyHero.getCurrentCell().getRow() != -1) {

                        int enemyRowHero = enemyHero.getCurrentCell().getRow();
                        int enemyColumnHero = enemyHero.getCurrentCell().getColumn();


                        // if (Math.abs((myRowHero - enemyRowHero) + (myColumnHero - enemyColumnHero)) <= 2) {
                        if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) <= 2) {
                            //// TODO: 2/27/19 bebini kodom hero hpish kamtare ono bezani

                           // Direction[] dir = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell(), Values.getMyHeroCells(world));
                            Direction[] dir = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell());
                            System.out.println("guardian attack");
                            if (dir.length == 0) {
                                world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myHero.getCurrentCell());
                            } else {
                                switch (dir[0]) {
                                    case DOWN:
                                        world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myRowHero + 1, myColumnHero);
                                        break;
                                    case UP:
                                        world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myRowHero - 1, myColumnHero);
                                        break;
                                    case LEFT:
                                        world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myRowHero, myColumnHero - 1);
                                        break;
                                    case RIGHT:
                                        world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myRowHero, myColumnHero + 1);
                                        break;

                                }
                            }

                            //in break bara ine ke vaghti ability ro estefade kard for time hadar nade
                            //break;
                        }

                    }

                }
                //end of guardian attack
            }

            if (myHero.getName() == HeroName.HEALER) {
                //begin of ability for heal
                // TODO: 2/27/19 mishe olaviat bandi kard vali inam khobe
                int heroId = Integer.MAX_VALUE;
                float hpRatio = Integer.MAX_VALUE;
                for (Hero needHeroHp : myHeroes) {
                    if (world.manhattanDistance(myHero.getCurrentCell(), needHeroHp.getCurrentCell()) <= 4) {
                        float heroHpRaito = (float) (needHeroHp.getCurrentHP() / needHeroHp.getMaxHP());
                        if (heroHpRaito < hpRatio) {
                            hpRatio = heroHpRaito;
                            heroId = needHeroHp.getId();
                        }


                    }

                }
                if (heroId != Integer.MAX_VALUE && hpRatio != 1) {

                    System.out.println("healer heal");
                    world.castAbility(myHero, AbilityName.HEALER_HEAL, world.getHero(heroId).getCurrentCell());


                }
                //end of ability for heal

                //begin of ability for attack
                heroId = Integer.MAX_VALUE;
                hpRatio = Integer.MAX_VALUE;
                for (Hero enemyHero : heroesEnemy) {
                    if (enemyHero.getCurrentCell().getRow() != -1) {
                        float enemyHpRatio = (float) (enemyHero.getCurrentHP() / enemyHero.getMaxHP());
                        if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) <= 4) {
                            if (enemyHpRatio < hpRatio) {
                                hpRatio = enemyHpRatio;
                                heroId = enemyHero.getId();
                            }

                        }


                    }

                }
                if (heroId != Integer.MAX_VALUE) {
                    System.out.println("healer attack");
                    world.castAbility(myHero, AbilityName.HEALER_ATTACK, world.getHero(heroId).getCurrentCell());
                }

                //end of ability for attack

            }


        }


        //saeid end

    }

    @Override
    public void moveTurn(World world) {
        Hero[] heroes = world.getMyHeroes();
        Hero[] heroesEnemy = world.getOppHeroes();

        for (Hero myHero : heroes) {
            if (myHero.getName() != HeroName.GUARDIAN)
                continue;

            Hero target = null;
            int pathTargetLength = Integer.MAX_VALUE;


            for (Hero enemyHero : heroesEnemy) {
                if (enemyHero.getCurrentCell().getRow() == -1)
                    continue;

                if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) > (myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getRange() + myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getAreaOfEffect())) {
                    if (world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell(), Values.getMyHeroCells(world)).length < pathTargetLength) {
                        pathTargetLength = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell(), Values.getMyHeroCells(world)).length;
                        target = enemyHero;
                    }
                } else {
                    target = null;
                    break;
                }
            }

            if (target == null)
                continue;

            int moveCounterAttackAp = myHero.getMoveAPCost();
            int remainingAp = world.getAP() - 2 * myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getAPCost();

            if (remainingAp >= moveCounterAttackAp) {
                Direction[] directions = world.getPathMoveDirections(myHero.getCurrentCell(), target.getCurrentCell(), Values.getMyHeroCells(world));
                if (directions != null && directions.length != 0)
                    world.moveHero(myHero, directions[0]);
            }
        }


        //saeid end for counter attack


    }
}
