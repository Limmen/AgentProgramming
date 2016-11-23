package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;


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
        closeAuctionMsg.setContent(((Auction) getDataStore().get(ArtistManagerAgent.AUCTION)).getArtifactTitle());
        DFAgentDescription[] bidders = (DFAgentDescription[]) getDataStore().get(ArtistManagerAgent.BIDDERS);
        for (int i = 0; i < bidders.length; i++) {
            closeAuctionMsg.addReceiver(bidders[i].getName());
        }
        myAgent.send(closeAuctionMsg);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("AUCTION OPENED, good is: " + ((Auction) getDataStore().get(ArtistManagerAgent.AUCTION)).getArtifactTitle());
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }
}
