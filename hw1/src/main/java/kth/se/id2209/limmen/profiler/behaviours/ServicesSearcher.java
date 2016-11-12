package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Behaviour that searches the yellow pages (aka DF) for registered tourguides and updates the datastore.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class ServicesSearcher extends OneShotBehaviour {
    public static String TOUR_GUIDES = "Tour guides";

    /**
     * Action of the behaviour.
     * Searches the yellow pages (aka DF) for registered tourguides and updates the datastore.
     */
    @Override
    public void action() {
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
