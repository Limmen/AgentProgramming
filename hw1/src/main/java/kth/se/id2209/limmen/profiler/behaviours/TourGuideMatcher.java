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
 * Behaviour that sends proposals to all tourguides identified at the yellow pages and checks if they offer
 * tours with matching interests.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class TourGuideMatcher extends ProposeInitiator {
    public static String TOUR_GUIDE = "Tour Guide";
    private boolean foundMatchingTourGuide = false;

    /**
     * Class constructor initializing the behaviour
     * @param a agent running the behaviour
     * @param initiation message to send to the tourguides
     * @param store datastore to communicate with other behaviours
     */
    public TourGuideMatcher(Agent a, ACLMessage initiation, DataStore store) {
        super(a, initiation, store);
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     *
     * @param proposal the message passed in the constructor
     * @return vector of messages to send
     */
    protected Vector prepareInitiations(ACLMessage proposal) {
        proposal = new ACLMessage(ACLMessage.PROPOSE);
        DFAgentDescription[] tourGuides = (DFAgentDescription[]) getDataStore().get(ServicesSearcher.TOUR_GUIDES);
        if (tourGuides != null) {
            for (int i = 0; i < tourGuides.length; ++i) {
                proposal.addReceiver(tourGuides[i].getName());
            }
        } else {
            onEnd();
        }
        proposal.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        proposal.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        proposal.setOntology("Ontology(Class(TourGuideMatcher partial ProposeInitiator))");
        proposal.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        proposal.setContent(((ProfilerAgent) myAgent).getUserProfile().getInterest());
        Vector<ACLMessage> messages = new Vector();
        messages.add(proposal);
        return messages;
    }

    /**
     * This method is called every time an accept-proposal message is received,
     * which is not out-of-sequence according to the protocol rules.
     * This method will match with the first accept-message received and will select that tourguide as the tourguide to
     * use.
     *
     * @param accept_proposal the accept-message received
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


    /**
     * Called just before termination of this behaviour. The returnvalue is used by the FSMbehaviour
     *
     * @return
     */
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

