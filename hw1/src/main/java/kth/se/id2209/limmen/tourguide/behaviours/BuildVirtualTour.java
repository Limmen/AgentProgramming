package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.artgallery.Artefact;
import kth.se.id2209.limmen.tourguide.TourGuideAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class BuildVirtualTour extends AchieveREInitiator {

    public BuildVirtualTour(Agent a, ACLMessage msg) {
        super(a, msg);
        System.out.println("BUILD VIRTUAL TOUR");
    }


    protected Vector prepareRequests(ACLMessage request) {
        System.out.println("buildvirtualtour PREPARE!!!!!");
        request = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = ((TourGuideAgent) myAgent).getGalleryCurators()[0].getName();
        request.addReceiver(receiver);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("TourGuide-Request-Art-Titles-Ontology");
        Artefact interests = new Artefact(new Artefact.ArtefactBuilder().genre("painting"));
        try {
            request.setContentObject(interests);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("SendTourGuideProposal completed");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        System.out.println("buildvirtualtour sent request to curator");
        return messages;
    }

    @Override
    protected void handleInform(ACLMessage agree) {
        System.out.println("BuildVirtualTour received inform message");
        try {
            ArrayList<String> virtualTour = (ArrayList<String>) agree.getContentObject();
            System.out.println("Reponse list size " + virtualTour.size());
            ACLMessage request = new ACLMessage(ACLMessage.INFORM);
            AID receiver = ((TourGuideAgent) myAgent).getRequester();
            request.addReceiver(receiver);
            try {
                request.setContentObject(virtualTour);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myAgent.send(request);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println("BuildVirtualTour received agree message");
    }

    protected void handleInconsistentFSM(java.lang.String current, int event){

    }

    @Override
    public int onEnd() {
        return super.onEnd();
    }

}
