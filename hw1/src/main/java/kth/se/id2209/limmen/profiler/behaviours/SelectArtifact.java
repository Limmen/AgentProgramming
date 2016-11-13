package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.profiler.ProfilerAgent;
import kth.se.id2209.limmen.tourguide.TourItem;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Behaviour for interacting with the user for selecting an artifact from the virtual tour to visit.
 *
 * @author Kim Hammar on 2016-11-12.
 */
public class SelectArtifact extends OneShotBehaviour {
    protected static String ARTIFACT = "Artefact";
    private boolean selectedArtefact = false;

    /**
     * Action of the behaviour.
     * Interacts with the user for selecting an artifact from the virtual tour to visit.
     */
    @Override
    public void action() {
        Scanner scanner = new Scanner(System.in);
        loop:
        while (true) {
            System.out.println("Press '1' to view user profile and visited artifacts | press '2' to view the tour | press '3' to visit an artifact in the tour |" +
                    "press '4' to exit the virtual tour and search for a new one | press '5' to update your interest");
            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        System.out.println("------------------------------------------------------------------------------------------------------------------");
                        System.out.println(((ProfilerAgent) myAgent).getUserProfile().toString());
                        System.out.println("------------------------------------------------------------------------------------------------------------------");
                        break;
                    case 2:
                        printTour();
                        break;
                    case 3:
                        System.out.println("Enter the name of the artifact");
                        String title = scanner.nextLine();
                        System.out.println("------------------------------------------------------------------------------------------------------------------");
                        System.out.println("Visiting artifact with name " + title);
                        getDataStore().put(ARTIFACT, title);
                        selectedArtefact = true;
                        break loop;
                    case 4:
                        selectedArtefact = false;
                        break loop;
                    case 5:
                        System.out.println("Enter your new interest below:");
                        String interest = scanner.nextLine();
                        ((ProfilerAgent) myAgent).getUserProfile().setInterest(interest);
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;

                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");

            }
        }
    }


    /**
     * Method to pretty-print the virtual tour
     */
    private void printTour() {
        ArrayList<TourItem> virtualTour = (ArrayList<TourItem>) getDataStore().get(FindVirtualTour.VIRTUAL_TOUR);
        AID tourGuide = (AID) getDataStore().get(TourGuideMatcher.TOUR_GUIDE);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("The virtual tour created by " + tourGuide.getName() + " according to your interest consists of the following items:");
        for (TourItem tourItem : virtualTour) {
            System.out.println(tourItem.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Called just before termination of this behaviour.
     * return value decides the next state.
     *
     * @return next-state indicator
     */
    @Override
    public int onEnd() {
        if (selectedArtefact) {
            return 1;
        } else {
            return 0;
        }
    }
}
