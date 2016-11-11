package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Behaviour that periodically polls the yellow pages (aka DF) and updates the list of tourguides
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class ServicesSearcher extends TickerBehaviour {
    public static String TOUR_GUIDES = "Tour guides";

    /**
     * Class constructor initializing the behaviour
     *
     * @param agent agent running the behaviour
     * @param timeout timeout value
     */
    public ServicesSearcher(Agent agent, int timeout) {
        super(agent, timeout);
    }

    /**
     * Method called periodically every timeout, sends a request to the DF and updates the list of tourguides
     */
    @Override
    protected void onTick() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription virtualTourServiceDescr = new ServiceDescription();
        virtualTourServiceDescr.setType("virtualtour");
        dfAgentDescription.addServices(virtualTourServiceDescr);
        DFAgentDescription[] tourGuides = new DFAgentDescription[0];
        try {
            tourGuides = DFService.search(myAgent, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        getDataStore().put(TOUR_GUIDES, tourGuides);
    }
}
