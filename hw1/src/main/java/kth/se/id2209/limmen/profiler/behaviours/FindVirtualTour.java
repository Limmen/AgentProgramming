package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-10.
 */
public class FindVirtualTour extends AchieveREInitiator {
    public static String TOUR_TITLES = "Tour titles";

    public FindVirtualTour(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    protected Vector prepareRequests(ACLMessage request) {
        System.out.println("FindVirtualTour preparing");
        request = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = (AID) getDataStore().get(TourGuideMatcher.TOUR_GUIDE);
        request.addReceiver(receiver);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("Profiler-Request-Virtual-Tour-Ontology");
        request.setContent("virtual-tour");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    @Override
    protected void handleInform(ACLMessage agree) {
        System.out.println("FindVirtualTour received inform message");
        try {
            ArrayList<String> tourTitles = (ArrayList<String>) agree.getContentObject();
            getDataStore().put(TOUR_TITLES, tourTitles);
            System.out.println("FindVirtualTourReponse list size " + tourTitles.size());

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
     * This method is called every time a not-understood message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param notUnderstood
     */
    protected void handleNotUnderstood(ACLMessage notUnderstood) {

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
