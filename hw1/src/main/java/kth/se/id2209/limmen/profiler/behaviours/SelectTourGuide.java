package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.profiler.ProfilerAgent;
import kth.se.id2209.limmen.profiler.TourGuide;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Behaviour for interacting with the user and selecting a tourguide from the list of discovered tourguides.
 *
 * @author Kim Hammar on 2016-11-12.
 */
public class SelectTourGuide extends OneShotBehaviour {
    public static String TOUR_GUIDE = "Tour Guide";
    private boolean foundMatchingTourGuide = false;

    /**
     * Action of the behaviour, interacts with the user for selecting a tourguide
     * from the list of discovered tourguides.
     */
    @Override
    public void action() {
        System.out.println("select tourguide");
        ArrayList<TourGuide> foundTourGuides =  (ArrayList<TourGuide>) getDataStore().get(TourGuideMatcher.TOUR_GUIDES);
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Found The following TourGuides");
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("TourGuides found that match your interest '" + ((ProfilerAgent) myAgent).getUserProfile().getInterest() + "'");
            System.out.println();
            int num = 0;
            for (TourGuide tourGuide : foundTourGuides) {
                if (tourGuide.getSupportedInterests().contains(((ProfilerAgent) myAgent).getUserProfile().getInterest())) {
                    System.out.println(num + " - " + tourGuide.toString());
                    num++;
                }
            }
            if (num == 0)
                System.out.println("-");
            int prevNum = num;
            System.out.println("Other tourguides found: ");
            System.out.println();
            for (TourGuide tourGuide : foundTourGuides) {
                if (!tourGuide.getSupportedInterests().contains(((ProfilerAgent) myAgent).getUserProfile().getInterest())) {
                    System.out.println(num + " : " + tourGuide.toString());
                    num++;
                }
            }
            if (num == prevNum)
                System.out.println("-");
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Select the tourguide you want to request a virtual tour by entering the corresponding number or enter '-1' to search again");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= 0 && input <= num) {
                    foundMatchingTourGuide = true;
                    getDataStore().put(TOUR_GUIDE, foundTourGuides.get(input).getTourGuide());
                    break;
                } else if (input == -1) {
                    System.out.println("input = -1");
                    break;
                } else {
                    System.out.println("That is not a valid selection, need to be an integer between (inclusive) " + 0 + " and " + num);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    /**
     * Called just before termination of this behaviour.
     * return value decides the next state.
     *
     * @return next-state indicator
     */
    @Override
    public int onEnd() {
        if (foundMatchingTourGuide) {
            return 1;
        } else {
            return 0;
        }
    }
}
