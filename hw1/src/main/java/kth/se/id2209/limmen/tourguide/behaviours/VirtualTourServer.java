package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * Behaviour for recieving requests for virtual tours from profilers.
 * This behaviour receives requests from profilers who have already checked that the art-interests matches.
 * This behaviour is linked to the BuildVirtualTour behaviour who will collect the art-inforḿation and build the actual
 * tour that is then returned to the user as a response
 *
 * @author Kim Hammar on 2016-11-09.
 */

public class VirtualTourServer extends AchieveREResponder {

    protected static String RESULT_KEY;
    protected static String REQUESTER;

    /**
     * Class constructor
     * @param a agent running the behaviour
     * @param mt messagetempate for filtering out the message that we want to recieve
     * @param store datastore for communicating with other behaviours
     */
    public VirtualTourServer(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
        RESULT_KEY = RESULT_NOTIFICATION_KEY;
        REQUESTER = REQUEST_KEY;
    }

    /**
     * This method is called when the protocol initiation message
     * (matching the MessageTemplate specified in the constructor) is received.
     *
     * @param request message received
     * @return the initial reply, before Rational Effect (RE)
     * @throws NotUnderstoodException thrown if the request is not understood
     * @throws RefuseException thrown if the agent chooses to refuse the request before even doing the RE
     */
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.AGREE);
        return reply;
    }

    /**
     * Called just before termination of this behaviour.
     * This behaviour is continous.
     * @return
     */
    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

}