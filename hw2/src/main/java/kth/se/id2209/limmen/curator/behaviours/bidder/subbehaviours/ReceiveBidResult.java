package kth.se.id2209.limmen.curator.behaviours.bidder.subbehaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.states.MsgReceiver;

/**
 * Behaviour for receiving result after having placed a bid in a dutch auction
 *
 * @author Kim Hammar on 2016-11-18.
 */
public class ReceiveBidResult extends MsgReceiver {

    public ReceiveBidResult(Agent a, MessageTemplate mt, long deadline, DataStore s, Object msgKey) {
        super(a, mt, deadline, s, msgKey);
    }

    /**
     * This is invoked when a message matching the specified template is received or the timeout has expired.
     * Prints the result to the user.
     *
     * @param msg the result of the bid, sent by the auctioneer
     */
    protected void handleMessage(ACLMessage msg) {
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
            System.out.println("Congratulations!");
            System.out.println(msg.getContent());
        }
        if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL){
            System.out.println("Bad luck");
            System.out.println(msg.getContent());
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }
}
