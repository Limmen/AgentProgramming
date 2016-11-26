package kth.se.id2209.limmen.hw3.curator;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.DataStore;
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
import kth.se.id2209.limmen.hw3.curator.behaviours.bidder.BidderBehaviour;
import kth.se.id2209.limmen.hw3.curator.model.Strategy;

import java.util.ArrayList;

/**
 * CuratorAgent that participates in auctions for art-artifacts. Can be cloned and moved to other  containers.
 *
 * Inspired from example at: http://www.iro.umontreal.ca/~vaucher/Agents/Jade/Mobility.html
 *
 * @author Kim Hammar on 2016-11-23.
 */
public class CuratorAgent extends GuiAgent implements HW3Agent {
    public static String AUCTION_NAME = "Auction Name";
    private ArrayList<Strategy> strategies = new ArrayList();
    private AID controller;
    private Location destination;
    transient protected CuratorAgentGUI myGui;
    private ParallelBehaviour parallelBehaviour;
    private String log = "";
    private DataStore dataStore;
    private Strategy strategy;

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        /**
         * Initialize session data, register at DF, create datastore, retrieve initialization arguments
         */
        System.out.println("CuratorAgent " + getAID().getName() + " starting up.");
        strategies.add(new Strategy(1.5));
        strategies.add(new Strategy(1.25));
        strategies.add(new Strategy(1));
        strategies.add(new Strategy(0.75));
        strategies.add(new Strategy(0.5));
        strategies.add(new Strategy(0.25));
        registerAtYellowPages();
        strategy = strategies.get(2);
        dataStore = new DataStore();
        Object[] args = getArguments();
        controller = (AID) args[0];
        destination = here();
        init();

        /**
         * Create behaviours and set datastores
         */
        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.setDataStore(dataStore);
        ReceiveCommands receiveCommands = new ReceiveCommands(this);
        receiveCommands.setDataStore(parallelBehaviour.getDataStore());
        BidderBehaviour bidderBehaviour = new BidderBehaviour(dataStore);

        /**
         * Add sub-behaviours
         */
        parallelBehaviour.addSubBehaviour(receiveCommands);
        parallelBehaviour.addSubBehaviour(bidderBehaviour);

        /**
         * Add behaviour
         */
        addBehaviour(parallelBehaviour);

    }

    /**
     * Initialization function that is called after being created/cloned/moved, initializes the GUI and updates the log.
     */
    void init() {
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());
        // Create and display the gui
        myGui = new CuratorAgentGUI(this);
        myGui.setVisible(true);
        myGui.setLocation(destination.getName());
        updateLog("Initializing myself at " + destination.getName());
    }

    protected void onGuiEvent(GuiEvent e) {
        //No interaction with the gui
    }

    /**
     * Called just before agent is moved
     */
    protected void beforeMove() {
        updateLog("Moving now to location : " + destination.getName());
        myGui.setVisible(false);
        myGui.dispose();
    }

    /**
     * Called after agent have successfully moved to a new container
     */
    protected void afterMove() {
        init();
        updateLog("Arrived at location : " + destination.getName());
    }

    /**
     * Called just before agent is cloned
     */
    protected void beforeClone() {
        updateLog("Cloning myself to location : " + destination.getName());
    }

    /**
     * Called after agent have been succesfully cloned
     */
    protected void afterClone() {
        log = "";
        registerAtYellowPages();
        dataStore = new DataStore();
        strategy = strategies.get(2);
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

    public void updateStrategy(Strategy strategy) {
        this.strategy = strategy;
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

    public Strategy getStrategy(){
        return strategy;
    }

    /**
     * Method for updating the log in the gui
     *
     * @param info text to add to the log
     */
    public void updateLog(String info){
        log = log + info + "\n";
        myGui.updateLog(log);
    }
}
