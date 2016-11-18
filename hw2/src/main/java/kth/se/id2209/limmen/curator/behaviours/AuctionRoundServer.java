package kth.se.id2209.limmen.curator.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;
import kth.se.id2209.limmen.curator.CuratorAgent;
import kth.se.id2209.limmen.curator.Strategy;

/**
 * Behaviour for receiving CFP's from auctioneer and responding according to the strategy of the bidder
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class AuctionRoundServer extends ProposeResponder {
    private boolean auctionEnded = false;
    private boolean acceptedPrice = false;

    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param mt message-template for the messages that this behaviour will respond to
     * @param s datastore for communicating with other behaviours
     */
    public AuctionRoundServer(Agent a, MessageTemplate mt, DataStore s) {
        super(a, mt, s);
    }

    /**
     * This method is called when the initiator's message is received that matches the message template
     * passed in the constructor.
     * This method will respond to CFP-messages that is received from auctioneers.
     *
     * @param propose message passed in the constructor
     * @return reply message
     * @throws NotUnderstoodException thrown when the message is not understood
     */
    protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException{
        ACLMessage response = propose.createReply();
        if(propose.getPerformative() == ACLMessage.CFP){
            auctionEnded = false;
            double price = Double.parseDouble(propose.getContent());
            Strategy strategy = (Strategy) getDataStore().get(CuratorAgent.AUCTION_STRATEGY);
            double valuation = (Double) getDataStore().get(CuratorAgent.AUCTION_VALUATION);
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Call for proposals, current price: " + price);
            System.out.println("The lowest price to accept according to your predefined strategy is: " + strategy.getPrice(valuation));
            if (strategy.acceptPrice(price, valuation)) {
                System.out.println("You chose to accept the price");
                response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                acceptedPrice = true;
            } else {
                System.out.println("You chose to reject the price");
                response.setPerformative(ACLMessage.REJECT_PROPOSAL);
            }
        } else{
            auctionEnded = true;
            response.setPerformative(ACLMessage.REJECT_PROPOSAL);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        return response;
    }
}
