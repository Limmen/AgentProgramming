package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author Kim Hammar on 2016-11-09.
 */

public class VirtualTourServer extends OneShotBehaviour {
    private int exitValue = 0;

    @Override
    public void action() {
       // MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
//                .MatchOntology("Profiler-request-virtual-tour");
        MessageTemplate mt = MessageTemplate.MatchAll();
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            exitValue = 1;
            getDataStore().put("recv-msg", msg.getSender());
            //((TourGuideAgent) myAgent).setRequester(msg.getSender());
        }
        else {
            block();
        }
    }

    @Override
    public int onEnd() {
        return exitValue;
    }
}