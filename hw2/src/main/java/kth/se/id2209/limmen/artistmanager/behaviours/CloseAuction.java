package kth.se.id2209.limmen.artistmanager.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.Auction;

/**
 * Behaviour for closing an ongoing auction. Will send a inform message to all participates informing that the auction
 * is finnished and closed.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class CloseAuction extends OneShotBehaviour {

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        System.out.println("send close auction msg");
        ACLMessage closeAuctionMsg = new ACLMessage(ACLMessage.INFORM);
        Auction auction = (Auction) getDataStore().get(ArtistManagerAgent.AUCTION);
        closeAuctionMsg.setContent("Auction for " + auction.getArtifactTitle() + " closed at price: " + auction.getCurrentPrice());
        closeAuctionMsg.setOntology("AuctionServer");
        for(AID aid : auction.getParticipants()){
            closeAuctionMsg.addReceiver(aid);
        }
        myAgent.send(closeAuctionMsg);
    }
}
