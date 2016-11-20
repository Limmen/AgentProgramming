package kth.se.id2209.limmen.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.model.Auction;

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
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("SENDING CFP");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
        Auction auction = (Auction) getDataStore().get(ArtistManagerAgent.AUCTION);
        DFAgentDescription[] bidders = (DFAgentDescription[]) getDataStore().get(ArtistManagerAgent.BIDDERS);
        for (int i = 0; i < bidders.length; i++) {
            cfpMessage.addReceiver(bidders[i].getName());
        }
        cfpMessage.setContent(Double.toString(auction.getCurrentPrice()));
        cfpMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION);
        cfpMessage.setOntology("Ontology(Class(SendCFP partial OneShotBehaviour))");
        myAgent.send(cfpMessage);
    }
}
