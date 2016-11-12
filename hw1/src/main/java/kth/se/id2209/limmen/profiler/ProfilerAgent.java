package kth.se.id2209.limmen.profiler;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.profiler.behaviours.*;

/**
 * Agent that maintains a profile of a user. The agent travels around the network and looks for
 * interesting information (according to user profile) about art from online art galleries.
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class ProfilerAgent extends Agent {
    private static final String SEARCH_TOURGUIDES_STATE = "Search Tourguides";
    private static final String FIND_MATCHING_TOUR_GUIDES_STATE = "Find matching tourguides";
    private static final String SELECT_TOURGUIDE_STATE = "Select tourguide";
    private static final String FIND_VIRTUAL_TOUR_STATE = "Find Virtual tour";
    private static final String SELECT_ARTIFACT_STATE = "Select artifact";
    private static final String RETRIEVE_ARTIFACT_STATE = "Retrieve artifact";
    private UserProfile userProfile;

    /**
     * Agent initialization. Called by the JADE runtime environment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ProfilerAgent " + getAID().getName() + " starting up.");
        /**
         * Get command-line arguments
         */
        Object[] args = getArguments();
        if (args != null && args.length == 5) {
            String name = (String) args[0];
            String interest = (String) args[1];
            String occupation = (String) args[2];
            int age = Integer.parseInt(((String) args[3]));
            String gender = (String) args[4];
            this.userProfile = new UserProfile.UserProfileBuilder().name(name).
                    interest(interest).occupation(occupation).age(age).gender(gender).build();
        } else {
            //If no command-line arguments use the InitializeUserProfile behaviour to ask the user.
            addBehaviour(new InitializeUserProfile());
        }

        /**
         * Create behaviours and set datastores
         */
        FSMBehaviour fsmBehaviour = new FSMBehaviour(this);
        ServicesSearcher servicesSearcher = new ServicesSearcher();
        servicesSearcher.setDataStore(fsmBehaviour.getDataStore());
        TourGuideMatcher tourGuideMatcher = new TourGuideMatcher(this, new ACLMessage(ACLMessage.QUERY_REF), fsmBehaviour.getDataStore());
        SelectTourGuide selectTourGuide = new SelectTourGuide();
        selectTourGuide.setDataStore(fsmBehaviour.getDataStore());
        FindVirtualTour findVirtualTour = new FindVirtualTour(this, new ACLMessage(ACLMessage.REQUEST), fsmBehaviour.getDataStore());
        SelectArtifact selectArtifact = new SelectArtifact();
        selectArtifact.setDataStore(fsmBehaviour.getDataStore());
        RetrieveArtefactDetails retrieveArtefactDetails = new RetrieveArtefactDetails(this, new ACLMessage(ACLMessage.REQUEST), fsmBehaviour.getDataStore());

        /**
         * Register states of the FSM
         */
        fsmBehaviour.registerFirstState(servicesSearcher, SEARCH_TOURGUIDES_STATE);
        fsmBehaviour.registerState(tourGuideMatcher, FIND_MATCHING_TOUR_GUIDES_STATE);
        fsmBehaviour.registerState(selectTourGuide, SELECT_TOURGUIDE_STATE);
        fsmBehaviour.registerState(findVirtualTour, FIND_VIRTUAL_TOUR_STATE);
        fsmBehaviour.registerState(selectArtifact, SELECT_ARTIFACT_STATE);
        fsmBehaviour.registerState(retrieveArtefactDetails, RETRIEVE_ARTIFACT_STATE);

        /**
         * Register transitions between states
         */
        fsmBehaviour.registerDefaultTransition(SEARCH_TOURGUIDES_STATE, FIND_MATCHING_TOUR_GUIDES_STATE, new String[] {FIND_MATCHING_TOUR_GUIDES_STATE});
        fsmBehaviour.registerDefaultTransition(FIND_MATCHING_TOUR_GUIDES_STATE, SELECT_TOURGUIDE_STATE);
        fsmBehaviour.registerTransition(SELECT_TOURGUIDE_STATE, SEARCH_TOURGUIDES_STATE, 0);
        fsmBehaviour.registerTransition(SELECT_TOURGUIDE_STATE, FIND_VIRTUAL_TOUR_STATE, 1, new String[] {FIND_VIRTUAL_TOUR_STATE});
        fsmBehaviour.registerTransition(FIND_VIRTUAL_TOUR_STATE, SELECT_ARTIFACT_STATE, 1);
        fsmBehaviour.registerTransition(FIND_VIRTUAL_TOUR_STATE, FIND_MATCHING_TOUR_GUIDES_STATE, 0, new String[] {FIND_MATCHING_TOUR_GUIDES_STATE});
        fsmBehaviour.registerTransition(SELECT_ARTIFACT_STATE, SEARCH_TOURGUIDES_STATE, 0);
        fsmBehaviour.registerTransition(SELECT_ARTIFACT_STATE, RETRIEVE_ARTIFACT_STATE, 1, new String[] {RETRIEVE_ARTIFACT_STATE});
        fsmBehaviour.registerDefaultTransition(RETRIEVE_ARTIFACT_STATE, SELECT_ARTIFACT_STATE);

        /**
         * Add fsmBehaviour to the agent
         */
        addBehaviour(fsmBehaviour);

    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination
     */
    @Override
    public void takeDown() {
        System.out.println("ProfilerAgent " + getAID().getName() + " terminating.");
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
