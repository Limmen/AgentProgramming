package kth.se.id2209.limmen.profiler;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.profiler.behaviours.FindVirtualTour;
import kth.se.id2209.limmen.profiler.behaviours.RetrieveArtefactDetails;
import kth.se.id2209.limmen.profiler.behaviours.ServicesSearcher;
import kth.se.id2209.limmen.profiler.behaviours.TourGuideMatcher;

/**
 * @author Kim Hammar on 2016-11-08.
 */
public class ProfilerAgent extends Agent {
    private static final String FIND_MATCHING_TOUR_GUIDE_STATE = "Find matching tour guide";
    private static final String REQUEST_VIRTUAL_TOUR = "Request virtual tour";
    private static final String RECEIVE_VIRTUAL_TOUR = "Receive virtual tour";
    private static final String RETRIEVE_ARTEFACT_DETAILS = "Retrieve artefact details";
    private static final String FIND_TOUR = "Find tour";
    private static final String VIRTUAL_TOUR_STATE = "Virtual tour";

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ProfilerAgent " + getAID().getName() + " starting up.");

        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        FSMBehaviour fsmBehaviour = new FSMBehaviour(this);
        fsmBehaviour.setDataStore(parallelBehaviour.getDataStore());

        TourGuideMatcher tourGuideMatcher = new TourGuideMatcher(this, new ACLMessage(ACLMessage.PROPOSE), fsmBehaviour.getDataStore());
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.setDataStore(fsmBehaviour.getDataStore());
        FindVirtualTour findVirtualTour = new FindVirtualTour(this, new ACLMessage(ACLMessage.REQUEST), fsmBehaviour.getDataStore());
        RetrieveArtefactDetails retrieveArtefactDetails = new RetrieveArtefactDetails(this, new ACLMessage(ACLMessage.REQUEST), fsmBehaviour.getDataStore());
        sequentialBehaviour.addSubBehaviour(findVirtualTour);
        sequentialBehaviour.addSubBehaviour(retrieveArtefactDetails);

        fsmBehaviour.registerFirstState(tourGuideMatcher, FIND_MATCHING_TOUR_GUIDE_STATE);
        fsmBehaviour.registerLastState(sequentialBehaviour, VIRTUAL_TOUR_STATE);
        fsmBehaviour.registerTransition(FIND_MATCHING_TOUR_GUIDE_STATE, FIND_MATCHING_TOUR_GUIDE_STATE, 0);
        fsmBehaviour.registerTransition(FIND_MATCHING_TOUR_GUIDE_STATE, VIRTUAL_TOUR_STATE, 1);

        ServicesSearcher servicesSearcher = new ServicesSearcher(this, 1000);
        servicesSearcher.setDataStore(parallelBehaviour.getDataStore());
        parallelBehaviour.addSubBehaviour(servicesSearcher);
        parallelBehaviour.addSubBehaviour(fsmBehaviour);
        addBehaviour(parallelBehaviour);

    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination
     */
    @Override
    public void takeDown() {
        System.out.println("ProfilerAgent " + getAID().getName() + " terminating.");
    }

}
