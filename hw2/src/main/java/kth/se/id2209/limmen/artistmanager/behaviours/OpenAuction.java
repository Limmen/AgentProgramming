package kth.se.id2209.limmen.artistmanager.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.Auction;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Behaviour that opens up an action. It will send a query to all discovered bidders and ask who likes to participate
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class OpenAuction extends ProposeInitiator {

    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param msg message to send
     * @param store store for communicating with other behaviours
     */
    public OpenAuction(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     * This method will send a proposal for an auction to all discovered bidders
     *
     * @param auctionProposal message to send
     * @return vector of messages to send
     */
    @Override
    protected Vector prepareInitiations(ACLMessage auctionProposal) {
        DFAgentDescription[] bidders = (DFAgentDescription[]) getDataStore().get(ArtistManagerAgent.BIDDERS);
        auctionProposal = new ACLMessage(ACLMessage.PROPOSE);
        auctionProposal.setContent(((Auction) getDataStore().get(ArtistManagerAgent.AUCTION)).getArtifactTitle());
        auctionProposal.setOntology("AuctionServer");
        for (int i = 0; i < bidders.length; i++) {
            auctionProposal.addReceiver(bidders[i].getName());
        }
        Vector<ACLMessage> messages = new Vector();
        messages.add(auctionProposal);
        return messages;
    }

    /**
     * This method is called when all the responses have been collected or when the timeout is expired.
     * The used timeout is the minimum value of the slot replyBy of all the sent messages.
     * By response message we intend here all the accept-proposal, reject-proposal, not-understood received messages,
     * which are not not out-of-sequence according to the protocol rules.
     * This method collects responses from bidders and builds up a list of all the participants for the new auction.
     *
     * @param responses vector of response messages
     */
    protected void handleAllResponses(Vector responses) {
        Auction auction = (Auction) getDataStore().get(ArtistManagerAgent.AUCTION);
        System.out.println("OpenAuction received all responses");
        ArrayList<AID> participants = new ArrayList();
        for(Object object : responses){
            ACLMessage response = (ACLMessage) object;
            if(response.getPerformative() == ACLMessage.ACCEPT_PROPOSAL)
                participants.add(response.getSender());
        }
        auction.setParticipants(participants);
        getDataStore().put(ArtistManagerAgent.AUCTION, auction);
    }
}