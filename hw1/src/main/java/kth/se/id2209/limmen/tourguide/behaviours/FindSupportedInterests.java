package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class FindSupportedInterests extends AchieveREInitiator {

    protected static String PROFILER_INTEREST = "Profiler Interest";

    public FindSupportedInterests(Agent a, ACLMessage msg, DataStore store) {
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
        request.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
        request.setOntology("Ontology(Class(FindSupportedInterests partial AchieveREInitiator))");
        Vector<ACLMessage> messages = new Vector();
        messages.add(request);
        return messages;
    }

    /**
     * This method is called when all the result notification messages have been collected.
     * By result notification message we intend here all the inform, failure received messages,
     * which are not not out-of-sequence according to the protocol rules.
     *
     * @param resultNotifications
     */
    protected void handleAllResultNotifications(Vector resultNotifications) {
        ArrayList<String> supportedInterests = new ArrayList();
        for (Object object : resultNotifications) {
            ACLMessage message = (ACLMessage) object;
            if (message.getPerformative() == ACLMessage.INFORM) {
                try {
                    ArrayList<String> genres = (ArrayList<String>) message.getContentObject();
                    for (String genre : genres) {
                        if (!supportedInterests.contains(genre))
                            supportedInterests.add(genre);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }
        ACLMessage proposal = ((ACLMessage) getDataStore().get(ProfilerMatcher.PROPOSE_KEY2));
        ACLMessage reply = proposal.createReply();
        reply.addReceiver(proposal.getSender());
        if (supportedInterests.contains(proposal.getContent())) {
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            getDataStore().put(PROFILER_INTEREST, proposal.getContent());
        } else {
            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
        }
        getDataStore().put(ProfilerMatcher.RESULT_KEY, reply);
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
     * This method is called every time a failure message is received,
     * which is not out-of-sequence according to the protocol rules.
     *
     * @param failure
     */
    protected void handleFailure(ACLMessage failure) {

    }

    protected void handleInconsistentFSM(String current, int event) {

    }

}
