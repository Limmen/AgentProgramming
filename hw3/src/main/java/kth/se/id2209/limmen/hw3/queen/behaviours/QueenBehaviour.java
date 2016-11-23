package kth.se.id2209.limmen.hw3.queen.behaviours;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

/**
 * Main behaviour of the QueenAgent.
 *
 * @author Kim Hammar on 2016-11-22.
 */
public class QueenBehaviour extends ParallelBehaviour {

    public QueenBehaviour(int endCondition, Agent agent, int id, int numberOfAgents, int[][] board) {
        super(endCondition);
        QueenFinder queenFinder = new QueenFinder(agent, QueenFinder.createSubscriptionMessage(agent), getDataStore(), numberOfAgents, id, board, this);
        QueenServer queenServer = new QueenServer(board, numberOfAgents, id);
        queenServer.setDataStore(getDataStore());

        addSubBehaviour(queenFinder);
        addSubBehaviour(queenServer);
    }
}
