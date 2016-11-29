package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;

import java.util.ArrayList;


/**
 * Behaviour for sending an INFORM message stating that an auction is starting.
 *
 * @author Kim Hammar on 2016-11-19.
 */
public class OpenAuction extends OneShotBehaviour {

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        ACLMessage closeAuctionMsg = new ACLMessage(ACLMessage.INFORM);
        closeAuctionMsg.setOntology("open");
        Auction auction = ((ArtistManagerAgent) myAgent).getAuction();
        closeAuctionMsg.setContent(auction.getArtifactTitle());
        ArrayList<AID> bidders = ((ArtistManagerAgent) myAgent).getBidders();
        for(AID aid : bidders){
            closeAuctionMsg.addReceiver(aid);
        }
        myAgent.send(closeAuctionMsg);
        ((ArtistManagerAgent) myAgent).updateLog("AUCTION OPENED, good is: " + auction.getArtifactTitle());
    }
}
