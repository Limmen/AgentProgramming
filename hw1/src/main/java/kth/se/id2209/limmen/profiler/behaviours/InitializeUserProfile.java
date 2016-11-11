package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.profiler.ProfilerAgent;
import kth.se.id2209.limmen.profiler.UserProfile;

import java.util.Scanner;

/**
 * OneShotBehaviour that reads user-information from stdIn and builds a userprofile.
 *
 * @author Kim Hammar on 2016-11-10.
 */
public class InitializeUserProfile extends OneShotBehaviour {

    /***
     * Action to build the userprofile.
     */
    @Override
    public void action() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your user information");
        System.out.println("Enter your name and press [ENTER]");
        String name = scanner.nextLine();
        System.out.println("Enter your interest (art genre) and press [ENTER]");
        String interest = scanner.nextLine();
        System.out.println("Enter your occupation and press [ENTER]");
        String occupation = scanner.nextLine();
        System.out.println("Enter your age and press [ENTER]");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter your gender and press [ENTER]");
        String gender = scanner.nextLine();
        UserProfile userProfile = new UserProfile.UserProfileBuilder().name(name).
                interest(interest).occupation(occupation).age(age).gender(gender).build();
        ((ProfilerAgent) myAgent).setUserProfile(userProfile);
    }
}
