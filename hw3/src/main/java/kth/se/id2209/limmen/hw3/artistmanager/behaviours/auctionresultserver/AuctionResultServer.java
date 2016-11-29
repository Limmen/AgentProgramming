package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctionresultserver;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.AuctionResult;

/**
 * Behaviour for receiving result of auction from parent agent
 *
 * @author Kim Hammar on 2016-11-29.
 */
public class AuctionResultServer extends CyclicBehaviour {


    /**
     * Main action of the behaviour, receives messages from parent and takes the appropriate action.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchOntology("auction-result");
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            try {
                AuctionResult auctionResult = (AuctionResult) msg.getContentObject();
                boolean won = myAgent.getAID().equals(auctionResult.getAuctioneer());
                ((ArtistManagerAgent) myAgent).setAuctionResult(auctionResult);
                ((ArtistManagerAgent) myAgent).setWon(won);
                ((ArtistManagerAgent) myAgent).receivedAuctionResultFromParent();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        } else {
            block();
        }
    }
}

