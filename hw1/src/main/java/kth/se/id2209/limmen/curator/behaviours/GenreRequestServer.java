package kth.se.id2209.limmen.curator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.artgallery.Artifact;
import kth.se.id2209.limmen.curator.CuratorAgent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Behaviour for receiving and responding to request for all available genres in the art gallery from tourguides.
 * .
 * @author Kim Hammar on 2016-11-09.
 */
public class GenreRequestServer extends CyclicBehaviour {

    /**
     * Action invoked whenever a matching message is received
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("Ontology(Class(FindSupportedInterests partial AchieveREInitiator))");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            ArrayList<Artifact> artifacts = ((CuratorAgent) myAgent).getArtGallery();
            ArrayList<String> genres = new ArrayList();
            for (Artifact artifact : artifacts) {
                if (!genres.contains(artifact.getGenre()))
                    genres.add(artifact.getGenre());
            }
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(genres);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myAgent.send(reply);

        } else {
            block();
        }

    }
}

