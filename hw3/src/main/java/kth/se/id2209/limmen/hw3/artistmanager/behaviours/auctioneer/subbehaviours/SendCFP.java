package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;

import java.util.ArrayList;


/**
 * Behaviour for sending a CFP to all bidders in a particular auction
 *
 * @author Kim Hammar on 2016-11-19.
 */
public class SendCFP extends OneShotBehaviour {

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        ((ArtistManagerAgent) myAgent).updateLog("SENDING CFP");
        ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
        Auction auction = ((ArtistManagerAgent) myAgent).getAuction();
        ArrayList<AID> bidders = ((ArtistManagerAgent) myAgent).getBidders();
        for(AID aid : bidders){
            cfpMessage.addReceiver(aid);
        }
        cfpMessage.setContent(Double.toString(auction.getCurrentPrice()));
        cfpMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION);
        cfpMessage.setOntology("Ontology(Class(SendCFP partial OneShotBehaviour))");
        myAgent.send(cfpMessage);
    }
}
