package kth.se.id2209.limmen.hw3.artistmanager.behaviours.receivecommands;

/**
 *
 * Behaviour for receiving commands from controller-agent
 *
 * @author Kim Hammar on 2016-11-23.
 */
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import kth.se.id2209.limmen.hw3.HW3Agent;


public class ReceiveCommands extends CyclicBehaviour {

    public ReceiveCommands(Agent a) {
        super(a);
    }

    /**
     * Main action, receives messages from controller and takes appropriate action.
     */
    public void action() {

        ACLMessage msg = myAgent.receive(MessageTemplate.MatchSender(((HW3Agent) myAgent).getController()));

        if (msg == null) {
            block();
            return;
        }

        if (msg.getPerformative() == ACLMessage.REQUEST) {

            try {
                ContentElement content = myAgent.getContentManager().extractContent(msg);
                Concept concept = ((Action) content).getAction();

                if (concept instanceof CloneAction) {

                    CloneAction ca = (CloneAction) concept;
                    String newName = ca.getNewName();
                    Location l = ca.getMobileAgentDescription().getDestination();
                    if (l != null) ((HW3Agent) myAgent).setDestination(l);
                    myAgent.doClone(((HW3Agent) myAgent).getDestination(), newName);
                } else if (concept instanceof MoveAction) {

                    MoveAction ma = (MoveAction) concept;
                    Location l = ma.getMobileAgentDescription().getDestination();
                    if (l != null) {
                        ((HW3Agent) myAgent).setDestination(l);
                        myAgent.doMove(((HW3Agent) myAgent).getDestination());
                    }
                } else if (concept instanceof KillAgent) {

                    ((HW3Agent) myAgent).getMyGui().setVisible(false);
                    ((HW3Agent) myAgent).getMyGui().dispose();
                    myAgent.doDelete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Unexpected msg from controller agent");
        }
    }
}
