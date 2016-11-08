package kth.se.id2209.limmen.curator;

import jade.core.Agent;

/**
 * @author Kim Hammar on 2016-11-08.
 */
public class CuratorAgent extends Agent {

    /**
     * Agent initialization. Called by the JADE runtime envrionment when the agent is started
     */
    @Override
    protected void setup() {
        System.out.println("CuratorAgent " + getAID().getName() + " starting up.");
        doDelete(); //method from the jade.core.Agent that terminates the agent
    }

    /**
     * Agent clean-up before termination. Called by the JADE runtime just before termination
     */
    @Override
    public void takeDown() {
        System.out.println("CuratorAgent " + getAID().getName() + " terminating.");
    }
}
