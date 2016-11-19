package kth.se.id2209.limmen.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;

import java.util.ArrayList;

/**
 * @author Kim Hammar on 2016-11-19.
 */
public class CollectBids extends WakerBehaviour {
    private boolean receivedBid = false;

    public CollectBids(Agent a, long timeout) {
        super(a, timeout);
    }

    @Override
    public void onWake() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        boolean messageQueueNotEmpty = true;
        ArrayList<ACLMessage> receivedBids = new ArrayList<>();
        while (messageQueueNotEmpty) {
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                receivedBids.add(msg);
            }else {
                messageQueueNotEmpty = false;
            }
        }
        if (receivedBids.size() == 0) {
            receivedBid = false;
        } else {
            getDataStore().put(ArtistManagerAgent.WINNERS, receivedBids);
            receivedBid = true;
        }
    }

    public int onEnd() {
        if (receivedBid)
            return 0;
        else
            return 1;
    }
}
