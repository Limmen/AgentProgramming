package kth.se.id2209.limmen.curator.behaviours.bidder;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.proto.states.MsgReceiver;
import kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours.AuctionRoundServer;
import kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours.AuctionServer;
import kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours.ReceiveBidResult;

/**
 * @author Kim Hammar on 2016-11-19.
 */
public class BidderBehaviour extends ParallelBehaviour {
    public static String AUCTION_RESULT_MSG = "Auction Result Message";
    public BidderBehaviour(DataStore dataStore) {
        setDataStore(dataStore);
        /**
         * Create behaviours and set datastores
         */
        AuctionServer auctionServer = new AuctionServer();
        auctionServer.setDataStore(getDataStore());
        AuctionRoundServer auctionRoundServer = new AuctionRoundServer();
        auctionRoundServer.setDataStore(getDataStore());
        ReceiveBidResult receiveBidResult = new ReceiveBidResult(myAgent, MessageTemplate.MatchOntology("Ontology(Class(SelectWinner partial OneShotBehaviour))"), MsgReceiver.INFINITE, getDataStore(), AUCTION_RESULT_MSG);

        /**
         * Add subbehaviours
         */
        addSubBehaviour(auctionServer);
        addSubBehaviour(auctionRoundServer);
        addSubBehaviour(receiveBidResult);
    }
}
