package kth.se.id2209.limmen.hw3.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;

/**
 * @author Kim Hammar on 2016-11-29.
 */
public class BiddingClosed extends OneShotBehaviour {

    @Override
    public void action() {
        ((ArtistManagerAgent) myAgent).biddingClosed();
    }
}
