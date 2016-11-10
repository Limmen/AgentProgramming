package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.profiler.ProfilerAgent;

import java.util.Date;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class TourGuideMatcher extends AchieveREInitiator {
    private boolean foundMatchingTourGuide = false;
    private DFAgentDescription[] tourGuides;

    public TourGuideMatcher(Agent a, ACLMessage msg, DFAgentDescription[] tourGuides) {
        super(a, msg);
        this.tourGuides = tourGuides;
        System.out.println("TourGUideMatcher starting, tourguides: " + tourGuides.length);
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     *
     * @param request
     * @return
     */
    protected Vector prepareRequests(ACLMessage request) {
        //System.out.println("TOurGuideMatcherPrepareREquests");
        request = new ACLMessage(ACLMessage.PROPOSE);
        //System.out.println("preparing requests");
        DFAgentDescription[] tourGuides = ((ProfilerAgent) myAgent).getTourGuides();
        if (tourGuides.length == 0) {
            //System.out.println("length 0 jao");
            //return new Vector();
        }
        for (int i = 0; i < tourGuides.length; ++i) {
            request.addReceiver(tourGuides[i].getName());
        }
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        // We want to receive a reply in 10 secs
        request.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        request.setContent("virtual-tour");
        //System.out.println("SendTourGuideProposal completed");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }


    /**
     * This method is called every time an agree message is received,
     * which is not out-of-sequence according to the protocol rules.
     */
    @Override
    protected void handleInform(ACLMessage agree) {
        //System.out.println("HandleAgree");
        foundMatchingTourGuide = true;
        ((ProfilerAgent) myAgent).setTourGuide(agree.getSender());
        onEnd();
        //System.out.println("TourGuideMatcher found a match");
    }


    protected void handleAllResultNotifications(Vector msgs){
        //System.out.println("HANDLE ALL RESULT NOTICIATIONS");
    }

    /*
    //This method is called when all the responses have been collected or when the timeout is expired.
    @Override
    protected void handleAllResponses(Vector responses) {
        System.out.println("TourGuideMatcher received " + responses.size() + " responses");
        if (!foundMatchingTourGuide)
            System.out.println("No tourguide matched our preferences");
        else
            System.out.println("Found match for preferences lol");
    }
*/
    @Override
    public int onEnd() {
        //System.out.println("TourGUideMatcher exiting");
        if (foundMatchingTourGuide){
            System.out.println("onEnd returning 1 ");
            forceTransitionTo(ProfilerAgent.RECEIVE_VIRTUAL_TOUR);
            return 1;
        }
        else{
            //System.out.println("onEnd returning 0 ");
            reset();
            return 0;
        }
    }


}

