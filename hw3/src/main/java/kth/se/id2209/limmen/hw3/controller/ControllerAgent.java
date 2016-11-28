package kth.se.id2209.limmen.hw3.controller;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Location;
import jade.core.ProfileImpl;
import jade.core.Runtime;
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
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ControllerAgent that initializes containers, receives commands from user and delegates commands to agents.
 *
 * Inspired from example at: http://www.iro.umontreal.ca/~vaucher/Agents/Jade/Mobility.html
 *
 * @author Kim Hammar on 2016-11-28.
 */
public class ControllerAgent extends GuiAgent {
    private AgentContainer homeContainer;
    private AgentContainer[] createdContainers;
    private Map containersOnPlatform = new HashMap();
    private ArrayList<String> agentNames = new ArrayList();
    transient protected ControllerAgentGUI myGui;
    private Runtime runtime = Runtime.instance();

    public static final int QUIT = 0;
    public static final int NEW_AGENT = 1;
    public static final int MOVE_AGENT = 2;
    public static final int CLONE_AGENT = 3;
    public static final int KILL_AGENT = 4;
    /**
     * Agent initialization. Called by the JADE runtime environment when the agent is started
     */
    protected void setup() {
        /**
         * Content manager manages the content languages and ontologies "known" by a given agent.
         * We register new languages that is required that our agent knows.
         * SLCodec is the codec class for the FIPA-SLn languages.
         * MobilityOntology is the class that represents the ontology used for JADE mobility.
         */
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(MobilityOntology.getInstance());

        /**
         * Create containers. ProfileImpl allows us to set boot-parameters for the new containers.
         */
        homeContainer = getContainerController(); //retrieve the containercontroller that this agent lives in
        createdContainers = new AgentContainer[3]; //we require 3 containers for this scenario
        ProfileImpl curatorContainer1 = new ProfileImpl();
        curatorContainer1.setParameter(ProfileImpl.CONTAINER_NAME, "Curator-Container-1");
        ProfileImpl curatorContainer2 = new ProfileImpl();
        curatorContainer2.setParameter(ProfileImpl.CONTAINER_NAME, "Curator-Container-2");
        ProfileImpl artistManagerContainer = new ProfileImpl();
        artistManagerContainer.setParameter(ProfileImpl.CONTAINER_NAME, "Artistmanager-Container");
        createdContainers[0] = runtime.createAgentContainer(curatorContainer1);
        createdContainers[1] = runtime.createAgentContainer(curatorContainer2);
        createdContainers[2] = runtime.createAgentContainer(artistManagerContainer);
        doWait(2000); //wait while containers initializes

        /**
         * Request a list of all containers on the platform from AMS
         */
        getAllContainers();

        /**
         * Initialize gui
         */
        myGui = new ControllerAgentGUI(this, (String []) containersOnPlatform.keySet().toArray());
        myGui.setVisible(true);

    }

    /**
     * Inherited from GuiAgent. Called when a GUI-event happens in the ControllerGUI
     *
     * @param guiEvent gui-event
     */
    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        int eventType = guiEvent.getType();

        /**
         * Shutdown this agent and all containers/agents we have created.
         */
        if (eventType == QUIT) {
            try {
                homeContainer.kill();
                for (int i = 0; i < createdContainers.length; i++) createdContainers[i].kill();
            } catch (Exception e) {
                e.printStackTrace();
            }
            myGui.setVisible(false);
            myGui.dispose();
            doDelete();
            System.exit(0);
        }
        if (eventType == NEW_AGENT) {
            Object[] args = new Object[2]; //arguments for creating the agent
            args[0] = getAID(); //the newly created agent should know about the controller
            String agentClass = (String) guiEvent.getParameter(0); //get parameters of the event
            String agentName = (String) guiEvent.getParameter(1);
            try {
                //Create the new agent on the homecontainer
                AgentController agentController = homeContainer.createNewAgent(agentName, agentClass, args);
                agentController.start(); //start the new agent
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
            agentNames.add(agentName);
            myGui.updateAgentsList(agentNames);
            myGui.pack();
            return;
        }
        /**
         * All events except NEW_AGENT require agentName as first parameter
         */
        String agentName = (String) guiEvent.getParameter(0);
        AID agentAID = new AID(agentName, AID.ISLOCALNAME);
        if (eventType == KILL_AGENT) {
            sendKillAgentRequestToAgent(agentAID);
            agentNames.remove(agentName);
            myGui.updateAgentsList(agentNames);
            myGui.pack();
            return;
        }
        /**
         * Both CLONE_AGENT event and MOVE_AGENT event require destination container parameter
         */
        String destinationName = (String) guiEvent.getParameter(1);
        Location destinationContainer = (Location) containersOnPlatform.get(destinationName);
        if(eventType == MOVE_AGENT){
            sendMoveRequestToAgent(agentAID, destinationContainer);
            return;
        }
        if(eventType == CLONE_AGENT){
            String newAgentName = (String) guiEvent.getParameter(2);
            sendCloneRequestToAgent(agentAID, destinationContainer, newAgentName);
            agentNames.add(newAgentName);
            myGui.updateAgentsList(agentNames);
            myGui.pack();
        }
    }

    /**
     * Sends a request to AMS for a list of all containers on the platform
     */
    private void getAllContainers() {
        AID ams = getAMS();
        QueryPlatformLocationsAction queryPlatformLocationsAction = new QueryPlatformLocationsAction();
        sendRequest(new Action(ams, queryPlatformLocationsAction));
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchSender(getAMS()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        ACLMessage resp = blockingReceive(mt);
        ContentElement ce = null;
        try {
            ce = getContentManager().extractContent(resp);
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
        Result result = (Result) ce;
        jade.util.leap.Iterator it = result.getItems().iterator();
        while (it.hasNext()) {
            Location loc = (Location) it.next();
            containersOnPlatform.put(loc.getName(), loc);
        }
    }

    /**
     * Method for sending a move-request to a particular agent
     *
     * @param agentAID AID of agent to move
     * @param destinationContainer container to move to
     */
    private void sendMoveRequestToAgent(AID agentAID, Location destinationContainer){
        MobileAgentDescription mobileAgentDescription = new MobileAgentDescription();
        mobileAgentDescription.setName(agentAID);
        mobileAgentDescription.setDestination(destinationContainer);
        MoveAction moveAction = new MoveAction();
        moveAction.setMobileAgentDescription(mobileAgentDescription);
        sendRequest(new Action(agentAID, moveAction));
    }

    /**
     * Method for sending a CloneAgent request to a particular agent
     *
     * @param agentToBeClonedAID AID of agent to be cloned
     * @param destinationContainer container to put the clone
     * @param newAgentName name of the clone
     */
    private void sendCloneRequestToAgent(AID agentToBeClonedAID, Location destinationContainer, String newAgentName){
        MobileAgentDescription mobileAgentDescription = new MobileAgentDescription();
        mobileAgentDescription.setName(agentToBeClonedAID);
        mobileAgentDescription.setDestination(destinationContainer);
        CloneAction cloneAction = new CloneAction();
        cloneAction.setNewName(newAgentName);
        cloneAction.setMobileAgentDescription(mobileAgentDescription);
        sendRequest(new Action(agentToBeClonedAID, cloneAction));
    }

    /**
     * Method for sending a kill-agent request to an agent
     *
     * @param agentAID AID for agent to be killed
     */
    private void sendKillAgentRequestToAgent(AID agentAID){
        KillAgent killAgentAction = new KillAgent(); //Class representing the kill-agent action that can be requested to the AMS
        killAgentAction.setAgent(agentAID);
        sendRequest(new Action(agentAID, killAgentAction));
    }

    /**
     * Method for sending request to an agent for a generic action
     *
     * @param action action to send
     */
    private void sendRequest(Action action) {
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
}
