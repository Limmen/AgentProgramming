package kth.se.id2209.limmen.hw3.artistmanager;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import kth.se.id2209.limmen.hw3.HW3Agent;
import kth.se.id2209.limmen.hw3.artgallery.ArtGallery;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.receivecommands.ReceiveCommands;


/**
 * ArtistManagerAgent that holds dutch auctions.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class ArtistManagerAgent extends GuiAgent implements HW3Agent {
    private ArtGallery artGallery = new ArtGallery();
    public static String BIDDERS = "Bidders";
    public static String AUCTION = "Auction";
    public static String WINNERS = "Winners";

    private AID controller;
    private Location destination;
    transient protected ArtistManagerGUI myGui;
    private ParallelBehaviour parallelBehaviour;
    private String log = "";

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

        init();

        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new ReceiveCommands(this));
        addBehaviour(parallelBehaviour);

    }

    void init() {
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());
        // Create and display the gui
        myGui = new ArtistManagerGUI(this);
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
}
