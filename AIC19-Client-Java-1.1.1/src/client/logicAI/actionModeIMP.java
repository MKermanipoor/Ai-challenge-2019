package client.logicAI;

import client.model.*;

/**
 * Created by saeidbahmani on 2/28/19.
 */
public class actionModeIMP implements ActionMode {
    @Override
    public void actionTurn(World world) {

        Hero[] heroesEnemy =world.getOppHeroes();

        Hero[] myHeroes = world.getMyHeroes();

        //saeid begin

        //begin of guardian attack
        for (Hero myHero:myHeroes){
            int myRowHero=myHero.getCurrentCell().getRow();
            int myColumnHero=myHero.getCurrentCell().getColumn();
            if(myHero.getName()== HeroName.GUARDIAN){

                for (Hero enemyHero:heroesEnemy) {
                    if (enemyHero.getCurrentCell().getRow() != -1) {

                        int enemyRowHero = enemyHero.getCurrentCell().getRow();
                        int enemyColumnHero = enemyHero.getCurrentCell().getColumn();


                        // if (Math.abs((myRowHero - enemyRowHero) + (myColumnHero - enemyColumnHero)) <= 2) {
                        if(world.manhattanDistance(myHero.getCurrentCell(),enemyHero.getCurrentCell())<=2){
                            //// TODO: 2/27/19 bebini kodom hero hpish kamtare ono bezani

                            // TODO: 2/28/19 on chizi ke masoud kos mige
                            Direction[] dir = world.getPathMoveDirections(myHero.getCurrentCell(), enemyHero.getCurrentCell());
                            System.out.println("guardian attack");
                            if(dir.length==0){
                                world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK,myHero.getCurrentCell());
                            }
                            else {
                                switch (dir[0]) {
                                    case DOWN:
                                        world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myRowHero - 1, myColumnHero);
                                        break;
                                    case UP:
                                        world.castAbility(myHero, AbilityName.GUARDIAN_ATTACK, myRowHero + 1, myColumnHero);
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

            if(myHero.getName()==HeroName.HEALER){
                //begin of ability for heal
                // TODO: 2/27/19 mishe olaviat bandi kard vali inam khobe
                int heroId=Integer.MAX_VALUE;
                float hpRatio=Integer.MAX_VALUE;
                for (Hero needHeroHp:myHeroes){
                    if(world.manhattanDistance(myHero.getCurrentCell(),needHeroHp.getCurrentCell())<=4) {
                        float heroHpRaito = (float) (needHeroHp.getCurrentHP() / needHeroHp.getMaxHP());
                        if (heroHpRaito < hpRatio) {
                            hpRatio = heroHpRaito;
                            heroId = needHeroHp.getId();
                        }


                    }

                }
                if(heroId!=Integer.MAX_VALUE && hpRatio!=1){

                    System.out.println("healer heal");
                    world.castAbility(myHero,AbilityName.HEALER_HEAL,world.getHero(heroId).getCurrentCell());


                }
                //end of ability for heal

                //begin of ability for attack
                heroId=Integer.MAX_VALUE;
                hpRatio=Integer.MAX_VALUE;
                for(Hero enemyHero:heroesEnemy){
                    if(enemyHero.getCurrentCell().getRow()!=-1){
                        float enemyHpRatio=(float) (enemyHero.getCurrentHP()/enemyHero.getMaxHP());
                        if(world.manhattanDistance(myHero.getCurrentCell(),enemyHero.getCurrentCell())<=4){
                            if(enemyHpRatio<hpRatio){
                                hpRatio=enemyHpRatio;
                                heroId=enemyHero.getId();
                            }

                        }


                    }

                }
                if(heroId!=Integer.MAX_VALUE) {
                    System.out.println("healer attack");
                    world.castAbility(myHero, AbilityName.HEALER_ATTACK,world.getHero(heroId).getCurrentCell());
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


        boolean isEndCellvalid[]={false,false,false,false};
        Cell endCellforCounterAttack[]=new Cell[4];

        //saeid begin for counter attack


        int i = 0;
        int heroEnemyCounterid=Integer.MAX_VALUE;
        for (Hero myHero : heroes) {

            if(myHero.getName()==HeroName.GUARDIAN){




                endCellforCounterAttack[i] = myHero.getCurrentCell();
                for (Hero enemyHero : heroesEnemy) {

                    if (enemyHero.getCurrentCell().getRow() != -1) {

                        if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) > (myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getRange()+myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getAreaOfEffect() )) {



                            if(heroEnemyCounterid==Integer.MAX_VALUE){

                                heroEnemyCounterid=enemyHero.getId();
                            }
                            else {
                                if(world.getPathMoveDirections(myHero.getCurrentCell(),enemyHero.getCurrentCell()).length<world.getPathMoveDirections(myHero.getCurrentCell(),world.getHero(heroEnemyCounterid).getCurrentCell()).length){
                                    heroEnemyCounterid=enemyHero.getId();


                                }

                            }

                        }
                        //if (world.manhattanDistance(myHero.getCurrentCell(), enemyHero.getCurrentCell()) <(myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getRange()+myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getAreaOfEffect() ))
                        else  {
                            endCellforCounterAttack[i]=myHero.getCurrentCell();


                        }

                        }

                }
                if(heroEnemyCounterid!=Integer.MAX_VALUE) {
                    endCellforCounterAttack[i] = world.getHero(heroEnemyCounterid).getCurrentCell();


                }
                int correntAp=world.getAP();
                int counterAttackAp=2*myHero.getAbility(AbilityName.GUARDIAN_ATTACK).getAPCost();
                int moveCounterAttackAp=myHero.getMoveAPCost();
                if (correntAp-counterAttackAp >= moveCounterAttackAp) {
                    //   DO: 2/27/19 cell target bayad jaygozari she
                    if((myHero.getCurrentCell().getRow()!=endCellforCounterAttack[i].getRow())&&(myHero.getCurrentCell().getColumn()!=endCellforCounterAttack[i].getColumn())) {
                        // TODO: 2/28/19 getmy hero cells
                        Direction[] counterDir = world.getPathMoveDirections(myHero.getCurrentCell(), endCellforCounterAttack[i]);

                        if(counterDir.length!=0)
                            world.moveHero(myHero, counterDir[0]);

                    }

                }


            }
            i++;
        }




        //saeid end for counter attack



    }
}
