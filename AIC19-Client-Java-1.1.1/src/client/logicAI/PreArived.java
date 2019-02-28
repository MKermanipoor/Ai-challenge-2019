package client.logicAI;

import client.model.World;

public interface PreArived {
    void moveTurn(World world, PreProcess PreProcess);
    void actionTurn(World world, PreProcess PreProcess);
}
