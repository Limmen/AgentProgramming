package kth.se.id2209.limmen.artistmanager.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.Auction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Behaviour that sends a CFP to all participants of the auction and then collects the bids.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class AuctionRound extends ProposeInitiator {

    private boolean foundWinner = false;

    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param msg message to send
     * @param store store for communicating with other behaviours
     */
    public AuctionRound(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     *
     * @param cfpMessage cfpMessage passed in the constructor
     * @return vector of refined requests to send
     */
    protected Vector prepareInitiations(ACLMessage cfpMessage) {
        cfpMessage = new ACLMessage(ACLMessage.CFP);
        Auction auction = (Auction) getDataStore().get(ArtistManagerAgent.AUCTION);
        for(AID aid : auction.getParticipants()){
            cfpMessage.addReceiver(aid);
        }
        cfpMessage.setContent(Double.toString(auction.getCurrentPrice()));
        cfpMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_DUTCH_AUCTION);
        cfpMessage.setOntology("Ontology(Class(AuctionRound partial AchieveREInitiator))");
        cfpMessage.setReplyByDate(new Date(System.currentTimeMillis() + 100000));
        Vector<ACLMessage> messages = new Vector();
        messages.add(cfpMessage);
        return messages;
    }

    /**
     * This method is called when all the responses have been collected or when the timeout is expired.
     * The used timeout is the minimum value of the slot replyBy of all the sent messages.
     * By response message we intend here all the accept-proposal, reject-proposal, not-understood received messages,
     * which are not not out-of-sequence according to the protocol rules.
     * This method will check if any bidders accepted the price and if so build a list of all possible winners
     * and pass that list on to the next behaviour.
     *
     * @param responses received responses
     */
    protected void handleAllResponses(Vector responses) {
        ArrayList<AID> possibleBuyers = new ArrayList<>();
        for (Object object : responses) {
            ACLMessage response = (ACLMessage) object;
         if(response.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
             possibleBuyers.add(response.getSender());
         }
        }
        getDataStore().put(ArtistManagerAgent.WINNERS, possibleBuyers);
        foundWinner = possibleBuyers.size() > 0;
    }


    /**
     * Called just before termination of this behaviour.
     *
     * @return 1 if a winner was found (aka the auction is finnished, otherwise 0.
     */
    @Override
    public int onEnd() {
        if (foundWinner)
            return 1;
        else
            return 0;
    }
}
