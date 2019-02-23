package client.logicAI;

import client.model.Cell;

import java.util.HashMap;

public interface preProcess {
    HashMap<Integer,Cell> getBestLocation();//hero id , best location cell
}
