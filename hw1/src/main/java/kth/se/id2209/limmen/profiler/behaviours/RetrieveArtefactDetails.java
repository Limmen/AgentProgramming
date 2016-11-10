package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.artgallery.Artefact;
import kth.se.id2209.limmen.profiler.ProfilerAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class RetrieveArtefactDetails extends AchieveREInitiator {

    public RetrieveArtefactDetails(Agent a, ACLMessage msg) {
        super(a, msg);
    }


    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = ((ProfilerAgent) myAgent).getGalleryCurators()[0].getName();
        request.addReceiver(receiver);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("Profiler-Request-Art-Information-Ontology");
        ArrayList<String> tourTitles = ((ProfilerAgent) myAgent).getTourTitles();
        try {
            request.setContentObject(tourTitles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    @Override
    protected void handleInform(ACLMessage agree) {
        try {
            ArrayList<Artefact> virtualTour = (ArrayList<Artefact>) agree.getContentObject();
            System.out.println("PROFILER ARTEFACT Reponse list size " + virtualTour.size());
            for(Artefact artefact : virtualTour){
                System.out.println(artefact.toString());
            }
            ((ProfilerAgent) myAgent).setDetailtedTour(virtualTour);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println("BuildVirtualTour received agree message");
    }

    protected void handleInconsistentFSM(String current, int event){

    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }

}
