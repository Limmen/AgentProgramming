package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.profiler.ProfilerAgent;
import kth.se.id2209.limmen.tourguide.TourItem;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Behaviour that is invoked after a matching tourguide is found. The behaviour will contact the tourguide and request
 * a virtual tour that matches the given interests and then (hopefully) receive a virtual tour as response.
 *
 * @author Kim Hammar on 2016-11-10.
 */
public class FindVirtualTour extends AchieveREInitiator {
    public static String VIRTUAL_TOUR = "Virtual Tour";
    private boolean success = true;

    /**
     * Class constructor that initializes the behaviour
     *
     * @param a     agent running the behaviour
     * @param msg   message to send to the tourguide
     * @param store datastore to communicate with other behaviours
     */
    public FindVirtualTour(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     *
     * @param request request passed in the constructor
     * @return vector of refined requests to send
     */
    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = (AID) getDataStore().get(TourGuideMatcher.TOUR_GUIDE);
        request.addReceiver(receiver);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("Ontology(Class(FindVirtualTour partial AchieveREInitiator))");
        request.setContent(((ProfilerAgent) myAgent).getUserProfile().getInterest());
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    /**
     * This method is called every time a inform message is received,
     * which is not out-of-sequence according to the protocol rules.
     * The inform method should contain a virtual tour.
     *
     * @param inform message received
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            ArrayList<TourItem> virtualTour = (ArrayList<TourItem>) inform.getContentObject();
            getDataStore().put(VIRTUAL_TOUR, virtualTour);
            System.out.println("Virtual Tour completed by agent: " + inform.getSender().getName());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called every time a failure message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param failure
     */
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Failed to find a virtual tour, reason: " + failure.getContent());
        success = false;
    }

    /**
     * Called just before termination of this behaviour.
     * return value decides the next state.
     *
     * @return next-state indicator
     */
    @Override
    public int onEnd() {
        if (success) {
            return 1;
        } else {
            return 0;
        }
    }
}
