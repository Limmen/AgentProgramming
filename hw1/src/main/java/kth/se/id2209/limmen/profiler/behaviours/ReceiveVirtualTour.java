package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

/**
 * @author Kim Hammar on 2016-11-10.
 */
public class ReceiveVirtualTour extends OneShotBehaviour {
    public static String TOUR_TITLES = "Tour titles";
    private int exitValue = 0;
    @Override
    public void action() {
        //System.out.println("ReceiveVirtualTour starting");
        //MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate mt = MessageTemplate.MatchAll();
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("ReceiveVirtualTour received message");
            try {
                ArrayList<String> titles = (ArrayList<String>) msg.getContentObject();
                System.out.println("PROFILER RECEIVED TITLES SIZE: " + titles.size());
                getDataStore().put(TOUR_TITLES, titles);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            exitValue = 1;
        }
        else {
            block();
        }
    }

    @Override
    public int onEnd() {
        //System.out.println("Virtual tour receiver exitiing");
        //reset();
        return exitValue;
    }
}
