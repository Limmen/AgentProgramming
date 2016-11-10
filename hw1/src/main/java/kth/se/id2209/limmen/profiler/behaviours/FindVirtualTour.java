package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.tourguide.TourItem;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-10.
 */
public class FindVirtualTour extends AchieveREInitiator {
    public static String VIRTUAL_TOUR = "Virtual Tour";

    public FindVirtualTour(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = (AID) getDataStore().get(TourGuideMatcher.TOUR_GUIDE);
        request.addReceiver(receiver);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("Ontology(Class(FindVirtualTour partial AchieveREInitiator))");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    @Override
    protected void handleInform(ACLMessage agree) {
        try {
            ArrayList<TourItem> virtualTour = (ArrayList<TourItem>) agree.getContentObject();
            getDataStore().put(VIRTUAL_TOUR, virtualTour);
            System.out.println("Virtual Tour completed by agent: " + agree.getSender().getName());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called every time a refuse message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param refuse
     */
    protected void handleRefuse(ACLMessage refuse) {

    }


    /**
     * This method is called every time a failure message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param failure
     */
    protected void handleFailure(ACLMessage failure) {

    }
}
