package kth.se.id2209.limmen.hw3.artistmanager;

import jade.gui.GuiEvent;
import kth.se.id2209.limmen.hw3.curator.CuratorAgent;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import java.util.Vector;

/**
 * @author Kim Hammar on 2016-11-23.
 */
public class ControllerAgentGUI extends JFrame implements ActionListener {
// -----------------------------------------------------------------------

    private JList list;
    private DefaultListModel listModel;
    private JComboBox locations, agentTypes;
    private JButton newAgent, move, clone, kill, quit;
    private JTextField agentNameField;
    private ControllerAgent myAgent;
    private Set set;
    private ControllerAgentGUI controllerAgentGUI;
    private String[] agentClasses = new String[]{CuratorAgent.class.getName(), ArtistManagerAgent.class.getName()};

    public ControllerAgentGUI(ControllerAgent a, Set s) {
        super("ControllerFrame");
        this.myAgent = a;
        this.set = s;
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

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == newAgent) {
            GuiEvent ge = new GuiEvent(this, myAgent.NEW_AGENT);
            ge.addParameter((String) agentTypes.getSelectedItem());
            ge.addParameter((String) agentNameField.getText());
            myAgent.postGuiEvent(ge);
        } else if (ae.getSource() == move) {

            GuiEvent ge = new GuiEvent(this, myAgent.MOVE_AGENT);
            ge.addParameter((String) list.getSelectedValue());
            ge.addParameter((String) locations.getSelectedItem());
            myAgent.postGuiEvent(ge);
        } else if (ae.getSource() == clone) {

            GuiEvent ge = new GuiEvent(this, myAgent.CLONE_AGENT);
            ge.addParameter((String) list.getSelectedValue());
            ge.addParameter((String) locations.getSelectedItem());
            myAgent.postGuiEvent(ge);
        } else if (ae.getSource() == kill) {

            GuiEvent ge = new GuiEvent(this, myAgent.KILL_AGENT);
            ge.addParameter((String) list.getSelectedValue());
            myAgent.postGuiEvent(ge);
        } else if (ae.getSource() == quit) {
            shutDown();
        }
    }

    void shutDown() {
        GuiEvent ge = new GuiEvent(this, myAgent.QUIT);
        myAgent.postGuiEvent(ge);
    }

    public void updateList(Vector v) {
        listModel.clear();
        for (int i = 0; i < v.size(); i++) {
            listModel.addElement(v.get(i));
        }
    }

    private class Container extends JPanel {
        private Container(){
            setLayout(new MigLayout("wrap 1, insets 50 50 50 50"));
            add(new AgentsNavigation(), "span 1");
            add(new CreateAgent(), "span 1, gaptop 100");
            add(quit = new JButton("Quit"), "span 1");
            quit.setToolTipText("Terminate this program");
            quit.addActionListener(controllerAgentGUI);
        }
    }

    private class AgentsNavigation extends JPanel {

        private AgentsNavigation(){
            setLayout(new MigLayout("wrap 3"));
            add(new JLabel("List of agents: "), "span 3, center");
            listModel = new DefaultListModel();
            list = new JList(listModel);
            list.setBorder(new EmptyBorder(2, 2, 2, 2));
            list.setVisibleRowCount(5);
            list.setFixedCellHeight(18);
            list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            JScrollPane listPane = new JScrollPane(list);
            listPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            listPane.setPreferredSize(new Dimension(400, 250));
            add(listPane, "span 3, center");
            add(new JLabel("Destination :"), "span 1");
            locations = new JComboBox(set.toArray());
            add(locations, "span 2");
            add(move = new JButton("Move"), "span 1");
            move.setToolTipText("Move agent to a new location");
            move.addActionListener(controllerAgentGUI);
            add(clone = new JButton("Clone"), "span 1");
            clone.setToolTipText("Clone selected agent");
            clone.addActionListener(controllerAgentGUI);
            add(kill = new JButton("Kill"), "span 1");
            kill.setToolTipText("Kill selected agent");
            kill.addActionListener(controllerAgentGUI);
            move.setEnabled(false);
            clone.setEnabled(false);
            kill.setEnabled(false);
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (list.getSelectedIndex() == -1) {
                        move.setEnabled(false);
                        clone.setEnabled(false);
                        kill.setEnabled(false);
                    } else {
                        move.setEnabled(true);
                        clone.setEnabled(true);
                        kill.setEnabled(true);
                    }
                }
            });
        }
    }

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
            add(newAgent = new JButton("New agent"), "span 2");
            newAgent.setToolTipText("Create a new agent");
            newAgent.addActionListener(controllerAgentGUI);
        }

    }


}
