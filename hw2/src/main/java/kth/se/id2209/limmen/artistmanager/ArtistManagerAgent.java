package kth.se.id2209.limmen.artistmanager;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.artgallery.ArtGallery;
import kth.se.id2209.limmen.artistmanager.behaviours.*;

/**
 * ArtistManagerAgent that holds dutch auctions.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class ArtistManagerAgent extends Agent {
    private ArtGallery artGallery = new ArtGallery();
    public static String BIDDERS = "Bidders";
    public static String AUCTION = "Auction";
    public static String WINNERS = "Winners";
    private static final String FIND_BIDDERS_STATE = "Find Bidders";
    private static final String DECIDE_AUCTION_DETAILS_STATE = "Decide auction details";
    private static final String OPEN_AUCTION_STATE = "Open auction";
    private static final String AUCTION_ROUND_STATE = "Auction round";
    private static final String MODIFY_PRICE_STATE = "Modify price";
    private static final String CLOSE_AUCTION_STATE = "Close auction";
    private static final String SELECT_WINNER_STATE = "Select winner";

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ArtistManagerAgent " + getAID().getName() + " starting up.");

        /**
         * Create behaviours and set datastores
         */
        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        FindBidders findBidders = new FindBidders();
        findBidders.setDataStore(fsmBehaviour.getDataStore());
        DecideAuctionDetails decideAuctionDetails = new DecideAuctionDetails();
        decideAuctionDetails.setDataStore(fsmBehaviour.getDataStore());
        OpenAuction openAuction = new OpenAuction(this, new ACLMessage(ACLMessage.PROPOSE), fsmBehaviour.getDataStore());
        openAuction.setDataStore(fsmBehaviour.getDataStore());
        AuctionRound auctionRound = new AuctionRound(this, new ACLMessage(ACLMessage.CFP), fsmBehaviour.getDataStore());
        ModifyPrice modifyPrice = new ModifyPrice();
        modifyPrice.setDataStore(fsmBehaviour.getDataStore());
        SelectWinner selectWinner = new SelectWinner();
        selectWinner.setDataStore(fsmBehaviour.getDataStore());
        CloseAuction closeAuction = new CloseAuction();
        closeAuction.setDataStore(fsmBehaviour.getDataStore());

        /**
         * Register states of the FSM
         */
        fsmBehaviour.registerFirstState(decideAuctionDetails, DECIDE_AUCTION_DETAILS_STATE);
        fsmBehaviour.registerState(findBidders, FIND_BIDDERS_STATE);
        fsmBehaviour.registerState(openAuction, OPEN_AUCTION_STATE);
        fsmBehaviour.registerState(auctionRound, AUCTION_ROUND_STATE);
        fsmBehaviour.registerState(modifyPrice, MODIFY_PRICE_STATE);
        fsmBehaviour.registerState(closeAuction, CLOSE_AUCTION_STATE);
        fsmBehaviour.registerState(selectWinner, SELECT_WINNER_STATE);

        /**
         * Register transitions between states
         */
        fsmBehaviour.registerDefaultTransition(DECIDE_AUCTION_DETAILS_STATE, FIND_BIDDERS_STATE);
        fsmBehaviour.registerDefaultTransition(FIND_BIDDERS_STATE, OPEN_AUCTION_STATE, new String[] {OPEN_AUCTION_STATE});
        fsmBehaviour.registerDefaultTransition(OPEN_AUCTION_STATE, AUCTION_ROUND_STATE, new String[] {AUCTION_ROUND_STATE});
        fsmBehaviour.registerTransition(AUCTION_ROUND_STATE, MODIFY_PRICE_STATE, 0);
        fsmBehaviour.registerTransition(AUCTION_ROUND_STATE, SELECT_WINNER_STATE, 1);
        fsmBehaviour.registerDefaultTransition(SELECT_WINNER_STATE, CLOSE_AUCTION_STATE);
        fsmBehaviour.registerTransition(MODIFY_PRICE_STATE, AUCTION_ROUND_STATE, 0, new String[] {AUCTION_ROUND_STATE});
        fsmBehaviour.registerTransition(MODIFY_PRICE_STATE, CLOSE_AUCTION_STATE, 1);
        fsmBehaviour.registerDefaultTransition(CLOSE_AUCTION_STATE, DECIDE_AUCTION_DETAILS_STATE);

        /**
         * Add fsmBehaviour to the agent
         */
        addBehaviour(fsmBehaviour);

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
}
