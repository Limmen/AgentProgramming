package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * TickerBehaviour that will periodically poll the yellow pages for all of the registered art-curators
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class CuratorSearcher extends TickerBehaviour {
    public static String CURATORS = "Curators";

    /**
     * Class constructor initializing the behaviour
     *
     * @param agent agent running the behaviour
     * @param timeout timeout value
     */
    public CuratorSearcher(Agent agent, int timeout) {
        super(agent, timeout);
    }

    /**
     * Called periodically every timeout. Polls the Yellow pages (aka DF) for a list of art-curators
     */
    @Override
    protected void onTick() {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription curatorServiceDescr = new ServiceDescription();
        curatorServiceDescr.setType("artgallery-information");
        dfAgentDescription.addServices(curatorServiceDescr);
        DFAgentDescription[] curators = new DFAgentDescription[0];
        try {
            curators = DFService.search(myAgent, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        getDataStore().put(CURATORS, curators);
    }
}
