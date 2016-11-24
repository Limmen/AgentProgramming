package kth.se.id2209.limmen.hw3.artistmanager;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import kth.se.id2209.limmen.hw3.HW3Agent;
import kth.se.id2209.limmen.hw3.artgallery.ArtGallery;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.AuctioneerBehaviour;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.receivecommands.ReceiveCommands;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;


/**
 * ArtistManagerAgent that holds dutch auctions.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class ArtistManagerAgent extends GuiAgent implements HW3Agent {
    private ArtGallery artGallery = new ArtGallery();
    public static String BIDDERS = "Bidders";
    public static String WINNERS = "Winners";

    private AID controller;
    private Location destination;
    transient protected ArtistManagerGUI myGui;
    private ParallelBehaviour parallelBehaviour;
    private String log = "";
    private DataStore dataStore;
    private Auction auction;

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ArtistManagerAgent " + getAID().getName() + " starting up.");

        // Retrieve arguments passed during this agent creation
        Object[] args = getArguments();
        controller = (AID) args[0];
        destination = here();
        dataStore = new DataStore();
        init();

        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.setDataStore(dataStore);
        ReceiveCommands receiveCommands = new ReceiveCommands(this);
        receiveCommands.setDataStore(parallelBehaviour.getDataStore());
        parallelBehaviour.addSubBehaviour(receiveCommands);
        addBehaviour(parallelBehaviour);

    }

    void init() {
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());
        // Create and display the gui
        myGui = new ArtistManagerGUI(this);
        myGui.setVisible(true);
        myGui.setLocation(destination.getName());
        updateLog(" Initializing myself at " + destination.getName());
    }

    protected void onGuiEvent(GuiEvent e) {
        //No interaction with the gui
    }

    protected void beforeMove() {
        updateLog("Moving now to location : " + destination.getName());
        myGui.setVisible(false);
        myGui.dispose();
    }

    protected void afterMove() {
        init();
        updateLog("Arrived at location : " + destination.getName());
    }

    protected void beforeClone() {
        updateLog("Cloning myself to location : " + destination.getName());
    }

    protected void afterClone() {
        log = "";
        dataStore = new DataStore();
        init();
    }

    public void startAuction(Auction auction) {
        this.auction = auction;
        AuctioneerBehaviour auctioneerBehaviour = new AuctioneerBehaviour();
        auctioneerBehaviour.setDataStore(parallelBehaviour.getDataStore());
        parallelBehaviour.addSubBehaviour(auctioneerBehaviour);
    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination.
     */
    @Override
    public void takeDown() {
        System.out.println("ArtistManagerAgent " + getAID().getName() + " terminating.");
    }

    public ArtGallery getArtGallery() {
        return artGallery;
    }

    public AID getController() {
        return controller;
    }

    public Location getDestination() {
        return destination;
    }

    public ArtistManagerGUI getMyGui() {
        return myGui;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void updateLog(String info){
        log = log + info + "\n";
        myGui.updateLog(log);
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}
