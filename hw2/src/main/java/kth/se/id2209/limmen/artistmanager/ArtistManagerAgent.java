package kth.se.id2209.limmen.artistmanager;

import jade.core.Agent;
import kth.se.id2209.limmen.artgallery.ArtGallery;
import kth.se.id2209.limmen.artistmanager.behaviours.auctioneer.AuctioneerBehaviour;

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

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ArtistManagerAgent " + getAID().getName() + " starting up.");

        /**
         * Add ActioneerBehaviour to the agent
         */
        addBehaviour(new AuctioneerBehaviour());

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
