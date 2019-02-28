package client.logicAI;

import client.model.Cell;
import client.model.Map;
import client.model.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class PreProcessIMP implements PreProcess {
    private HashMap<String, Cell> cellLocation = new HashMap<>();
    private HashMap<Cell, Integer> distanceMyHashMap = new HashMap<>();
    private HashMap<Cell, Integer> distanceOppHashMap = new HashMap<>();
    private ArrayList<Cell> inUseCell=new ArrayList<>(4);
    private final World world;
    private int row, column;

    public PreProcessIMP(World world) {
        this.world = world;
    }

    @Override
    public HashMap<String, Cell> getBestLocation() {
        if (cellLocation.size() == 0) {


            Map map = world.getMap();
            Cell[] goalCell = map.getObjectiveZone();
            Cell[] myHeroCell = map.getMyRespawnZone();
            Cell firstBestCell;
            int distance;
            row = 0;
            column = 0;
            for (Cell workCell :
                    myHeroCell) {
                row += workCell.getRow();
                column += workCell.getColumn();
            }
            row = Math.round(row / myHeroCell.length);
            column = Math.round(column / myHeroCell.length);

            //opp
            int oppRow = 0;
            int oppColumn = 0;
            for (Cell workCell :
                    map.getOppRespawnZone()) {
                oppRow += workCell.getRow();
                oppColumn += workCell.getColumn();
            }
            oppRow = Math.round(oppRow / map.getOppRespawnZone().length);
            oppColumn = Math.round(oppColumn / map.getOppRespawnZone().length);

            //find best goal cell
            firstBestCell = goalCell[0];
            distance = getDistance(firstBestCell);
            distanceMyHashMap.put(firstBestCell, distance);
            for (int i = 1; i < goalCell.length; i++) {
                Cell workCell = goalCell[i];
                int d = getDistance(workCell);
                distanceMyHashMap.put(workCell, d);
                distanceOppHashMap.put(workCell, world.getPathMoveDirections(oppRow,oppColumn, workCell.getRow(), workCell.getColumn()).length);
                if (distance > d) {
                    firstBestCell = workCell;
                    distance = d;
                }
            }
            Cell secondBestCell = null;
            //firstBestCell is helper first location
            Random random = new Random();
//            if (random.nextBoolean()) {
//                secondBestCell = checkIsWall(getCellBy(firstBestCell, 2, 0));
//            } else {
//                secondBestCell = checkIsWall(getCellBy(firstBestCell, -2, 0));
//            }
            secondBestCell=checkIsWallMin(firstBestCell,1);
            //Tonk
            Cell firstTonk = checkIsWallMax(firstBestCell,  2);
            Cell secondTonk = checkIsWallMax(secondBestCell,2);
            //hasVariable
            cellLocation.put(Values.HEALER_1, firstBestCell);
            cellLocation.put(Values.HEALER_2, secondBestCell);
            cellLocation.put(Values.TANK_1, firstTonk);
            cellLocation.put(Values.TANK_2, secondTonk);
            printHash();
        }
        return cellLocation;
    }



    private void printHash() {
        Set<String> keys = cellLocation.keySet();
        for (String key :
                keys) {
            System.out.println(key + " in :( " + cellLocation.get(key).getRow() + " , " + cellLocation.get(key).getColumn() + " )");
        }
    }

    private Cell checkIsWallMin(Cell secondBestCell,int minDistans) {
//        if (!secondBestCell.isInObjectiveZone()) {
//            findHelarLocation:
        Cell maghsad=secondBestCell;
            for (int j = minDistans; j < minDistans+2; j++) {
                for (int i = j; i > 0; i--) {
                    int k = j - i;
                    int d=Integer.MIN_VALUE;
                    if (checkCell(getCellBy(secondBestCell, i, k))&&d<distanceOppHashMap.get(getCellBy(secondBestCell, i, k))) {
                        maghsad = getCellBy(secondBestCell, i, k);
                        d=distanceOppHashMap.get(getCellBy(secondBestCell, i, k));
//                        break findHelarLocation;
                    }
                    if (checkCell(getCellBy(secondBestCell, -i, k))&&d<distanceOppHashMap.get(getCellBy(secondBestCell, -i, k))) {
                        maghsad = getCellBy(secondBestCell, -i, k);
                        d=distanceOppHashMap.get(getCellBy(secondBestCell, -i, k));
//                        break findHelarLocation;
                    }
                    if (checkCell(getCellBy(secondBestCell, i, -k))&&d<distanceOppHashMap.get(getCellBy(secondBestCell, i, -k))) {
                        maghsad = getCellBy(secondBestCell, i, -k);
                        d=distanceOppHashMap.get(getCellBy(secondBestCell, i, -k));
//                        break findHelarLocation;
                    }
                    if (checkCell(getCellBy(secondBestCell, -i, -k))&&d<distanceOppHashMap.get(getCellBy(secondBestCell, -i, -k))) {
                        maghsad = getCellBy(secondBestCell, -i, -k);
                        d=distanceOppHashMap.get(getCellBy(secondBestCell, -i, -k));
//                        break findHelarLocation;
                    }
                }
            }
//        }
        inUseCell.add(maghsad);
        return maghsad;
    }

    private Cell checkIsWallMax(Cell secondBestCell,int minDistans) {
//        if (!secondBestCell.isInObjectiveZone()) {
//            findHelarLocation:
        Cell maghsad=secondBestCell;
        for (int j = minDistans; j < minDistans+2; j++) {
            for (int i = j; i > 0; i--) {
                int k = j - i;
                int d=Integer.MAX_VALUE;
                if (
                        checkCell(getCellBy(secondBestCell, i, k))
                        &&d>distanceOppHashMap.get(getCellBy(secondBestCell, i, k))) {
                    maghsad = getCellBy(secondBestCell, i, k);
                    d=distanceOppHashMap.get(getCellBy(secondBestCell, i, k));
//                        break findHelarLocation;
                }
                if (checkCell(getCellBy(secondBestCell, -i, k))&&d>distanceOppHashMap.get(getCellBy(secondBestCell, -i, k))) {
                    maghsad = getCellBy(secondBestCell, -i, k);
                    d=distanceOppHashMap.get(getCellBy(secondBestCell, -i, k));
//                        break findHelarLocation;
                }
                if (checkCell(getCellBy(secondBestCell, i, -k))&&d>distanceOppHashMap.get(getCellBy(secondBestCell, i, -k))) {
                    maghsad = getCellBy(secondBestCell, i, -k);
                    d=distanceOppHashMap.get(getCellBy(secondBestCell, i, -k));
//                        break findHelarLocation;
                }
                if (checkCell(getCellBy(secondBestCell, -i, -k))&&d>distanceOppHashMap.get(getCellBy(secondBestCell, -i, -k))) {
                    maghsad = getCellBy(secondBestCell, -i, -k);
                    d=distanceOppHashMap.get(getCellBy(secondBestCell, -i, -k));
//                        break findHelarLocation;
                }
            }
        }
//        }
        inUseCell.add(maghsad);
        return maghsad;
    }

    private boolean checkCell(Cell cellBy) {
        return cellBy != null && cellBy.isInObjectiveZone()&&!inUseCell.contains(cellBy);
    }

    private Cell getCellBy(Cell goalCell, int row, int column) {
        return world.getMap().getCell(goalCell.getRow() + row, goalCell.getColumn() + column);
    }

    private int getDistance(Cell cell) {
        return world.getPathMoveDirections(row, column, cell.getRow(), cell.getColumn()).length;
    }

    @Override
    public Cell getBestLocation(String hero) {
        return getBestLocation().get(hero);
    }
}
