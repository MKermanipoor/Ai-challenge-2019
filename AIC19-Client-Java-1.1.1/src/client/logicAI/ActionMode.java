package client.logicAI;

import client.model.World;

/**
 * Created by saeidbahmani on 2/28/19.
 */
public interface ActionMode {
     void actionTurn(World world);
     void moveTurn(World world);
}
