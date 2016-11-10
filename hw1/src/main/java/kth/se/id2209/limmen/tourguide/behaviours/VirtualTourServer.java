package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * @author Kim Hammar on 2016-11-09.
 */

public class VirtualTourServer extends AchieveREResponder {

    protected static String RESULT_KEY;

    public VirtualTourServer(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
        RESULT_KEY = RESULT_NOTIFICATION_KEY;
    }

    /**
     * This method is called when the protocol initiation message
     * (matching the MessageTemplate specified in the constructor) is received.
     *
     * @param request
     * @return
     * @throws NotUnderstoodException
     * @throws RefuseException
     */
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        //Check if curator is available?
        ACLMessage reply = request.createReply();
        reply.setPerformative(ACLMessage.AGREE);
        return reply;
    }

    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

}