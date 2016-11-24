package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;


/**
 * Behaviour for modifying the price for the next round of the auction, according to the strategy of the auctioneer.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class ModifyPrice extends OneShotBehaviour {
    private boolean reachedBelowReservedPrice = false;
    @Override
    public void action() {
        Auction auction = ((ArtistManagerAgent) myAgent).getAuction();
        double modifiedPrice = auction.getCurrentPrice() * (1 - auction.getRateOfReduction());
        if(modifiedPrice < auction.getReservePrice()){
            reachedBelowReservedPrice = true;
            ((ArtistManagerAgent) myAgent).updateLog("Reached reserved price, your reserved price: " + auction.getReservePrice());
            ((ArtistManagerAgent) myAgent).updateLog("AUCTION CLOSED");
        } else{
            auction.setCurrentPrice(modifiedPrice);
            ((ArtistManagerAgent) myAgent).setAuction(auction);
            ((ArtistManagerAgent) myAgent).updateLog("No bids received, modifying price...");
            ((ArtistManagerAgent) myAgent).updateLog("Price modified, new price: " + auction.getCurrentPrice());
            ((ArtistManagerAgent) myAgent).updateLog("Your reserved price is: " + auction.getReservePrice());
            reachedBelowReservedPrice = false;
        }
    }

    /**
     * Called just before termination of this behaviour.
     *
     * @return 1 if the reserved price is reached, otherwise 0
     */
    @Override
    public int onEnd() {
        if (reachedBelowReservedPrice)
            return 1;
        else
            return 0;
    }
}
