package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class VirtualTourServer extends CyclicBehaviour {
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("TourGuide VirtualTourServer behaviour received message: "
            + "content: " + msg.getContent() + " language: " +msg.getLanguage()
                    + " ontolgy: " + msg.getOntology() + " protocol: " + msg.getProtocol()
             + " encoding: " + msg.getEncoding());

        }
        else {
            block();
        }
    }
}
