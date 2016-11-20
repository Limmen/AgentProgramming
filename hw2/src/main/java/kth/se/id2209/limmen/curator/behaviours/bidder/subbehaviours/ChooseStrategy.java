package kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.curator.CuratorAgent;
import kth.se.id2209.limmen.curator.model.Strategy;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Behaviour for interacting with the user and choosing a strategy
 *
 * @author Kim Hammar on 2016-11-19.
 */
public class ChooseStrategy extends OneShotBehaviour {

    /**
     * Class constructor, initializes the behaviour by setting the datastore.
     *
     * @param dataStore datastore for communicating with other behaviours
     */
    public ChooseStrategy(DataStore dataStore) {
        setDataStore(dataStore);
    }

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Strategy> strategies = ((CuratorAgent) myAgent).getStrategies();
        while (true) {
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            System.out.println("Choose strategy for participating in dutch auctions by entering the associated number");
            int num = 0;
            for (Strategy strategy : strategies) {
                System.out.println(num + " : " + strategy.toString());
                num++;
            }
            int strategy = Integer.parseInt(scanner.nextLine());
            if (strategy >= 0 && strategy < num) {
                getDataStore().put(CuratorAgent.AUCTION_STRATEGY, strategies.get(strategy));
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }
}
