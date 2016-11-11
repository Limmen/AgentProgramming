package kth.se.id2209.limmen.curator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.artgallery.Artifact;
import kth.se.id2209.limmen.curator.CuratorAgent;

import java.io.IOException;

/**
 * Behaviour for receiving and responding to request for artifact details about some artifact in
 * the gallery from profilers.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class ArtifactRequestServer extends CyclicBehaviour {

    /**
     * Action invoked whenever a matching message is received
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("Profiler-Request-Art-Information-Ontology");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            String title = msg.getContent();
            Artifact response = null;
            for (Artifact artifact : ((CuratorAgent) myAgent).getArtGallery()) {
                boolean match = false;
                if (artifact.getName().equalsIgnoreCase(title))
                    match = true;
                if (match)
                    response = artifact;
            }
            ACLMessage reply = msg.createReply();
            if (response != null) {
                reply.setPerformative(ACLMessage.AGREE);
                myAgent.send(reply);
                reply = msg.createReply();
                try {
                    reply.setContentObject(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reply.setPerformative(ACLMessage.INFORM);
                myAgent.send(reply);
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("Artifact not found, the artifact might have moved");
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
