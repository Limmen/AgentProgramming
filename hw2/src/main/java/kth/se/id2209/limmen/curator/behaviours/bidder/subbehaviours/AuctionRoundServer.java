package kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.curator.CuratorAgent;
import kth.se.id2209.limmen.curator.model.Strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Behaviour for receiving CFP's from auctioneer and responding according to the strategy of the bidder
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class AuctionRoundServer extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            double price = Double.parseDouble(msg.getContent());
            Strategy strategy = (Strategy) getDataStore().get(CuratorAgent.AUCTION_STRATEGY);
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Call for proposals, current price: " + price);
            Double valuation = (Double) getDataStore().get(CuratorAgent.AUCTION_VALUATION);
            if(valuation == null){
                valuation = chooseValuation(price);
                getDataStore().put(CuratorAgent.AUCTION_VALUATION, valuation);
            }
            System.out.println("The lowest price to accept according to your predefined strategy is: " + strategy.getPrice(valuation));
            if (strategy.acceptPrice(price, valuation)) {
                System.out.println("You chose to accept the price");
                ACLMessage response = new ACLMessage(ACLMessage.PROPOSE);
                response.addReceiver(msg.getSender());
                myAgent.send(response);
            } else {
                System.out.println("You chose to reject the price");
            }
        } else {
            block();
        }
    }

    private double chooseValuation(double price){
        System.out.println("You have not set a valuation for the good of this auction, please enter your valuation below");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        long startTime = System.currentTimeMillis();
        double valuation = price / 2;
        try {
            while ((System.currentTimeMillis() - startTime) < 8000
                    && !in.ready()) {
            }

            if (in.ready()) {
                valuation = Double.parseDouble(in.readLine());
                System.out.println("You entered: " + valuation);
            } else {
                System.out.println("You did not enter any valuation, using default valuation 50% of initial price: " + valuation);
            }
        } catch(IOException io){
            io.printStackTrace();
        }
        return valuation;
    }
}
