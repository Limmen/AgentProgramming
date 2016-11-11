package kth.se.id2209.limmen.profiler;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.profiler.behaviours.*;

/**
 * Agent that maintains a profile of a user. The agent travels around the network and looks for
 * interesting information (according to user profile) about art from online art galleries.
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class ProfilerAgent extends Agent {
    private static final String FIND_MATCHING_TOUR_GUIDE_STATE = "Find matching tour guide";
    private static final String VIRTUAL_TOUR_STATE = "Virtual tour";
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
         * Create behaviours
         */
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        FSMBehaviour fsmBehaviour = new FSMBehaviour(this);
        fsmBehaviour.setDataStore(parallelBehaviour.getDataStore());
        TourGuideMatcher tourGuideMatcher = new TourGuideMatcher(this, new ACLMessage(ACLMessage.PROPOSE), fsmBehaviour.getDataStore());
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        sequentialBehaviour.setDataStore(fsmBehaviour.getDataStore());
        FindVirtualTour findVirtualTour = new FindVirtualTour(this, new ACLMessage(ACLMessage.REQUEST), fsmBehaviour.getDataStore());
        RetrieveArtefactDetails retrieveArtefactDetails = new RetrieveArtefactDetails(this, new ACLMessage(ACLMessage.REQUEST), fsmBehaviour.getDataStore());
        ServicesSearcher servicesSearcher = new ServicesSearcher(this, 1000);
        servicesSearcher.setDataStore(parallelBehaviour.getDataStore());

        /**
         * Construct sequential behaviour of FindVirtualTour and RetrieveArtefactDetails
         */
        sequentialBehaviour.addSubBehaviour(findVirtualTour);
        sequentialBehaviour.addSubBehaviour(retrieveArtefactDetails);

        /**
         * Construct fsmBehaviour of TourGuideMatcher behaviour and the sequential behaviour
         */
        fsmBehaviour.registerFirstState(tourGuideMatcher, FIND_MATCHING_TOUR_GUIDE_STATE);
        fsmBehaviour.registerLastState(sequentialBehaviour, VIRTUAL_TOUR_STATE);
        fsmBehaviour.registerTransition(FIND_MATCHING_TOUR_GUIDE_STATE, FIND_MATCHING_TOUR_GUIDE_STATE, 0);
        fsmBehaviour.registerTransition(FIND_MATCHING_TOUR_GUIDE_STATE, VIRTUAL_TOUR_STATE, 1);

        /**
         * Construct parallelBehaviour of ServicesSearcher and the fsmbehaviour
         */
        parallelBehaviour.addSubBehaviour(servicesSearcher);
        parallelBehaviour.addSubBehaviour(fsmBehaviour);

        /**
         * Add parallelbehaviour to the agent
         */
        addBehaviour(parallelBehaviour);

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
