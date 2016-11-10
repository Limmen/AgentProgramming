package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;

/**
 * @author Kim Hammar on 2016-11-09.
 */

public class ProfilerMatcher extends ProposeResponder {

    protected static String RESULT_KEY;
    protected static String PROPOSE_KEY2;

    public ProfilerMatcher(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
        RESULT_KEY = RESPONSE_KEY;
        PROPOSE_KEY2 = PROPOSE_KEY;
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
    /*
    protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException, RefuseException {
        //check if matching interest
        getDataStore().put(INTEREST, propose.getContent());
        ACLMessage reply = propose.createReply();
        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        return reply;
    }*/

    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

}
