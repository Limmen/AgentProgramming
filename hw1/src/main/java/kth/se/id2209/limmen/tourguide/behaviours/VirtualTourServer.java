package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.tourguide.TourGuideAgent;

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
            System.out.println("VirtualTourServer received msg");
            exitValue = 1;
            ((TourGuideAgent) myAgent).setRequester(msg.getSender());
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