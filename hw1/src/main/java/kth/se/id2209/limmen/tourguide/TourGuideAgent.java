package kth.se.id2209.limmen.tourguide;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.tourguide.behaviours.BuildVirtualTour;
import kth.se.id2209.limmen.tourguide.behaviours.CuratorSearcher;
import kth.se.id2209.limmen.tourguide.behaviours.ProfilerMatcher;
import kth.se.id2209.limmen.tourguide.behaviours.VirtualTourServer;

/**
 * TourGuideAgent
 *
 * @author Kim Hammar on 2016-11-08.
 */
public class TourGuideAgent extends Agent {

    private DFAgentDescription[] galleryCurators = new DFAgentDescription[0];
    private static final String RECEIVE_REQUEST_STATE = "Receive request state";
    private static final String BUILD_TOUR_STATE = "Build tour state";
    private AID requester;

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("TourGuideAgent " + getAID().getName() + " starting up.");
        registerAtYellowPages();

        //FSMBehaviour fsmBehaviour = new FSMBehaviour(this);
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        parallelBehaviour.addSubBehaviour(new CuratorSearcher(this, 100));
        parallelBehaviour.addSubBehaviour(new ProfilerMatcher(this, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE)));
        FSMBehaviour fsmBehaviour = new FSMBehaviour();
        fsmBehaviour.registerFirstState(new VirtualTourServer(), RECEIVE_REQUEST_STATE);
        fsmBehaviour.registerState(new BuildVirtualTour(this, new ACLMessage(ACLMessage.PROPOSE)), BUILD_TOUR_STATE);
        fsmBehaviour.registerTransition(RECEIVE_REQUEST_STATE, RECEIVE_REQUEST_STATE, 0);
        fsmBehaviour.registerTransition(RECEIVE_REQUEST_STATE, BUILD_TOUR_STATE, 1);
        fsmBehaviour.registerDefaultTransition(BUILD_TOUR_STATE, RECEIVE_REQUEST_STATE);

        parallelBehaviour.addSubBehaviour(fsmBehaviour);
        addBehaviour(parallelBehaviour);




        /*
        fsmBehaviour.registerFirstState(new VirtualTourServer(this, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)), RECEIVE_REQUEST_STATE);
        fsmBehaviour.registerState(new BuildVirtualTour(), BUILD_TOUR_STATE);
        fsmBehaviour.registerTransition(RECEIVE_REQUEST_STATE, RECEIVE_REQUEST_STATE, 0);
        fsmBehaviour.registerTransition(RECEIVE_REQUEST_STATE, BUILD_TOUR_STATE, 1);
        fsmBehaviour.registerDefaultTransition(BUILD_TOUR_STATE, RECEIVE_REQUEST_STATE);
        */

        //Add the FSM behaviour for recieving tourrequests from profilers and building tours accordingly
        //addBehaviour(fsmBehaviour);

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

    public void registerAtYellowPages() {
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
    }

    public DFAgentDescription[] getGalleryCurators() {
        return galleryCurators;
    }

    public void setGalleryCurators(DFAgentDescription[] galleryCurators) {
        this.galleryCurators = galleryCurators;
    }

    public AID getRequester() {
        return requester;
    }

    public void setRequester(AID requester) {
        this.requester = requester;
    }
}
