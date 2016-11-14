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
 * Behaviour for building a virtual tour according to the requester's interest.
 * The behaviour will send a request for art-titles matching the given interest to all of the available curators
 * and then collect the responses and build a virtual tour from it.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class BuildVirtualTour extends AchieveREInitiator {

    private VirtualTourServer virtualTourServer;
    public BuildVirtualTour(Agent a, ACLMessage msg, DataStore store, VirtualTourServer virtualTourServer) {
        super(a, msg, store);
        this.virtualTourServer = virtualTourServer;
    }

    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.REQUEST);
        ArrayList<DFAgentDescription> curators = (ArrayList<DFAgentDescription>) getDataStore().get(CuratorSubscriber.CURATORS);
        for(DFAgentDescription curator : curators){
            request.addReceiver(curator.getName());
        }
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setOntology("Ontology(Class(BuildVirtualTour partial AchieveREInitiator))");
        request.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        request.setContent((String) getDataStore().get(VirtualTourServer.PROFILER_INTEREST));
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    /**
     * This method is called when all the result notification messages have been collected.
     * By result notification message we intend here all the inform, failure received messages,
     * which are not not out-of-sequence according to the protocol rules.
     *
     * @param resultNotifications vector of notifications containing list of art artifact-names that match the given interest
     */
    protected void handleAllResultNotifications(Vector resultNotifications) {
        ArrayList<TourItem> virtualTour = new ArrayList();
        for (Object object : resultNotifications) {
            ACLMessage notification = (ACLMessage) object;
            if (notification.getPerformative() == ACLMessage.INFORM) {
                try {
                    ArrayList<String> titles = (ArrayList<String>) notification.getContentObject();
                    for (String title : titles) {
                        TourItem tourItem = new TourItem(title, notification.getSender());
                        if (!virtualTour.contains(tourItem))
                            virtualTour.add(tourItem);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        ACLMessage reply = ((ACLMessage) getDataStore().get(virtualTourServer.REQUEST_KEY)).createReply();
        if (virtualTour.size() > 0) {
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(virtualTour);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent("Failed to retrieve virtual tour from art-curators matching the given interest. Please try again later");
        }
        getDataStore().put(virtualTourServer.RESULT_NOTIFICATION_KEY, reply);
    }

}
