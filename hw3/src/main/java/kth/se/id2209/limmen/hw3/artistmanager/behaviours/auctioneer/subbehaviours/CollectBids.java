package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;

import java.util.ArrayList;

/**
 * Behaviour for collecting bids from bidders. This behaviour is invoked timeout-milliseconds after sending out the
 * CFP.
 *
 * @author Kim Hammar on 2016-11-19.
 */
public class CollectBids extends WakerBehaviour {
    private boolean receivedBid = false;

    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param timeout timeout before invoking the behaviour.
     */
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
            ((ArtistManagerAgent) myAgent).setPossibleWinners(receivedBids);
            receivedBid = true;
        }
    }

    /**
     * Called just before termination of this behaviour.
     *
     * @return 0 if a bid was received, otherwise 1
     */
    public int onEnd() {
        if (receivedBid)
            return 0;
        else
            return 1;
    }
}
