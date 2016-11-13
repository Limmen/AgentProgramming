package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * Behaviour that waits for requests of which genres this agent can build tours of from profiler-agents.
 * This behaviour is linked to the FindSupportedInterests behaviour that will collect a
 * list of supported interests, and send it as a response to the requester.
 *
 * @author Kim Hammar on 2016-11-09.
 */

public class ProfilerMatcher extends AchieveREResponder {

    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param mt messagetemplate for filtering out the message that we want to receive
     * @param store datastore for communicating with other behaviours.
     */
    public ProfilerMatcher(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
    }

    /**
     * This method is called when the protocol initiation message
     * (matching the MessageTemplate specified in the constructor) is received.
     * If the message is understodd we return an "AGREE" message, indicating to the requester that we will perform
     * the necessary action (the RE), which in this case is to retrieve all supported genres.
     *
     * @param request received message
     * @return inital reply before Rational Effect (RE)
     * @throws NotUnderstoodException thrown when the received message is not understood
     * @throws RefuseException thrown when the received message is refused by the agent
     */
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        if(request.getPerformative() == ACLMessage.QUERY_REF && request.getContent().equals("interest")){
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.AGREE);
            return reply;
        }else {
            throw new NotUnderstoodException("Expected performative: QUERY_REF and context: 'interest' ");
        }
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
