package kth.se.id2209.limmen.hw3.controller;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Location;
import jade.core.ProfileImpl;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.CloneAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-23.
 */
public class ControllerAgent extends GuiAgent {
// --------------------------------------------

    private jade.wrapper.AgentContainer home;
    private jade.wrapper.AgentContainer[] container = null;
    private Map locations = new HashMap();
    private Vector agents = new Vector();
    private int agentCnt = 0;
    private int command;
    transient protected ControllerAgentGUI myGui;

    public static final int QUIT = 0;
    public static final int NEW_AGENT = 1;
    public static final int MOVE_AGENT = 2;
    public static final int CLONE_AGENT = 3;
    public static final int KILL_AGENT = 4;

    // Get a JADE Runtime instance
    jade.core.Runtime runtime = jade.core.Runtime.instance();

    protected void setup() {
// ------------------------

        // Register language and ontology
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());

        try {
            // Create the container objects
            home = getContainerController();
            container = new jade.wrapper.AgentContainer[3];
            ProfileImpl curatorContainer1 = new ProfileImpl();
            curatorContainer1.setParameter(ProfileImpl.CONTAINER_NAME, "Curator-Container-1");
            ProfileImpl curatorContainer2 = new ProfileImpl();
            curatorContainer2.setParameter(ProfileImpl.CONTAINER_NAME, "Curator-Container-2");
            ProfileImpl artistManagerContainer = new ProfileImpl();
            artistManagerContainer.setParameter(ProfileImpl.CONTAINER_NAME, "Artistmanager-Container");
            container[0] = runtime.createAgentContainer(curatorContainer1);
            container[1] = runtime.createAgentContainer(curatorContainer2);
            container[2] = runtime.createAgentContainer(artistManagerContainer);
            /*
            for (int i = 0; i < 5; i++){
                container[0] = runtime.createAgentContainer(new ProfileImpl());
            }*/
            doWait(2000);

            // Get available locations with AMS
            sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));

            //Receive response from AMS
            MessageTemplate mt = MessageTemplate.and(
                    MessageTemplate.MatchSender(getAMS()),
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            ACLMessage resp = blockingReceive(mt);
            ContentElement ce = getContentManager().extractContent(resp);
            Result result = (Result) ce;
            jade.util.leap.Iterator it = result.getItems().iterator();
            while (it.hasNext()) {
                Location loc = (Location) it.next();
                locations.put(loc.getName(), loc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Create and show the gui
        myGui = new ControllerAgentGUI(this, locations.keySet());
        myGui.setVisible(true);
    }


    protected void onGuiEvent(GuiEvent ev) {

        command = ev.getType();

        if (command == QUIT) {
            try {
                home.kill();
                for (int i = 0; i < container.length; i++) container[i].kill();
            } catch (Exception e) {
                e.printStackTrace();
            }
            myGui.setVisible(false);
            myGui.dispose();
            doDelete();
            System.exit(0);
        }
        if (command == NEW_AGENT) {

            jade.wrapper.AgentController a = null;
            try {
                Object[] args = new Object[2];
                args[0] = getAID();
                String agentClass = (String) ev.getParameter(0);
                String name = (String) ev.getParameter(1);
                a = home.createNewAgent(name, agentClass, args);
                a.start();
                agents.add(name);
                myGui.updateList(agents);
                myGui.pack();
            } catch (Exception ex) {
                System.out.println("Problem creating new agent");
            }
            return;
        }
        String agentName = (String) ev.getParameter(0);
        AID aid = new AID(agentName, AID.ISLOCALNAME);

        if (command == MOVE_AGENT) {
            String destName = (String) ev.getParameter(1);
            Location dest = (Location) locations.get(destName);
            MobileAgentDescription mad = new MobileAgentDescription();
            mad.setName(aid);
            mad.setDestination(dest);
            MoveAction ma = new MoveAction();
            ma.setMobileAgentDescription(mad);
            sendRequest(new Action(aid, ma));
        } else if (command == CLONE_AGENT) {

            String destName = (String) ev.getParameter(1);
            Location dest = (Location) locations.get(destName);
            MobileAgentDescription mad = new MobileAgentDescription();
            mad.setName(aid);
            mad.setDestination(dest);
            String newName = "Clone-" + agentName;
            CloneAction ca = new CloneAction();
            ca.setNewName(newName);
            ca.setMobileAgentDescription(mad);
            sendRequest(new Action(aid, ca));
            agents.add(newName);
            myGui.updateList(agents);
            myGui.pack();
        } else if (command == KILL_AGENT) {

            KillAgent ka = new KillAgent();
            ka.setAgent(aid);
            sendRequest(new Action(aid, ka));
            agents.remove(agentName);
            myGui.updateList(agents);
            myGui.pack();
        }
    }


    void sendRequest(Action action) {
// ---------------------------------

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(new SLCodec().getName());
        request.setOntology(MobilityOntology.getInstance().getName());
        try {
            getContentManager().fillContent(request, action);
            request.addReceiver(action.getActor());
            send(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}//class Controller

