package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.artgallery.Artefact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class BuildVirtualTour extends AchieveREInitiator {

    public BuildVirtualTour(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    protected Vector prepareRequests(ACLMessage request) {
        System.out.println("BuildVirtualTour preparing");
        request = new ACLMessage(ACLMessage.REQUEST);
        AID receiver = ((DFAgentDescription[]) getDataStore().get(CuratorSearcher.CURATORS))[0].getName();
        request.addReceiver(receiver);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("TourGuide-Request-Art-Titles-Ontology");
        Artefact interests = new Artefact(new Artefact.ArtefactBuilder().genre("painting"));
        try {
            request.setContentObject(interests);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    @Override
    protected void handleInform(ACLMessage agree) {
        System.out.println("BuildVirtualTour received inform message");
        try {
            ArrayList<String> virtualTour = (ArrayList<String>) agree.getContentObject();
            System.out.println("Reponse list size " + virtualTour.size());
            ACLMessage request = new ACLMessage(ACLMessage.INFORM);
            //AID receiver = (AID) getDataStore().get(VirtualTourServer.REQUESTER);
            AID receiver = ((ACLMessage) getDataStore().get(REQUEST_KEY)).getSender();
            request.addReceiver(receiver);
            try {
                request.setContentObject(virtualTour);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getDataStore().put(VirtualTourServer.RESULT_KEY, request);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called every time a refuse message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param refuse
     */
    protected void handleRefuse(ACLMessage refuse) {

    }

    /**
     * This method is called every time a not-understood message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param notUnderstood
     */
    protected void handleNotUnderstood(ACLMessage notUnderstood) {

    }

    /**
     * This method is called every time a failure message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param failure
     */
    protected void handleFailure(ACLMessage failure) {

    }

    protected void handleInconsistentFSM(java.lang.String current, int event) {

    }

}
