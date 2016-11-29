package kth.se.id2209.limmen.hw3.artistmanager.behaviours.closeauction;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;
import kth.se.id2209.limmen.hw3.artistmanager.model.AuctionResult;

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
        AuctionResult auctionResult = ((ArtistManagerAgent) myAgent).getAuctionResult();
        String closeMsg = "";
        if(auction.getWinner() != null){
            closeMsg = "Auction for " + auction.getArtifactTitle() + " closed at price: " + auctionResult.getPrice() + " | winner: " + auction.getWinner().getName().toString();
            closeAuctionMsg.setContent(closeMsg);
        }
        else{
            closeMsg = "Auction for " + auction.getArtifactTitle() + " closed at price: " + auctionResult.getPrice() + " | no buyer was found on this container";
            closeAuctionMsg.setContent(closeMsg);
        }
        closeAuctionMsg.setOntology("closed");
        ArrayList<AID> bidders = ((ArtistManagerAgent) myAgent).getBidders();
        for(AID aid : bidders){
            closeAuctionMsg.addReceiver(aid);
        }
        myAgent.send(closeAuctionMsg);
        ((ArtistManagerAgent) myAgent).updateLog(closeMsg);
    }
}
