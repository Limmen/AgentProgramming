package kth.se.id2209.limmen.profiler;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.artgallery.Artefact;
import kth.se.id2209.limmen.profiler.behaviours.*;

import java.util.ArrayList;

/**
 * @author Kim Hammar on 2016-11-08.
 */
public class ProfilerAgent extends Agent {

    private DFAgentDescription[] tourGuides = new DFAgentDescription[0];
    private DFAgentDescription[] galleryCurators = new DFAgentDescription[0];
    private AID tourGuide;
    private static final String FIND_MATCHING_TOUR_GUIDE = "Find matching tour guide";
    private static final String REQUEST_VIRTUAL_TOUR = "Request virtual tour";
    private static final String RECEIVE_VIRTUAL_TOUR = "Receive virtual tour";
    private static final String RETRIEVE_ARTEFACT_DETAILS = "Retrieve artefact details";
    private ArrayList<String> tourTitles = new ArrayList<String>();
    private ArrayList<Artefact> detailtedTour = new ArrayList<Artefact>();

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ProfilerAgent " + getAID().getName() + " starting up.");
        FSMBehaviour fsmBehaviour = new FSMBehaviour(this);
        fsmBehaviour.registerFirstState(new TourGuideMatcher(this, new ACLMessage(ACLMessage.PROPOSE), tourGuides), FIND_MATCHING_TOUR_GUIDE);
        fsmBehaviour.registerState(new RequestVirtualTour(), REQUEST_VIRTUAL_TOUR);
        fsmBehaviour.registerState(new ReceiveVirtualTour(), RECEIVE_VIRTUAL_TOUR);
        fsmBehaviour.registerState(new RetrieveArtefactDetails(this, new ACLMessage(ACLMessage.REQUEST)), RETRIEVE_ARTEFACT_DETAILS);
        fsmBehaviour.registerTransition(FIND_MATCHING_TOUR_GUIDE, FIND_MATCHING_TOUR_GUIDE, 0);
        fsmBehaviour.registerTransition(FIND_MATCHING_TOUR_GUIDE, REQUEST_VIRTUAL_TOUR, 1);
        fsmBehaviour.registerDefaultTransition(REQUEST_VIRTUAL_TOUR, RECEIVE_VIRTUAL_TOUR);
        fsmBehaviour.registerTransition(RECEIVE_VIRTUAL_TOUR, RECEIVE_VIRTUAL_TOUR, 0);
        fsmBehaviour.registerTransition(RECEIVE_VIRTUAL_TOUR, RETRIEVE_ARTEFACT_DETAILS, 1);
        ParallelBehaviour par = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        par.addSubBehaviour(new ServicesSearcher(this, 1000));
        par.addSubBehaviour(fsmBehaviour);
        addBehaviour(par);

    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination
     */
    @Override
    public void takeDown() {
        System.out.println("ProfilerAgent " + getAID().getName() + " terminating.");
    }

    public DFAgentDescription[] getTourGuides() {
        return tourGuides;
    }

    public void setTourGuides(DFAgentDescription[] tourGuides) {
        this.tourGuides = tourGuides;
    }

    public DFAgentDescription[] getGalleryCurators() {
        return galleryCurators;
    }

    public void setGalleryCurators(DFAgentDescription[] galleryCurators) {
        this.galleryCurators = galleryCurators;
    }

    public void setTourGuide(AID tourGuide) {
        this.tourGuide = tourGuide;
    }

    public AID getTourGuide() {
        return tourGuide;
    }

    public ArrayList<String> getTourTitles() {
        return tourTitles;
    }

    public void setTourTitles(ArrayList<String> tourTitles) {
        this.tourTitles = tourTitles;
    }

    public ArrayList<Artefact> getDetailtedTour() {
        return detailtedTour;
    }

    public void setDetailtedTour(ArrayList<Artefact> detailtedTour) {
        this.detailtedTour = detailtedTour;
    }
}
