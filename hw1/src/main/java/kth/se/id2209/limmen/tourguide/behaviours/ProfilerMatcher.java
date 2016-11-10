package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;

/**
 * @author Kim Hammar on 2016-11-09.
 */

public class ProfilerMatcher extends ProposeResponder {

    public ProfilerMatcher(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    /**
     * This method is called when the initiator's message is received that matches the message
     * template passed in the constructor.
     *
     * @param propose
     * @return
     * @throws NotUnderstoodException
     * @throws RefuseException
     */
    protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException, RefuseException {
        //check if matching interest
        ACLMessage reply = propose.createReply();
        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        return reply;
    }

    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

}
