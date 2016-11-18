package kth.se.id2209.limmen.artistmanager.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.Auction;

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
        System.out.println("Select winner");
        Auction auction = ((Auction) getDataStore().get(ArtistManagerAgent.AUCTION));
        ArrayList<AID> possibleWinners = (ArrayList<AID>) getDataStore().get(ArtistManagerAgent.WINNERS);
        AID winner = possibleWinners.get(0);
        ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        reply.setOntology("Ontology(Class(SelectWinner partial OneShotBehaviour))");
        reply.setContent("Your won auction for artifact '" + auction.getArtifactTitle() + "' with a bid of " + auction.getCurrentPrice());
        reply.addReceiver(winner);
        myAgent.send(reply);
        possibleWinners.remove(winner);
        reply = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
        reply.setOntology("Ontology(Class(SelectWinner partial OneShotBehaviour))");
        reply.setContent("Your bid for artifact " + auction.getArtifactTitle() + " was not accepted");
        for(AID loser : possibleWinners){
            reply.addReceiver(loser);
        }
        myAgent.send(reply);
    }
}
