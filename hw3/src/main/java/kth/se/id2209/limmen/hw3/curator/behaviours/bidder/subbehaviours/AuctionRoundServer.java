package kth.se.id2209.limmen.hw3.curator.behaviours.bidder.subbehaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.hw3.curator.CuratorAgent;
import kth.se.id2209.limmen.hw3.curator.model.Strategy;

/**
 * Behaviour for receiving CFP's from auctioneer and responding according to the strategy of the bidder
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class AuctionRoundServer extends CyclicBehaviour {

    /**
     * Main action of the behaviour, receives CFP's and takes the appropriate action.
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            double price = Double.parseDouble(msg.getContent());
            Strategy strategy =  ((CuratorAgent) myAgent).getStrategy();
            ((CuratorAgent)myAgent).updateLog("Call for proposals, current price: " + price);
            ((CuratorAgent)myAgent).updateLog("The lowest price to accept according to your predefined strategy is: " + strategy.getPrice());
            if (strategy.acceptPrice(price)) {
                ((CuratorAgent)myAgent).updateLog("You chose to accept the price");
                ACLMessage response = new ACLMessage(ACLMessage.PROPOSE);
                response.addReceiver(msg.getSender());
                myAgent.send(response);
            } else {
                ((CuratorAgent)myAgent).updateLog("You chose to reject the price");
            }
        } else {
            block();
        }
    }
}
