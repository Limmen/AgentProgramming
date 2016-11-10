package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class CuratorSearcher extends TickerBehaviour {

    public static String CURATORS = "Curators";
    public CuratorSearcher(Agent agent, int timeout) {
        super(agent, timeout);
    }

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
