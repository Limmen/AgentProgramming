package kth.se.id2209.limmen.hw3.curator;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import kth.se.id2209.limmen.hw3.HW3Agent;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.receivecommands.ReceiveCommands;
import kth.se.id2209.limmen.hw3.curator.model.Strategy;

import java.util.ArrayList;

/**
 * CuratorAgent that participates in auctions for art-artifacts.
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class CuratorAgent extends GuiAgent implements HW3Agent {
    public static String AUCTION_NAME = "Auction Name";
    public static String AUCTION_VALUATION = "Auction Valuation";
    public static String AUCTION_STRATEGY = "Auction Strategy";
    private ArrayList<Strategy> strategies = new ArrayList();
    private AID controller;
    private Location destination;
    transient protected CuratorAgentGUI myGui;
    private ParallelBehaviour parallelBehaviour;
    private String log = "";

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
        /*
        DataStore dataStore = new DataStore();
        ChooseStrategy chooseStrategy = new ChooseStrategy(dataStore);
        BidderBehaviour bidderBehaviour = new BidderBehaviour(dataStore);
        addBehaviour(chooseStrategy);
        */
        /**
         * Add behaviour
         */
        //addBehaviour(bidderBehaviour);

        // Retrieve arguments passed during this agent creation
        Object[] args = getArguments();
        controller = (AID) args[0];
        destination = here();

        init();

        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new ReceiveCommands(this));
        addBehaviour(parallelBehaviour);

    }

    void init() {
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());
        // Create and display the gui
        myGui = new CuratorAgentGUI(this);
        myGui.setVisible(true);
        myGui.setLocation(destination.getName());
        log = log + " Initializing myself at " + destination.getName() + " \n";
        myGui.updateLog(log);
    }

    protected void onGuiEvent(GuiEvent e) {
        //No interaction with the gui
    }

    protected void beforeMove() {
        System.out.println("Moving now to location : " + destination.getName());
        log = log + "Moving now to location : " + destination.getName() + "\n";
        myGui.updateLog(log);
        myGui.setVisible(false);
        myGui.dispose();
    }

    protected void afterMove() {
        init();
        log = log + "Arrived at location : " + destination.getName() + "\n";
        myGui.updateLog(log);
    }

    protected void beforeClone() {
        log = log + "Cloning myself to location : " + destination.getName() + "\n";
        myGui.updateLog(log);
    }

    protected void afterClone() {
        log = "";
        init();
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

    public Location getDestination() {
        return destination;
    }

    public AID getController() {
        return controller;
    }

    public CuratorAgentGUI getMyGui() {
        return myGui;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
