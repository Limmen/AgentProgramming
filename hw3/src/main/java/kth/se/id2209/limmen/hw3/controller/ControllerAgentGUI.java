package kth.se.id2209.limmen.hw3.controller;

import jade.gui.GuiEvent;
import kth.se.id2209.limmen.hw3.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.hw3.curator.CuratorAgent;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 *
 * JFrame for the controllerAgent. Takes commands from user and delegates to AMS/agents.
 *
 * Inspired from example at: http://www.iro.umontreal.ca/~vaucher/Agents/Jade/Mobility.html
 *
 * @author Kim Hammar on 2016-11-23.
 */
public class ControllerAgentGUI extends JFrame implements ActionListener {
    private JList listOfAgents;
    private DefaultListModel listOfAgentsModel;
    private JComboBox containers, agentTypes;
    private JButton newAgentButton, moveButton, cloneButton, killButton, quitButton;
    private JTextField agentNameField;
    private ControllerAgent myAgent;
    private String[] containerNames;
    private ControllerAgentGUI controllerAgentGUI;
    private String[] agentClasses = new String[]{CuratorAgent.class.getName(), ArtistManagerAgent.class.getName()};

    /**
     * Class constructor initializing the frame
     *
     * @param controllerAgent controlleragent that this frame represents
     * @param containerNames names of containers on the platform
     */
    public ControllerAgentGUI(ControllerAgent controllerAgent, String[] containerNames) {
        super("ControllerFrame");
        this.myAgent = controllerAgent;
        this.containerNames = containerNames;
        controllerAgentGUI = this;
        setLayout(new MigLayout());
        setContentPane(new Container());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                shutDown();
            }
        });
        pack();
        setLocationRelativeTo(null); //center on screen
        setVisible(true);
    }

    /**
     * Handles user-invoked actions
     *
     * @param actionEvent actionevent
     */
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == newAgentButton) {
            GuiEvent ge = new GuiEvent(this, myAgent.NEW_AGENT);
            ge.addParameter(agentTypes.getSelectedItem());
            ge.addParameter(agentNameField.getText());
            myAgent.postGuiEvent(ge);
        } else if (actionEvent.getSource() == moveButton) {

            GuiEvent ge = new GuiEvent(this, myAgent.MOVE_AGENT);
            ge.addParameter(listOfAgents.getSelectedValue());
            ge.addParameter(containers.getSelectedItem());
            myAgent.postGuiEvent(ge);
        } else if (actionEvent.getSource() == cloneButton) {
            String name = JOptionPane.showInputDialog(this, "Name of the clone: ");
            if(name == null)//user cancelled
                return;
            GuiEvent ge = new GuiEvent(this, myAgent.CLONE_AGENT);
            ge.addParameter(listOfAgents.getSelectedValue());
            ge.addParameter(containers.getSelectedItem());
            ge.addParameter(name);
            myAgent.postGuiEvent(ge);
        } else if (actionEvent.getSource() == killButton) {

            GuiEvent ge = new GuiEvent(this, myAgent.KILL_AGENT);
            ge.addParameter(listOfAgents.getSelectedValue());
            myAgent.postGuiEvent(ge);
        } else if (actionEvent.getSource() == quitButton) {
            shutDown();
        }
    }

    /**
     * Method for generating a shutdown event
     */
    void shutDown() {
        GuiEvent ge = new GuiEvent(this, myAgent.QUIT);
        myAgent.postGuiEvent(ge);
    }

    /**
     * Method for updating the listOfAgents of  agents
     * @param agentNames names of agents
     */
    public void updateAgentsList(ArrayList<String> agentNames) {
        listOfAgentsModel.clear();
        for(String agentName : agentNames){
            listOfAgentsModel.addElement(agentName);
        }
    }

    /**
     * Container panel
     */
    private class Container extends JPanel {
        private Container(){
            setLayout(new MigLayout("wrap 1, insets 50 50 50 50"));
            add(new AgentsNavigation(), "span 1");
            add(new CreateAgent(), "span 1, gaptop 100");
            add(quitButton = new JButton("Quit"), "span 1");
            quitButton.setToolTipText("Terminate this program");
            quitButton.addActionListener(controllerAgentGUI);
        }
    }

    /**
     * Panel for navigating agents
     */
    private class AgentsNavigation extends JPanel {
        private AgentsNavigation(){
            setLayout(new MigLayout("wrap 3"));
            add(new JLabel("List of agents: "), "span 3, center");
            listOfAgentsModel = new DefaultListModel();
            listOfAgents = new JList(listOfAgentsModel);
            listOfAgents.setBorder(new EmptyBorder(2, 2, 2, 2));
            listOfAgents.setVisibleRowCount(5);
            listOfAgents.setFixedCellHeight(18);
            listOfAgents.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            JScrollPane listPane = new JScrollPane(listOfAgents);
            listPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            listPane.setPreferredSize(new Dimension(400, 250));
            add(listPane, "span 3, center");
            add(new JLabel("Destination :"), "span 1");
            containers = new JComboBox(containerNames);
            add(containers, "span 2");
            add(moveButton = new JButton("Move"), "span 1");
            moveButton.setToolTipText("Move agent to a new location");
            moveButton.addActionListener(controllerAgentGUI);
            add(cloneButton = new JButton("Clone"), "span 1");
            cloneButton.setToolTipText("Clone selected agent");
            cloneButton.addActionListener(controllerAgentGUI);
            add(killButton = new JButton("Kill"), "span 1");
            killButton.setToolTipText("Kill selected agent");
            killButton.addActionListener(controllerAgentGUI);
            moveButton.setEnabled(false);
            cloneButton.setEnabled(false);
            killButton.setEnabled(false);
            listOfAgents.addListSelectionListener((ListSelectionEvent e) -> {
                if (listOfAgents.getSelectedIndex() == -1) {
                    moveButton.setEnabled(false);
                    cloneButton.setEnabled(false);
                    killButton.setEnabled(false);
                } else {
                    moveButton.setEnabled(true);
                    cloneButton.setEnabled(true);
                    killButton.setEnabled(true);
                }
            });
        }
    }

    /**
     * Panel for creating new agents
     */
    private class CreateAgent extends JPanel {
        private CreateAgent(){
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Create new agent"), "span 2");
            JLabel lbl = new JLabel("Type of agent: ");
            add(lbl, "span 1");
            agentTypes = new JComboBox(agentClasses);
            add(agentTypes, "span 1");
            lbl = new JLabel("Name: ");
            add(lbl, "span 1");
            agentNameField = new JTextField(25);
            add(agentNameField, "span 1");
            add(newAgentButton = new JButton("New agent"), "span 2");
            newAgentButton.setToolTipText("Create a new agent");
            newAgentButton.addActionListener(controllerAgentGUI);
        }

    }


}
