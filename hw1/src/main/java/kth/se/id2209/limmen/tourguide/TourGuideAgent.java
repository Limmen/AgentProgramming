package kth.se.id2209.limmen.tourguide;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import kth.se.id2209.limmen.tourguide.behaviours.VirtualTourServer;

/**
 * TourGuideAgent
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class TourGuideAgent extends Agent {

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("TourGuideAgent " + getAID().getName() + " starting up.");

        // Register the gallery-curator service at the DF (Yellow pages)
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("virtualtour");
        serviceDescription.setName("Virtual-Tour");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        //Add the behaviour for receiving requests for virtual tours from profilers
        addBehaviour(new VirtualTourServer());

        //doDelete(); //method from the jade.core.Agent that terminates the agent
    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination.
     * DeRegisters from the DF registry.
     */
    @Override
    public void takeDown() {
        System.out.println("TourGuideAgent " + getAID().getName() + " terminating.");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
