package kth.se.id2209.limmen.artistmanager.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;

/**
 * Behaviour for searching at the DF for registered art-bidders.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class FindBidders extends OneShotBehaviour {

    /**
     * Action of the behaviour.
     * Searches the yellow pages (aka DF) for registered art-bidders and updates the datastore.
     */
    @Override
    public void action() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription bidderDescription = new ServiceDescription();
        bidderDescription.setType("art-bidder");
        dfAgentDescription.addServices(bidderDescription);
        DFAgentDescription[] bidders = new DFAgentDescription[0];
        try {
            bidders = DFService.search(myAgent, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        getDataStore().put(ArtistManagerAgent.BIDDERS, bidders);
    }
}
