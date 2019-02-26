package client.logicAI;

import client.model.World;

public interface PreArived {
    void moveTurn(World world, preProcess preProcess);
    void actionTurn(World world);
}
