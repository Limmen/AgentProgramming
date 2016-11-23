package kth.se.id2209.limmen.hw3.curator.behaviours.bidder.subbehaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.hw3.curator.CuratorAgent;

/**
 * Behaviour for receiving AUCTION-OPEN/AUCTION-CLOSED messages.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class AuctionServer extends CyclicBehaviour {


    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (msg.getOntology().equals("closed")) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("AUCTION CLOSED");
                System.out.println(msg.getContent());
                getDataStore().put(CuratorAgent.AUCTION_VALUATION, null);
                System.out.println("------------------------------------------------------------------------------------------------------------------");
            } else if (msg.getOntology().equals("open")) {
                System.out.println("------------------------------------------------------------------------------------------------------------------");
                System.out.println("AUCTION OPENED");
                String auctionName = msg.getContent();
                getDataStore().put(CuratorAgent.AUCTION_NAME, auctionName);
                System.out.println("Good of the auction is : '" + auctionName + "'");
                System.out.println("------------------------------------------------------------------------------------------------------------------");
            }
        } else {
            block();
        }
    }

}
