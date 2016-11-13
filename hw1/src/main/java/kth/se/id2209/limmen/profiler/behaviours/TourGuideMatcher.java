package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.profiler.TourGuide;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Behaviour that sends requests to all tourguides identified at the yellow pages and checks what type of tours they offer.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class TourGuideMatcher extends AchieveREInitiator {
    public static String TOUR_GUIDES = "Tour Guides with genres";
    public static String TOUR_GUIDE = "Tour Guide";


    /**
     * Class constructor initializing the behaviour
     *
     * @param a          agent running the behaviour
     * @param initiation message to send to the tourguides
     * @param store      datastore to communicate with other behaviours
     */
    public TourGuideMatcher(Agent a, ACLMessage initiation, DataStore store) {
        super(a, initiation, store);
        getDataStore().put(TOUR_GUIDES, new ArrayList<TourGuide>());
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     *
     * @param request the message passed in the constructor
     * @return vector of messages to send
     */
    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.QUERY_REF);
        DFAgentDescription[] tourGuides = (DFAgentDescription[]) getDataStore().get(ServicesSearcher.TOUR_GUIDES);
        if (tourGuides != null) {
            for (int i = 0; i < tourGuides.length; ++i) {
                request.addReceiver(tourGuides[i].getName());
            }
        }
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
        request.setOntology("Ontology(Class(TourGuideMatcher partial AchieveREInitiator))");
        request.setContent("interest");
        request.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    /**
     * This method is called when all the result notification messages have been collected.
     * By result notification message we intend here all the inform, failure received messages,
     * which are not not out-of-sequence according to the protocol rules.
     * This method will collect all inform messages from tourguides, containing lists of supported interests
     * and update the datastore accordingly.
     *
     * @param resultNotifications vector of notifications (aka INFORM messages)
     */
    protected void handleAllResultNotifications(Vector resultNotifications) {
        ArrayList<TourGuide> foundTourGuides = new ArrayList();
        for (Object object : resultNotifications) {
            ACLMessage notification = (ACLMessage) object;
            if (notification.getPerformative() == ACLMessage.INFORM) {
                try {
                    ArrayList<String> supportedInterests = (ArrayList<String>) notification.getContentObject();
                    TourGuide tourGuide = new TourGuide(notification.getSender(), supportedInterests);
                    foundTourGuides.add(tourGuide);
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        getDataStore().put(TOUR_GUIDES, foundTourGuides);
    }

}

