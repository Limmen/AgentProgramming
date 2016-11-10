package kth.se.id2209.limmen.curator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import kth.se.id2209.limmen.artgallery.Artefact;
import kth.se.id2209.limmen.curator.CuratorAgent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class ProfilerRequestServer extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("Profiler-Request-Art-Information-Ontology");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            // Message received. Process it
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.AGREE);
            myAgent.send(reply);
            try {
                ArrayList<String> titles = (ArrayList<String>) msg.getContentObject();
                System.out.println("PROFILERREQUESTERSERVER recieved titles size: " + titles.size());
                ArrayList<Artefact> response = new ArrayList();
                for (String title : titles) {
                    for (Artefact artefact : ((CuratorAgent) myAgent).getArtGallery()) {
                        boolean match = false;
                        if (artefact.getName().equalsIgnoreCase(title))
                            match = true;
                        if (match && !response.contains(artefact))
                            response.add(artefact);
                    }
                }
                reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContentObject(response);
                myAgent.send(reply);
            } catch (IOException | UnreadableException e) {
                e.printStackTrace();
            }
        }
            else{
                block();
            }
        }
    }
