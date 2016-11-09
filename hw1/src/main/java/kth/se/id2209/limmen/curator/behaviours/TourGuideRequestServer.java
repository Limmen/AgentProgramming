package kth.se.id2209.limmen.curator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class TourGuideRequestServer extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                .MatchOntology("TourGuide-Request-Art-Titles-Ontology");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            // Message received. Process it
        }
        else {
            block();
        }
    }
}
