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
 * @author Kim Hammar on 2016-11-09.
 */
public class RetrieveArtefactDetails extends AchieveREInitiator {

    public RetrieveArtefactDetails(Agent a, ACLMessage msg, DataStore store) {
        super(a, msg, store);
    }


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
                            //((DFAgentDescription[]) getDataStore().get(ServicesSearcher.CURATORS))[0].getName();
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

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println("BuildVirtualTour received agree message");
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Could not retrieve the necessary information at the registered curator");
        System.out.println("Reason: " + refuse.getContent());
    }

    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

    protected void printTour() {
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

    private AID findCurator(String name) throws Exception{
        ArrayList<TourItem> virtualTour = (ArrayList<TourItem>) getDataStore().get(FindVirtualTour.VIRTUAL_TOUR);
        for(TourItem tourItem : virtualTour){
            if(tourItem.getTitle().equals(name)){
                return tourItem.getCurator();
            }
        }
        throw new Exception("Name not in tour");
    }

}
