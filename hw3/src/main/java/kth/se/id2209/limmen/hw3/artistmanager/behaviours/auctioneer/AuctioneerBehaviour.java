package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer;

import jade.core.behaviours.FSMBehaviour;
import kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours.*;


/**
 * Behaviour of an auctioneer in a dutch auction.
 *
 * @author Kim Hammar on 2016-11-19.
 */
public class AuctioneerBehaviour extends FSMBehaviour {
    private static final String FIND_BIDDERS_STATE = "Find Bidders";
    private static final String DECIDE_AUCTION_DETAILS_STATE = "Decide auction details";
    private static final String OPEN_AUCTION_STATE = "Open auction";
    private static final String SEND_CFP_STATE = "Send CFP";
    private static final String COLLECT_BIDS_STATE = "Collect bids";
    private static final String MODIFY_PRICE_STATE = "Modify price";
    private static final String CLOSE_AUCTION_STATE = "Close auction";
    private static final String SELECT_WINNER_STATE = "Select winner";

    /**
     * Class constructor that initializes the behaviour by setting up the different states and transitions.
     */
    public AuctioneerBehaviour() {
        /**
         * Create behaviours and set datastores
         */
        FindBidders findBidders = new FindBidders();
        findBidders.setDataStore(getDataStore());
        OpenAuction openAuction = new OpenAuction();
        openAuction.setDataStore(getDataStore());
        openAuction.setDataStore(getDataStore());
        ModifyPrice modifyPrice = new ModifyPrice();
        modifyPrice.setDataStore(getDataStore());
        SelectWinner selectWinner = new SelectWinner();
        selectWinner.setDataStore(getDataStore());
        CloseAuction closeAuction = new CloseAuction();
        closeAuction.setDataStore(getDataStore());
        CollectBids collectBids = new CollectBids(myAgent, 4000);
        collectBids.setDataStore(getDataStore());
        SendCFP sendCFP = new SendCFP();
        sendCFP.setDataStore(getDataStore());
        /**
         * Register states of the FSM
         */
        registerFirstState(findBidders, FIND_BIDDERS_STATE);
        registerState(openAuction, OPEN_AUCTION_STATE);
        registerState(sendCFP, SEND_CFP_STATE);
        registerState(collectBids, COLLECT_BIDS_STATE);
        registerState(modifyPrice, MODIFY_PRICE_STATE);
        registerLastState(closeAuction, CLOSE_AUCTION_STATE);
        registerState(selectWinner, SELECT_WINNER_STATE);
        /**
         * Register transitions between states
         */
        registerDefaultTransition(DECIDE_AUCTION_DETAILS_STATE, FIND_BIDDERS_STATE);
        registerDefaultTransition(FIND_BIDDERS_STATE, OPEN_AUCTION_STATE);
        registerDefaultTransition(OPEN_AUCTION_STATE, SEND_CFP_STATE);
        registerDefaultTransition(SEND_CFP_STATE, COLLECT_BIDS_STATE, new String[] {COLLECT_BIDS_STATE});
        registerTransition(COLLECT_BIDS_STATE, MODIFY_PRICE_STATE, 1);
        registerTransition(COLLECT_BIDS_STATE, SELECT_WINNER_STATE, 0);
        registerDefaultTransition(SELECT_WINNER_STATE, CLOSE_AUCTION_STATE);
        registerTransition(MODIFY_PRICE_STATE, SEND_CFP_STATE, 0);
        registerTransition(MODIFY_PRICE_STATE, CLOSE_AUCTION_STATE, 1);
    }
}
