package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import kth.se.id2209.limmen.artgallery.Artifact;
import kth.se.id2209.limmen.profiler.ProfilerAgent;
import kth.se.id2209.limmen.tourguide.TourItem;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * Behaviour that interacts with the user after a virtualtour have been received by a tourguide.
 * The behaviour lets the user 1. view his profile 2. view the tour 3. visit artifacts in the tour
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class RetrieveArtefactDetails extends AchieveREInitiator {

    /**
     * Class consructor that initializes the behaviour
     *
     * @param a agent running the behaviour
     * @param msg message to send to art-curators for information about artifacts
     * @param store datastore to communicate with other behaviours
     */
    public RetrieveArtefactDetails(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }

    /**
     * This method must return the vector of ACLMessage objects to be sent.
     * It is called in the first state of this protocol.
     * This method will collect an action from the user and perform it. If the action is to visit an artifact then a
     * request will be sent to the curator of that artifact and the details is later received (hopefully)
     *
     * @param request request that was passed in the constructor
     * @return Vector of requests to send
     */
    protected Vector prepareRequests(ACLMessage request) {
        Scanner scanner = new Scanner(System.in);
        try {
            loop:
            while (true) {
                System.out.println("Press '1' to view user profile and visited artifacts, press '2' to view the tour, press '3' to visit an artifact in the tour");
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        System.out.println(((ProfilerAgent) myAgent).getUserProfile().toString());
                        break;
                    case 2:
                        printTour();
                        break;
                    case 3:
                        System.out.println("Enter the name of the artifact");
                        String title = scanner.nextLine();
                        System.out.println("Visiting artifact with name " + title);
                        request = new ACLMessage(ACLMessage.REQUEST);
                        try {
                            AID curator = findCurator(title);
                            request.addReceiver(curator);
                            request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                            request.setOntology("Profiler-Request-Art-Information-Ontology");
                            request.setContent(title);
                            Vector<ACLMessage> messages = new Vector();
                            messages.add(request);
                            return messages;
                        } catch (Exception e) {
                            System.out.println("That title is not in the tour");
                            break;
                        }
                    default:
                        System.out.println("Invalid input");
                        break;

                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            return new Vector();
        }

    }

    /**
     * This method is called every time a inform message is received,
     * which is not out-of-sequence according to the protocol rules.
     * This message should contain detailed information about an artifact.
     *
     * @param inform message received
     */
    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            Artifact artifact = (Artifact) inform.getContentObject();
            ((ProfilerAgent) myAgent).getUserProfile().addVisitedArtifact(artifact);
            System.out.println();
            System.out.println("Retrieved information: ");
            System.out.println(artifact.toString());
            System.out.println();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called every time a refuse message is received,
     * which is not out-of-sequence according to the protocol rules.
     * The curators will refuse the request if they cant find the artifact of the request.
     *
     * @param refuse message received
     */
    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Could not retrieve the necessary information at the registered curator");
        System.out.println("Reason: " + refuse.getContent());
    }

    /**
     * Called just before termination of this behaviour.
     *
     * @return
     */
    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

    /**
     * Method to pretty-print the virtual tour
     */
    private void printTour() {
        ArrayList<TourItem> virtualTour = (ArrayList<TourItem>) getDataStore().get(FindVirtualTour.VIRTUAL_TOUR);
        AID tourGuide = (AID) getDataStore().get(TourGuideMatcher.TOUR_GUIDE);
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("The virtual tour created by " + tourGuide.getName() + " according to your interest consists of the following items:");
        for (TourItem tourItem : virtualTour) {
            System.out.println(tourItem.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    /**
     * Method to find the curator of a given artifact in the tour
     *
     * @param name name of the artifact
     * @return AID of the curator
     * @throws Exception if the curator cannot be found
     */
    private AID findCurator(String name) throws Exception{
        ArrayList<TourItem> virtualTour = (ArrayList<TourItem>) getDataStore().get(FindVirtualTour.VIRTUAL_TOUR);
        for(TourItem tourItem : virtualTour){
            if(tourItem.getTitle().equalsIgnoreCase(name)){
                return tourItem.getCurator();
            }
        }
        throw new Exception("Name not in tour");
    }

}
