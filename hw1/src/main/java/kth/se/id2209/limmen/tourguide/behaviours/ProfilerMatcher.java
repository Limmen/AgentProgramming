package kth.se.id2209.limmen.tourguide.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;

/**
 * Behaviour that waits for proposals from profiler-agents. The proposals indicate what kind of tours that the
 * profiler is interested in, this behaviour is linked to the FindSupportedInterests behaviour that will collect a
 * list of supported interests, and if there is a match with the profiler's interest, accept the proposal, otherwise
 * reject.
 *
 * @author Kim Hammar on 2016-11-09.
 */

public class ProfilerMatcher extends ProposeResponder {

    protected static String RESULT_KEY;
    protected static String PROPOSE_KEY2;

    /**
     * Class constructor that initializes the behaviour.
     *
     * @param a agent running the behaviour
     * @param mt messagetemplate for filtering out the message that we want to receive
     * @param store datastore for communicating with other behaviours.
     */
    public ProfilerMatcher(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
        RESULT_KEY = RESPONSE_KEY;
        PROPOSE_KEY2 = PROPOSE_KEY;
    }

    /**
     * Called just before termination of this behaviour.
     * This behaviour is continous.
     * @return
     */
    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

}
