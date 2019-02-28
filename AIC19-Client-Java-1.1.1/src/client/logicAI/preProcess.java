package client.logicAI;

import client.model.Cell;

import java.util.HashMap;

public interface PreProcess {
    HashMap<String ,Cell> getBestLocation();//hero tag , best location cell
    Cell getBestLocation(String hero);//hero tag , best location cell
}
