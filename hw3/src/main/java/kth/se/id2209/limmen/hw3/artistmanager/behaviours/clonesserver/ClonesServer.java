package kth.se.id2209.limmen.hw3.artistmanager.behaviours.clonesserver;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.AuctionResult;

/**
 *
 * Behaviour for receiving auctionresults from clones
 *
 * @author Kim Hammar on 2016-11-25.
 */
public class ClonesServer extends CyclicBehaviour {

    /**
     * Main action of the behaviour, receives messages from clones and takes the appropriate action.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("clones-update");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            try {
                AuctionResult auctionResult = (AuctionResult) msg.getContentObject();
                ((ArtistManagerAgent) myAgent).addCloneAuctionResult(auctionResult, msg.getSender());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        } else {
            block();
        }
    }
}
