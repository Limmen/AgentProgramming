package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class ProfilerMatcher extends AchieveREResponder {
    private int exitValue = 0;

    public ProfilerMatcher(Agent a, MessageTemplate mt) {
        super(a, mt);
        System.out.println("ProfilerMatcher started");
    }

    /**
     * This method is called when the protocol initiation message (matching the MessageTemplate specified in the constructor) is received.
     *
     * @param request
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        System.out.println("ProfilerMatcher received proposal");
        ACLMessage agree = request.createReply();
        agree.setPerformative(ACLMessage.INFORM);
        return agree;
    }
/*
    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
        return null;
    }
*/
    @Override
    public int onEnd() {
        System.out.println("ProfilerMatcher exited");
        return exitValue;
    }
}
