package kth.se.id2209.limmen.tourguide;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.tourguide.behaviours.*;

/**
 * TourGuideAgent. Agent that publishes a service where it can provide other agents with virtual tours of
 * art artifacts.
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
        registerAtYellowPages();

        /**
         * Create behaviours
         */
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        //CuratorSubscriber curatorSubscriber = new CuratorSubscriber(this, 100);
        //curatorSubscriber.setDataStore(parallelBehaviour.getDataStore());
        CuratorSubscriber curatorSubscriber = new CuratorSubscriber(this, CuratorSubscriber.createSubscriptionMessage(this), parallelBehaviour.getDataStore());

        FindSupportedInterests findSupportedInterests = new FindSupportedInterests(this, new ACLMessage(ACLMessage.REQUEST), parallelBehaviour.getDataStore());
        ProfilerMatcher profilerMatcher = new ProfilerMatcher(this, MessageTemplate.MatchOntology("Ontology(Class(TourGuideMatcher partial ProposeInitiator))"), parallelBehaviour.getDataStore());
        profilerMatcher.registerPrepareResponse(findSupportedInterests); //register FindSupportedInterest as RE of ProfilerMatcher
        BuildVirtualTour buildVirtualTour = new BuildVirtualTour(this, new ACLMessage(ACLMessage.REQUEST), parallelBehaviour.getDataStore());
        VirtualTourServer virtualTourServer = new VirtualTourServer(this, MessageTemplate.MatchOntology("Ontology(Class(FindVirtualTour partial AchieveREInitiator))"), parallelBehaviour.getDataStore());
        virtualTourServer.setDataStore(parallelBehaviour.getDataStore());
        virtualTourServer.registerPrepareResultNotification(buildVirtualTour); //register BuildVirtualTour as RE of VirtualTourServer

        /**
         * Add three sub-behaviours to be executed in parallel (1. search for curators, 2. respond to tour-proposals 3. respond to tour requests)
         */
        parallelBehaviour.addSubBehaviour(curatorSubscriber);
        parallelBehaviour.addSubBehaviour(profilerMatcher);
        parallelBehaviour.addSubBehaviour(virtualTourServer);

        /**
         * Add behaviour
         */
        addBehaviour(parallelBehaviour);
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

    /**
     * Register the service as a tour guide at the "yellow pages" aka the Directory Facilitator (DF)
     * that runs on the platform.
     */
    public void registerAtYellowPages() {
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
    }

}
