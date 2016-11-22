package kth.se.id2209.limmen.queen.behaviours;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import kth.se.id2209.limmen.queen.gui.BoardFrame;

/**
 * @author Kim Hammar on 2016-11-22.
 */
public class QueenBehaviour extends ParallelBehaviour {

    public QueenBehaviour(Agent agent, BoardFrame boardFrame, int id, int numberOfAgents, int[][] board) {
        QueenFinder queenFinder = new QueenFinder(agent, QueenFinder.createSubscriptionMessage(agent), getDataStore(), numberOfAgents, id, board);
        QueenServer queenServer = new QueenServer(board, boardFrame, numberOfAgents, id);
        queenServer.setDataStore(getDataStore());

        addSubBehaviour(queenFinder);
        addSubBehaviour(queenServer);
    }
}
