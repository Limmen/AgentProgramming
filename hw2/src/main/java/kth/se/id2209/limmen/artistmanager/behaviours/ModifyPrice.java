package kth.se.id2209.limmen.artistmanager.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.Auction;

/**
 * Behaviour for modifying the price for the next round of the auction, according to the strategy of the auctioneer.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class ModifyPrice extends OneShotBehaviour {
    private boolean reachedBelowReservedPrice = false;
    @Override
    public void action() {
        Auction auction = ((Auction) getDataStore().get(ArtistManagerAgent.AUCTION));
        double modifiedPrice = auction.getCurrentPrice() * (1 - auction.getRateOfReduction());
        if(modifiedPrice < auction.getReservePrice()){
            reachedBelowReservedPrice = true;
            System.out.println("Reached reserved price");
        } else{
            auction.setCurrentPrice(modifiedPrice);
            getDataStore().put(ArtistManagerAgent.AUCTION, auction);
            System.out.println("No bids received, modifying price...");
            System.out.println("Price modified, new price: " + auction.getCurrentPrice());
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
