package kth.se.id2209.limmen.curator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.artgallery.Artifact;
import kth.se.id2209.limmen.curator.CuratorAgent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Behaviour for receiving and responding to request for artifacts in the artgallery matching a given genre from tourguides.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class TourRequestServer extends CyclicBehaviour {

    /**
     * Action invoked everytime a matching message is received
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("Ontology(Class(BuildVirtualTour partial AchieveREInitiator))");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.AGREE);
            myAgent.send(reply);
            try {
                String interest = msg.getContent();
                ArrayList<String> response = new ArrayList();
                for (Artifact artifact : ((CuratorAgent) myAgent).getArtGallery()) {
                    boolean match = false;
                    if (artifact.getGenre().equalsIgnoreCase(interest))
                        match = true;
                    if (match && !response.contains(artifact.getName()))
                        response.add(artifact.getName());
                }
                reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContentObject(response);
                myAgent.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            block();
        }
    }
}
