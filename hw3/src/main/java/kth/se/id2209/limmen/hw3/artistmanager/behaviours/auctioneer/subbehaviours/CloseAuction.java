package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;

import java.util.ArrayList;


/**
 * Behaviour for closing an ongoing auction. Will send a inform message to all participates informing that the auction
 * is finished and closed.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class CloseAuction extends OneShotBehaviour {

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        ACLMessage closeAuctionMsg = new ACLMessage(ACLMessage.INFORM);
        Auction auction = ((ArtistManagerAgent) myAgent).getAuction();
        if(auction.getWinner() != null)
        closeAuctionMsg.setContent("Auction for " + auction.getArtifactTitle() + " closed at price: " + auction.getCurrentPrice() + " | winner: " + auction.getWinner().getName().toString());
        else
            closeAuctionMsg.setContent("Auction for " + auction.getArtifactTitle() + " closed at price: " + auction.getCurrentPrice() + " | no buyer was found, reached the reserved price");
        closeAuctionMsg.setOntology("closed");
        ArrayList<AID> bidders = (ArrayList<AID>) getDataStore().get(ArtistManagerAgent.BIDDERS);
        for(AID aid : bidders){
            closeAuctionMsg.addReceiver(aid);
        }
        myAgent.send(closeAuctionMsg);
    }
}