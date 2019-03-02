package client.logicAI;

import client.model.*;

/**
 * Created by saeidbahmani on 2/28/19.
 */
public class actionModeIMP implements ActionMode {
    @Override
    public void actionTurn(World world) {
        healerAction(world);
        guardianAction(world);
    }

    private void guardianAction(World world) {
        Hero[] heroesEnemy = world.getOppHeroes();
        Hero[] myHeroes = world.getMyHeroes();

        for (Hero myHero : myHeroes) {
            if (myHero.getName() != HeroName.GUARDIAN)
                continue;


            int myRowHero = myHero.getCurrentCell().getRow();
            int myColumnHero = myHero.getCurrentCell().getColumn();

            Hero enemyTarget = null;
            int enemyTargetHP = Integer.MAX_VALUE;
            for (Hero enemyHero : heroesEnemy) {
                if (enemyHero.getCurrentCell().getRow() == -1)
                    continue;
                if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) > 2)
                    continue;
                if (enemyHero.getCurrentHP() < enemyTargetHP) {
                    enemyTargetHP = enemyHero.getCurrentHP();
                    enemyTarget = enemyHero;
                }
            }
            // Direction[] dir = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell(), Values.getMyHeroCells(world));
            if (enemyTarget == null)
                continue;

            // TODO: 3/2/2019 begay inke masir ro peyda kone bere khone doros ro peyda kono chon momkene on kone divar bashe
            Direction[] dir = world.getPathMoveDirections(myHero.getCurrentCell(), enemyTarget.getCurrentCell());
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

            //end of guardian attack


        }
    }

    private void healerAction(World world) {
        Hero[] myHeroes = world.getMyHeroes();
        Hero[] heroesEnemy = world.getOppHeroes();

        for (Hero myHero : myHeroes) {
            if (myHero.getName() != HeroName.HEALER)
                continue;
            //begin of ability for heal
            Hero heroTarget = null;
            float hpRatio = Float.MAX_VALUE;
            for (Hero needHeroHp : myHeroes) {
                if (world.manhattanDistance(myHero.getCurrentCell(), needHeroHp.getCurrentCell()) > 4)
                    continue;

                float heroHpRaito = (float) (needHeroHp.getCurrentHP() / needHeroHp.getMaxHP());
                if (heroHpRaito < hpRatio) {
                    hpRatio = heroHpRaito;
                    heroTarget = needHeroHp;
                }

            }
            if (heroTarget != null && hpRatio != 1) {
                System.out.println("healer heal");
                world.castAbility(myHero, AbilityName.HEALER_HEAL, heroTarget.getCurrentCell());
                continue;
            }
            //end of ability for heal

            //begin of ability for attack
            heroTarget = null;
            int enemyHeroHP = Integer.MAX_VALUE;
            for (Hero enemyHero : heroesEnemy) {
                if (enemyHero.getCurrentCell().getRow() == -1)
                    continue;
                if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) > 4)
                    continue;

                if (enemyHero.getCurrentHP() < enemyHeroHP) {
                    enemyHeroHP = enemyHero.getCurrentHP();
                    heroTarget = enemyHero;
                }
            }
            if (heroTarget != null) {
                System.out.println("healer attack");
                world.castAbility(myHero, AbilityName.HEALER_ATTACK, heroTarget.getCurrentCell());
            }

            //end of ability for attack

        }
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
                    if (world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell()).length < pathTargetLength) {
                        pathTargetLength = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell()).length;
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
                Direction[] directions = world.getPathMoveDirections(myHero.getCurrentCell(), target.getCurrentCell());
                if (directions != null && directions.length != 0)
                    world.moveHero(myHero, directions[0]);
            }
        }
    }


}
