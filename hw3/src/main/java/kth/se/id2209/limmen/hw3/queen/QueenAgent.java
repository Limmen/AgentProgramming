package kth.se.id2209.limmen.hw3.queen;


import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import kth.se.id2209.limmen.hw3.queen.behaviours.QueenBehaviour;

/**
 * Agent that acts as a Queen in a M.A.S that tries to solve the Queens Puzzle
 *
 * @author Kim Hammar on 2016-11-22.
 */
public class QueenAgent extends Agent {

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        /**
         * Get command-line arguments
         */
        int id = -1;
        int numberOfAgents = -1;
        Object[] args = getArguments();
        if (args != null && args.length > 1) {
            id = Integer.parseInt((String) args[0]);
            numberOfAgents = Integer.parseInt((String) args[1]);
        }
        registerAtYellowPages(id);
        int[][] board = new int[numberOfAgents][numberOfAgents];
        QueenBehaviour queenBehaviour = new QueenBehaviour(ParallelBehaviour.WHEN_ANY, this, id ,numberOfAgents, board);
        addBehaviour(queenBehaviour);
    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination.
     * DeRegisters from the DF registry.
     */
    @Override
    public void takeDown() {
        System.out.println("QueenAgent " + getAID().getName() + " terminating.");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register the service as an queen at the "yellow pages" aka the Directory Facilitator (DF)
     * that runs on the platform.
     */
    private void registerAtYellowPages(int id) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("queen");
        serviceDescription.setName(Integer.toString(id));
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
