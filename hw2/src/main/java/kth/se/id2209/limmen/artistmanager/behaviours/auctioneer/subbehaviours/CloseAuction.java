package kth.se.id2209.limmen.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.model.Auction;

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
        ACLMessage closeAuctionMsg = new ACLMessage(ACLMessage.INFORM);
        Auction auction = (Auction) getDataStore().get(ArtistManagerAgent.AUCTION);
        closeAuctionMsg.setContent("Auction for " + auction.getArtifactTitle() + " closed at price: " + auction.getCurrentPrice() + " | winner: " + auction.getWinner().getName().toString());
        closeAuctionMsg.setOntology("closed");
        DFAgentDescription[] bidders = (DFAgentDescription[]) getDataStore().get(ArtistManagerAgent.BIDDERS);
        for (int i = 0; i < bidders.length; i++) {
            closeAuctionMsg.addReceiver(bidders[i].getName());
        }
        myAgent.send(closeAuctionMsg);
    }
}
