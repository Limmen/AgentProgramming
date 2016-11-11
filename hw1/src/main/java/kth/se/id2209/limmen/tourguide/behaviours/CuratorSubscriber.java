package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

import java.util.ArrayList;

/**
 * Subscribes to the yellow pages for notifications when new curators register.
 * Acts as the initiator role in a FIPA-Subscribe-like interaction protocol.
 *
 * @author Kim Hammar on 2016-11-11.
 */
public class CuratorSubscriber extends SubscriptionInitiator {
    public static String CURATORS = "Curators";

    /**
     * Class constructor initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param msg subscription message
     * @param store datastore to communicate with oter behaviours
     */
    public CuratorSubscriber(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
        getDataStore().put(CURATORS, new ArrayList<DFAgentDescription>());
    }

    /**
     * This method is called every time a inform message is received,
     * which is not out-of-sequence according to the protocol rules.
     * Updates the datastore with new curators.
     * @param inform
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            DFAgentDescription[] newCurators = DFService.decodeNotification(inform.getContent());
            ArrayList<DFAgentDescription> curators = (ArrayList<DFAgentDescription>) getDataStore().get(CURATORS);
            for(int i = 0; i < newCurators.length; i++){
                if(!curators.contains(newCurators[i]))
                    curators.add(newCurators[i]);
            }
            getDataStore().put(CURATORS, curators);
        }
        catch (FIPAException fe) {fe.printStackTrace(); }
    }

    /**
     * Method to create a subscriptionmessage that can be used to subscribe to the yellow pages
     * for artgallery-information services
     *
     * @param agent agent that should subscribe
     * @return subscriptionmessage
     */
    public static ACLMessage createSubscriptionMessage(Agent agent){
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("artgallery-information");
        dfAgentDescription.addServices(serviceDescription);
        return DFService.createSubscriptionMessage(agent, agent.getDefaultDF(), dfAgentDescription, null);
    }

}
