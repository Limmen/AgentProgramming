package kth.se.id2209.limmen.curator;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import kth.se.id2209.limmen.artgallery.ArtGallery;
import kth.se.id2209.limmen.artgallery.Artefact;
import kth.se.id2209.limmen.curator.behaviours.ProfilerRequestServer;
import kth.se.id2209.limmen.curator.behaviours.TourGuideRequestServer;

import java.util.ArrayList;

/**
 * @author Kim Hammar on 2016-11-08.
 */
public class CuratorAgent extends Agent {

    private ArtGallery artGallery = new ArtGallery();
    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("CuratorAgent " + getAID().getName() + " starting up.");

        registerAtYellowPages();

        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();

        //Add the behaviour for receiving requests from profilers
        parallelBehaviour.addSubBehaviour(new ProfilerRequestServer());

        //Add the behaviour for receiving requests from tourguides
        parallelBehaviour.addSubBehaviour(new TourGuideRequestServer());

        //Add the two cyclic server behaviours running in parallel
        addBehaviour(parallelBehaviour);

    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination.
     * DeRegisters from the DF registry.
     */
    @Override
    public void takeDown() {
        System.out.println("CuratorAgent " + getAID().getName() + " terminating.");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Artefact> getArtGallery() {
        return artGallery.getGallery();
    }

    private void registerAtYellowPages(){
        // Register the gallery-curator service at the DF (Yellow pages)
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("artgallery-information");
        serviceDescription.setName("Art-Gallery-Information");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
