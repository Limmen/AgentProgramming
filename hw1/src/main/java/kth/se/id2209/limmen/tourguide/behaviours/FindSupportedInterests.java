package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;


/**
 * Behaviour that is invoked after an agent have sent a probe checking if there is any available tours with matching
 * his interests. This behaviour will send requests to all art-curators that are registered at the yellow pages and
 * collect their available art-genres. If the interest of the agent matches one or more of the genres the agent accepts
 * the proposal, otherwise it is rejected.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class FindSupportedInterests extends AchieveREInitiator {
    private ProfilerMatcher profilerMatcher;

    /**
     * Class constructor initializing the behaviour.
     *
     * @param a     agent that runs the behaviour
     * @param msg   message to send to the curators
     * @param store datastore to communicate with other behaviours
     */
    public FindSupportedInterests(Agent a, ACLMessage msg, DataStore store, ProfilerMatcher profilerMatcher) {
        super(a, msg, store);
        this.profilerMatcher = profilerMatcher;
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     *
     * @param request the request passed in the constructor
     * @return the updated request as how it should look when sending to the curators
     */
    protected Vector prepareRequests(ACLMessage request) {
        request = new ACLMessage(ACLMessage.REQUEST);
        ArrayList<DFAgentDescription> curators = (ArrayList<DFAgentDescription>) getDataStore().get(CuratorSubscriber.CURATORS);
        if (curators.size() == 0) {
            ACLMessage query = ((ACLMessage) getDataStore().get(profilerMatcher.REQUEST_KEY));
            ACLMessage reply = query.createReply();
            reply.addReceiver(query.getSender());
            reply.setPerformative(ACLMessage.INFORM);
            try {
                ArrayList<String> supportedInterests = new ArrayList();
                supportedInterests.add("-");
                reply.setContentObject(supportedInterests);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getDataStore().put(profilerMatcher.RESULT_NOTIFICATION_KEY, reply);
            return null;
        }
        for (DFAgentDescription curator : curators) {
            request.addReceiver(curator.getName());
        }
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setReplyByDate(new Date(System.currentTimeMillis() + 1000));
        request.setOntology("Ontology(Class(FindSupportedInterests partial AchieveREInitiator))");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    /**
     * This method is called when all the result notification messages have been collected.
     * By result notification message we intend here all the inform, failure received messages,
     * which are not not out-of-sequence according to the protocol rules.
     * This method will collect all genres of contacted curators and respond to the Profiler that this is the list
     * of genres that this tourguide can create tours for.
     *
     * @param resultNotifications vector of notifications (aka INFORM messages) from curators containing their genres.
     */
    protected void handleAllResultNotifications(Vector resultNotifications) {
        ArrayList<String> supportedInterests = new ArrayList();
        for (Object object : resultNotifications) {
            ACLMessage notification = (ACLMessage) object;
            if (notification.getPerformative() == ACLMessage.INFORM) {
                try {
                    ArrayList<String> genres = (ArrayList<String>) notification.getContentObject();
                    for (String genre : genres) {
                        if (!supportedInterests.contains(genre))
                            supportedInterests.add(genre);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        ACLMessage query = ((ACLMessage) getDataStore().get(profilerMatcher.REQUEST_KEY));
        ACLMessage reply = query.createReply();
        reply.addReceiver(query.getSender());
        reply.setPerformative(ACLMessage.INFORM);
        try {
            reply.setContentObject(supportedInterests);
            getDataStore().put(profilerMatcher.RESULT_NOTIFICATION_KEY, reply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
