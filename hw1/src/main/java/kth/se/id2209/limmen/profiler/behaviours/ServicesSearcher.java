package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import kth.se.id2209.limmen.profiler.ProfilerAgent;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class ServicesSearcher extends TickerBehaviour {
    public ServicesSearcher(Agent agent, int timeout) {
        super(agent, timeout);
    }

    @Override
    protected void onTick() {
        //Template used both for registering and searching in the DF centralized registry of services
        //The template should not include an AID when used for searching
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
        dfAgentDescription = new DFAgentDescription();
        ServiceDescription curatorServiceDescr = new ServiceDescription();
        curatorServiceDescr.setType("artgallery-information");
        dfAgentDescription.addServices(curatorServiceDescr);
        DFAgentDescription[] curators = new DFAgentDescription[0];
        try {
            curators = DFService.search(myAgent, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        ProfilerAgent profilerAgent = (ProfilerAgent) myAgent;
        profilerAgent.setTourGuides(tourGuides);
        profilerAgent.setGalleryCurators(curators);
       // System.out.println("Updating tourguides: " + tourGuides.length);
        //System.out.println("Updating curators: " + curators.length);
    }
}
