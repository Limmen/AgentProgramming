package kth.se.id2209.limmen.profiler;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

/**
 * @author Kim Hammar on 2016-11-08.
 */
public class ProfilerAgent extends Agent {

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("ProfilerAgent " + getAID().getName() + " starting up.");

        //Template used both for registering and searching in the DF centralized registry of services
        //The template should not include an AID when used for searching
        final DFAgentDescription dfAgentDescription = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("virtualtour");
        dfAgentDescription.addServices(serviceDescription);

        addBehaviour( new SubscriptionInitiator( this,
                DFService.createSubscriptionMessage( this, getDefaultDF(),
                        dfAgentDescription, null)) //null since we have no search constraints
        {
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] dfAgentDescriptions =
                            DFService.decodeNotification(inform.getContent());

                    System.out.println("Profiler Agent found " + dfAgentDescriptions.length + " virtualtours:");
                    for(int i = 0; i < dfAgentDescriptions.length; i++){
                        System.out.println("Virtual tour " + i + " name: " + dfAgentDescriptions[i].getName()
                        + " ontologies: " + dfAgentDescriptions[i].getAllOntologies());
                    }
                    DFAgentDescription tourGuide = dfAgentDescriptions[0];
                    ACLMessage requestMessage = new ACLMessage(ACLMessage.REQUEST);
                    requestMessage.setContent("test-request");
                    requestMessage.addReceiver(tourGuide.getName());
                    myAgent.send(requestMessage);
                }
                catch (FIPAException fe) {fe.printStackTrace(); }
            }
        });

        //doDelete(); //method from the jade.core.Agent that terminates the agent
    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination
     */
    @Override
    public void takeDown() {
        System.out.println("ProfilerAgent " + getAID().getName() + " terminating.");
    }

}
