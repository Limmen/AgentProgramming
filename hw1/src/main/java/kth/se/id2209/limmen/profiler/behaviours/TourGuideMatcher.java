package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
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
    public static String TOUR_GUIDE = "Tour Guide";
    private boolean foundMatchingTourGuide = false;

    public TourGuideMatcher(Agent a, ACLMessage initiation, DataStore store) {
        super(a, initiation, store);
    }


    protected Vector prepareInitiations(ACLMessage request) {
        request = new ACLMessage(ACLMessage.PROPOSE);
        DFAgentDescription[] tourGuides = (DFAgentDescription[]) getDataStore().get(ServicesSearcher.TOUR_GUIDES);
        if (tourGuides != null) {
            for (int i = 0; i < tourGuides.length; ++i) {
                request.addReceiver(tourGuides[i].getName());
            }
        } else {
            onEnd();
        }
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        request.setOntology("Ontology(Class(TourGuideMatcher partial ProposeInitiator))");
        request.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        request.setContent(((ProfilerAgent) myAgent).getUserProfile().getInterest());
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
    protected void handleAcceptProposal(ACLMessage accept_proposal) {
        foundMatchingTourGuide = true;
        getDataStore().put(TOUR_GUIDE, accept_proposal.getSender());
    }

    /**
     * This method is called every time an reject-proposal message is received,
     * which is not out-of-sequence according to the protocol rules.
     * @param reject_proposal
     */
    protected void handleRejectProposal(ACLMessage reject_proposal){

    }



    @Override
    public int onEnd() {
        if (foundMatchingTourGuide) {
            return 1;
        } else {
            reset();
            return 0;
        }
    }
}

