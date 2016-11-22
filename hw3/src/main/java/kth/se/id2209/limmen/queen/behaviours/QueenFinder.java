package kth.se.id2209.limmen.queen.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kim Hammar on 2016-11-22.
 */
public class QueenFinder extends SubscriptionInitiator {
    public static String QUEENS = "Queens";
    public static String SORTED_QUEENS = "Sorted queens";
    private boolean firstRound = true;
    private int numberOfAgents;
    private int id;
    private int[][] board;

    public QueenFinder(Agent a, ACLMessage msg, DataStore store, int numberOfAgents, int id, int[][] board) {
        super(a, msg, store);
        getDataStore().put(QUEENS, new ArrayList<DFAgentDescription>());
        this.numberOfAgents = numberOfAgents;
        this.id = id;
        this.board = board;
    }

    /**
     * This method is called every time a inform message is received,
     * which is not out-of-sequence according to the protocol rules.
     * Updates the datastore with new curators.
     *
     * @param inform message received from the subscription that indicates that a new service matching service was found.
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            DFAgentDescription[] newQueens = DFService.decodeNotification(inform.getContent());
            ArrayList<DFAgentDescription> queens = (ArrayList<DFAgentDescription>) getDataStore().get(QUEENS);
            for (int i = 0; i < newQueens.length; i++) {
                if (!queens.contains(newQueens[i]))
                    queens.add(newQueens[i]);
            }
            getDataStore().put(QUEENS, queens);
            getDataStore().put(SORTED_QUEENS, sortQueens(queens));
            if (id == 0 && queens.size() == numberOfAgents && firstRound) {
                ACLMessage notify = new ACLMessage(ACLMessage.INFORM);
                notify.setOntology("queens");
                notify.setContentObject(board);
                notify.addReceiver(myAgent.getAID());
                myAgent.send(notify);
                firstRound = false;
            }
        } catch (FIPAException | IOException fe) {
            fe.printStackTrace();
        }
    }

    /**
     * Method to create a subscriptionmessage that can be used to subscribe to the yellow pages
     * for artgallery-information services
     *
     * @param agent agent that should subscribe
     * @return subscriptionmessage
     */
    public static ACLMessage createSubscriptionMessage(Agent agent) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("queen");
        dfAgentDescription.addServices(serviceDescription);
        return DFService.createSubscriptionMessage(agent, agent.getDefaultDF(), dfAgentDescription, null);
    }

    private DFAgentDescription[] sortQueens(ArrayList<DFAgentDescription> queens) {
        DFAgentDescription[] sortedQueens = new DFAgentDescription[numberOfAgents];
        for (DFAgentDescription dfAgentDescription : queens) {
            ServiceDescription serviceDescription = (ServiceDescription) dfAgentDescription.getAllServices().next();
            int index = Integer.parseInt(serviceDescription.getName());
            sortedQueens[index] = dfAgentDescription;
        }
        return sortedQueens;
    }

}
