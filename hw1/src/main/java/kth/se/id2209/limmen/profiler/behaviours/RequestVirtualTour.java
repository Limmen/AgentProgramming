package kth.se.id2209.limmen.profiler.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import kth.se.id2209.limmen.profiler.ProfilerAgent;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class RequestVirtualTour extends OneShotBehaviour {

    @Override
    public void action() {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        System.out.println("RequestVirtualTour!! preparing requests");
        AID receiver = ((ProfilerAgent) myAgent).getTourGuide();
        request.addReceiver(receiver);
        request.setOntology("VirtualTourServer received msg");
        request.setContent("virtual-tour");
        myAgent.send(request);
    }
}


