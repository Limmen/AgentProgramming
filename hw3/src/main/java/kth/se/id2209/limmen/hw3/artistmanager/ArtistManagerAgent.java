package kth.se.id2209.limmen.hw3.artistmanager;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.hw3.HW3Agent;
import kth.se.id2209.limmen.hw3.artgallery.ArtGallery;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.AuctioneerBehaviour;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctionresultserver.AuctionResultServer;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.clonesserver.ClonesServer;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.closeauction.CloseAuction;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.selectwinner.SelectWinner;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.receivecommands.ReceiveCommands;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;
import kth.se.id2209.limmen.hw3.artistmanager.model.AuctionResult;

import java.io.IOException;
import java.util.ArrayList;


/**
 * ArtistManagerAgent that holds dutch auctions. Can be cloned and moved to other  containers. Can also synthesize
 * results from clones.
 * <p>
 * Inspired from example at: http://www.iro.umontreal.ca/~vaucher/Agents/Jade/Mobility.html
 *
 * @author Kim Hammar on 2016-11-24.
 */
public class ArtistManagerAgent extends GuiAgent implements HW3Agent {
    private ArtGallery artGallery = new ArtGallery();
    private AID controller;
    private Location destination;
    private AID parent;
    transient protected ArtistManagerGUI myGui;
    private ParallelBehaviour parallelBehaviour;
    private String log = "";
    private DataStore dataStore;
    private Auction auction;
    private ArrayList<AuctionResult> clonesResult = new ArrayList<>();
    ArrayList<ACLMessage> possibleWinners = new ArrayList<>();
    ArrayList<AID> bidders = new ArrayList<>();
    private AuctionResult auctionResult;
    private boolean won = false;
    private boolean auctionOngoing = false;
    private boolean auctionReadyToBeClosed = false;
    private boolean biddingClosed = false;
    private boolean haveReported = false;

    /**
     * Agent initialization. Called by the JADE runtime environment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ArtistManagerAgent " + getAID().getName() + " starting up.");

        /**
         * Retrieve initialization arguments and initialize controller, parent, datastore
         */
        Object[] args = getArguments();
        controller = (AID) args[0];
        parent = getAID();
        destination = here();
        dataStore = new DataStore();
        init();

        /**
         * Create behaviours and set datastore
         */
        parallelBehaviour = new ParallelBehaviour();
        parallelBehaviour.setDataStore(dataStore);
        ReceiveCommands receiveCommands = new ReceiveCommands(this);
        receiveCommands.setDataStore(parallelBehaviour.getDataStore());
        ClonesServer clonesServer = new ClonesServer();
        clonesServer.setDataStore(parallelBehaviour.getDataStore());
        AuctionResultServer auctionResultServer = new AuctionResultServer();
        auctionResultServer.setDataStore(parallelBehaviour.getDataStore());

        /**
         * Add subbehaviours
         */
        parallelBehaviour.addSubBehaviour(receiveCommands);
        parallelBehaviour.addSubBehaviour(clonesServer);
        parallelBehaviour.addSubBehaviour(auctionResultServer);

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
        myGui = new ArtistManagerGUI(this);
        myGui.setVisible(true);
        myGui.setLocation(destination.getName());
        updateLog(" Initializing myself at " + destination.getName());
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
        this.parent = getAID();
    }

    /**
     * Called after agent have been succesfully cloned
     */
    protected void afterClone() {
        log = "";
        dataStore = new DataStore();
        init();
    }

    /**
     * Method for starting an auction at the curent contianer
     *
     * @param auction auction to start
     */
    public void startAuction(Auction auction) {
        auctionOngoing = true;
        biddingClosed = false;
        myGui.updateBiddingClosed();
        myGui.updateAuctionOnGoing();
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

    /**
     * Method for updating the log in the gui
     *
     * @param info text to add to the log
     */
    public void updateLog(String info) {
        log = log + info + "\n";
        myGui.updateLog(log);
    }

    /**
     * Method called when a message from a clone-agent have been received with results from an auction
     *
     * @param auctionResult result of the auction
     * @param clone         clone that sent the result
     */
    public void addCloneAuctionResult(AuctionResult auctionResult, AID clone) {
        clonesResult.add(auctionResult);
        myGui.updateClonesResult();
        updateLog("Received subresult from clone: " + clone.getLocalName());
    }

    /**
     * Method to synthesize the results from the clones.
     */
    public void synthesizeResults() {
        updateLog("Received results from " + clonesResult.size() + " clones that performed dutch auctions. The subresults are:");
        AuctionResult winner = null;
        for (AuctionResult auctionResult : clonesResult) {
            updateLog("Highest bid received by clone " + auctionResult.getAuctioneer().getLocalName() + " is: " + auctionResult.getPrice());
            if (winner == null && auctionResult.isSold()) {
                winner = auctionResult;
            } else {
                if (auctionResult.getPrice() > winner.getPrice() && auctionResult.isSold()) {
                    winner = auctionResult;
                }
            }
        }
        if (winner != null)
            updateLog("The highest bid for the good is: " + winner.getPrice() + " this was from the auction made by clone: " + winner.getAuctioneer().getLocalName());
        else {
            updateLog("The good was not sold by any of the clones");
        }
        for (AuctionResult auctionResult : clonesResult) {
            ACLMessage response = new ACLMessage(ACLMessage.INFORM);
            response.setOntology("auction-result");
            try {
                response.setContentObject(winner);
                response.addReceiver(auctionResult.getAuctioneer());
                send(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clonesResult = new ArrayList<>();
        myGui.updateClonesResult();
    }

    /**
     * Method to send a message with auctionResult to a parent-agent
     */
    public void reportToParent() {
        boolean sold = possibleWinners.size() > 0;
        AuctionResult auctionResult = new AuctionResult("", auction.getCurrentPrice(), sold, getAID());
        if (parent != getAID()) {
            ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
            try {
                inform.setContentObject(auctionResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inform.setOntology("clones-update");
            inform.addReceiver(parent);
            send(inform);
            biddingClosed = true;
            haveReported = true;
            myGui.updateBiddingClosed();
        }
    }

    /**
     * Method to initialize closing of the auction (i.e notify all bidders).
     */
    public void closeAuction() {
        SelectWinner selectWinner = new SelectWinner();
        selectWinner.setDataStore(dataStore);
        addBehaviour(selectWinner);
        CloseAuction closeAuction = new CloseAuction();
        closeAuction.setDataStore(dataStore);
        addBehaviour(closeAuction);
        won = false;
        auctionOngoing = false;
        auctionReadyToBeClosed = false;
        biddingClosed = false;
        haveReported = false;
        myGui.updateBiddingClosed();
        myGui.updateAuctionDone();
        myGui.updateAuctionOnGoing();
    }

    /**
     * Method to flag that the bidding is closed and we are ready to report back to parent
     */
    public void biddingClosed() {
        biddingClosed = true;
        myGui.updateBiddingClosed();
        updateLog("Auction done ready to migrate back to parent container");
    }

    /**
     * Method to flag that we have received auction result from parent
     */
    public void receivedAuctionResultFromParent() {
        auctionReadyToBeClosed = true;
        myGui.updateAuctionDone();
    }

    /**
     * Getters & Setters
     */

    public boolean isAuctionOngoing() {
        return auctionOngoing;
    }

    public ArrayList<ACLMessage> getPossibleWinners() {
        return possibleWinners;
    }

    public void setPossibleWinners(ArrayList<ACLMessage> possibleWinners) {
        this.possibleWinners = possibleWinners;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean isAuctionReadyToBeClosed() {
        return auctionReadyToBeClosed;
    }

    public ArrayList<AID> getBidders() {
        return bidders;
    }

    public void setBidders(ArrayList<AID> bidders) {
        this.bidders = bidders;
    }

    public boolean isBiddingClosed() {
        return biddingClosed;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public ArrayList<AuctionResult> getClonesResult() {
        return clonesResult;
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

    public AuctionResult getAuctionResult() {
        return auctionResult;
    }

    public void setAuctionResult(AuctionResult auctionResult) {
        this.auctionResult = auctionResult;
    }

    public boolean isHaveReported() {
        return haveReported;
    }
}
