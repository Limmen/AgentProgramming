package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.tourguide.TourItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class BuildVirtualTour extends AchieveREInitiator {

    public BuildVirtualTour(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.REQUEST);
        DFAgentDescription[] curators = (DFAgentDescription[]) getDataStore().get(CuratorSearcher.CURATORS);
        for (int i = 0; i < curators.length; i++) {
            request.addReceiver(curators[i].getName());
        }
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("Ontology(Class(BuildVirtualTour partial AchieveREInitiator))");
        request.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        request.setContent((String) getDataStore().get(FindSupportedInterests.PROFILER_INTEREST));
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }
/*
    @Override
    protected void handleInform(ACLMessage agree) {
        System.out.println("BuildVirtualTour received inform message");
        try {
            ArrayList<String> virtualTour = (ArrayList<String>) agree.getContentObject();
            System.out.println("Reponse list size " + virtualTour.size());
            ACLMessage request = new ACLMessage(ACLMessage.INFORM);
            AID receiver = ((ACLMessage) getDataStore().get(VirtualTourServer.REQUESTER)).getSender();
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
*/
    /**
     * This method is called when all the result notification messages have been collected.
     * By result notification message we intend here all the inform, failure received messages,
     * which are not not out-of-sequence according to the protocol rules.
     *
     * @param resultNotifications
     */
    protected void handleAllResultNotifications(Vector resultNotifications) {
        System.out.println("Handle all result notifications / BuildVirtualTour");
        System.out.println("result size: " + resultNotifications.size());
        ArrayList<TourItem> virtualTour = new ArrayList();
        for (Object object : resultNotifications) {
            ACLMessage message = (ACLMessage) object;
            if (message.getPerformative() == ACLMessage.INFORM) {
                try {
                    ArrayList<String> titles = (ArrayList<String>) message.getContentObject();
                    for (String title : titles) {
                        TourItem tourItem = new TourItem(title, message.getSender());
                        if(!virtualTour.contains(tourItem))
                            virtualTour.add(tourItem);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        ACLMessage reply = ((ACLMessage) getDataStore().get(VirtualTourServer.REQUESTER)).createReply();
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(virtualTour);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getDataStore().put(VirtualTourServer.RESULT_KEY, reply);
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
