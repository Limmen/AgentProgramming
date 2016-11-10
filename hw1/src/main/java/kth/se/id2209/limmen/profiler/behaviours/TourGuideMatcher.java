package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;
import kth.se.id2209.limmen.profiler.ProfilerAgent;

import java.util.Date;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class TourGuideMatcher extends ProposeInitiator {
    private boolean foundMatchingTourGuide = false;
    private DFAgentDescription[] tourGuides;

    public TourGuideMatcher(Agent a, ACLMessage msg, DFAgentDescription[] tourGuides) {
        super(a, msg);
        this.tourGuides = tourGuides;
        System.out.println("TourGUideMatcher starting, tourguides: " + tourGuides.length);
    }


    protected Vector prepareInitiations(ACLMessage request) {
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
        request.setOntology("Profiler-Search-Matching-Guide");
        request.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        request.setContent("virtual-tour");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }


    /**
     * This method is called every time an accept-proposal message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param accept_proposal
     */
    protected void handleAcceptProposal(ACLMessage accept_proposal){
        foundMatchingTourGuide = true;
        ((ProfilerAgent) myAgent).setTourGuide(accept_proposal.getSender());
        onEnd();
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
            //forceTransitionTo(ProfilerAgent.RECEIVE_VIRTUAL_TOUR);
            return 1;
        }
        else{
            //System.out.println("onEnd returning 0 ");
            reset();
            return 0;
        }
    }


}

