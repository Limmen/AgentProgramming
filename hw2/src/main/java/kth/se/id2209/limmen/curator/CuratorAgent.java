package kth.se.id2209.limmen.curator;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import kth.se.id2209.limmen.curator.behaviours.bidder.BidderBehaviour;
import kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours.ChooseStrategy;
import kth.se.id2209.limmen.curator.model.Strategy;

import java.util.ArrayList;

/**
 * CuratorAgent that participates in auctions for art-artifacts.
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class CuratorAgent extends Agent {
    public static String AUCTION_NAME = "Auction Name";
    public static String AUCTION_VALUATION = "Auction Valuation";
    public static String AUCTION_STRATEGY = "Auction Strategy";
    private ArrayList<Strategy> strategies = new ArrayList();

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("CuratorAgent " + getAID().getName() + " starting up.");
        strategies.add(new Strategy(1.5));
        strategies.add(new Strategy(1.25));
        strategies.add(new Strategy(1));
        strategies.add(new Strategy(0.75));
        strategies.add(new Strategy(0.5));
        strategies.add(new Strategy(0.25));
        registerAtYellowPages();

        /**
         * Create behaviours and set datastores
         */
        DataStore dataStore = new DataStore();
        ChooseStrategy chooseStrategy = new ChooseStrategy(dataStore);
        BidderBehaviour bidderBehaviour = new BidderBehaviour(dataStore);
        addBehaviour(chooseStrategy);
        /**
         * Add behaviour
         */
        addBehaviour(bidderBehaviour);

    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination.
     * DeRegisters from the DF registry.
     */
    @Override
    public void takeDown() {
        System.out.println("CuratorAgent " + getAID().getName() + " terminating.");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    /**
     * Register the service as an art-bidder at the "yellow pages" aka the Directory Facilitator (DF)
     * that runs on the platform.
     */
    private void registerAtYellowPages() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("art-bidder");
        serviceDescription.setName("art-bidder");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public ArrayList<Strategy> getStrategies() {
        return strategies;
    }
}
