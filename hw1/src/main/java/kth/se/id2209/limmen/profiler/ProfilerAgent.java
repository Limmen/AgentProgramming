package kth.se.id2209.limmen.profiler;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.profiler.behaviours.*;

/**
 *
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class ProfilerAgent extends Agent {
    private static final String FIND_MATCHING_TOUR_GUIDE_STATE = "Find matching tour guide";
    private static final String VIRTUAL_TOUR_STATE = "Virtual tour";

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    private UserProfile userProfile;

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ProfilerAgent " + getAID().getName() + " starting up.");
        // Get the title of the book to buy as a start-up argument
        Object[] args = getArguments();
        if (args != null && args.length == 5) {
            String name = (String) args[0];
            String interest = (String) args[1];
            String occupation = (String) args[2];
            int age = Integer.parseInt(((String) args[3]));
            String gender = (String) args[4];
            this.userProfile = new UserProfile.UserProfileBuilder().name(name).
                    interest(interest).occupation(occupation).age(age).gender(gender).build();
        } else{
            addBehaviour(new InitializeUserProfile());
        }

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

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
