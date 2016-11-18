package kth.se.id2209.limmen.curator.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;
import kth.se.id2209.limmen.curator.CuratorAgent;
import kth.se.id2209.limmen.curator.Strategy;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Behaviour for receiving AUCTION-OPEN/AUCTION-CLOSED messages.
 * When joining a new auction this behaviour will ask the user for a strategy for the auction depending on his/hers
 * valuation of the artifact.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class AuctionServer extends ProposeResponder {


    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param mt message-template for the messages that this behaviour will respond to
     * @param store datastore for communicating with other behaviours
     */
    public AuctionServer(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
        getDataStore().put(CuratorAgent.AUCTION_ACTIVE, false);
    }

    /**
     * This method is called when the initiator's message is received that matches the message template
     * passed in the constructor.
     * This method will respond to AUCTION-CLOSED/AUCTION-OPEN messages that is received from auctioneers.
     *
     * @param propose message passed in the constructor
     * @return reply message
     * @throws NotUnderstoodException thrown when the message is not understood
     */
    protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException {
        if(propose.getPerformative() == ACLMessage.INFORM){
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Auction closed");
            System.out.println(propose.getContent());
            getDataStore().put(CuratorAgent.AUCTION_ACTIVE, false);
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            return  null;
        }else {
            boolean auctionActive = (boolean) getDataStore().get(CuratorAgent.AUCTION_ACTIVE);
            ACLMessage reply = propose.createReply();
            if(auctionActive){
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
            }else {
                String auctionName = propose.getContent();
                getDataStore().put(CuratorAgent.AUCTION_NAME, auctionName);
                chooseStrategy(auctionName);
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            }
            return reply;
        }
    }

    /**
     * Method that interacts with the user to choose strategy for the auction.
     *
     * @param artifact artifact of the auction.
     */
    private void chooseStrategy(String artifact){
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("AUCTION STARTED. The artifact of the auction is: " + artifact);
        System.out.println("Enter your valuation of the artifact: ");
        Double valuation = Double.parseDouble(scanner.nextLine());
        getDataStore().put(CuratorAgent.AUCTION_VALUATION, valuation);
        ArrayList<Strategy> strategies = ((CuratorAgent) myAgent).getStrategies();
        while (true) {
            System.out.println("Choose strategy for the auction by entering the associated number");
            int num = 0;
            for (Strategy strategy : strategies) {
                System.out.println(num + " : " + strategy.toString());
                num++;
            }
            int strategy = Integer.parseInt(scanner.nextLine());
            if (strategy >= 0 && strategy < num) {
                getDataStore().put(CuratorAgent.AUCTION_STRATEGY, strategies.get(strategy));
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }

}
