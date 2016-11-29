package kth.se.id2209.limmen.hw3.artistmanager.behaviours.selectwinner;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;
import kth.se.id2209.limmen.hw3.artistmanager.model.AuctionResult;

import java.util.ArrayList;

/**
 * Behaviour that selects a winner in the case where 1 or more bidders have accepted the price proposed by the
 * auctioneer.
 *
 * @author Kim Hammar on 2016-11-18.
 */
public class SelectWinner extends OneShotBehaviour {

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        Auction auction = ((ArtistManagerAgent) myAgent).getAuction();
        AuctionResult auctionResult = ((ArtistManagerAgent) myAgent).getAuctionResult();
        ArrayList<ACLMessage> possibleWinners = ((ArtistManagerAgent) myAgent).getPossibleWinners();
        ACLMessage reply = null;
        if(auctionResult.getAuctioneer().equals(myAgent.getAID())) {
            AID winner = possibleWinners.get(0).getSender();
            auction.setWinner(winner);
            ((ArtistManagerAgent) myAgent).setAuction(auction);
            reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            reply.setOntology("Ontology(Class(SelectWinner partial OneShotBehaviour))");
            reply.setContent("Your won auction for artifact '" + auction.getArtifactTitle() + "' with a bid of " + auction.getCurrentPrice());
            reply.addReceiver(winner);
            myAgent.send(reply);
            possibleWinners.remove(winner);
        }
        reply = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
        reply.setOntology("Ontology(Class(SelectWinner partial OneShotBehaviour))");
        reply.setContent("Your bid for artifact " + auction.getArtifactTitle() + " was not accepted");
        for(ACLMessage loser : possibleWinners){
            reply.addReceiver(loser.getSender());
        }
        myAgent.send(reply);
    }
}
