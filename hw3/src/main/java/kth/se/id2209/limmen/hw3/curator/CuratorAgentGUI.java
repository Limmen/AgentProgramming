package kth.se.id2209.limmen.hw3.curator;

import kth.se.id2209.limmen.hw3.curator.model.Strategy;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 *
 * JFrame for representing the status of a CuratorAgent and for taking commands from the user.
 *
 * @author Kim Hammar on 2016-11-23.
 */

public class CuratorAgentGUI extends JFrame {
    private JLabel container;
    private JComboBox strategy;
    private JTextField valuation;
    private JTextArea log;
    private CuratorAgent myAgent;

    /**
     * Class constructor for initializing the frame
     *
     * @param curatorAgent agent that this frame is representing
     */
    public CuratorAgentGUI(CuratorAgent curatorAgent) {
        myAgent = curatorAgent;
        setTitle(myAgent.getLocalName());
        setLayout(new MigLayout());
        setContentPane(new Container());
        pack();
        setLocationRelativeTo(null); //center on screen
        setVisible(true);
    }

    /**
     * Container panel
     */
    private class Container extends JPanel {
        private Container() {
            setLayout(new MigLayout("wrap 1"));
            add(new MainPanel(), "span 1");
            add(new SetStrategyPanel(), "span 1, gaptop 30");
        }
    }

    /**
     * Panel with general information about the agent
     */
    private class MainPanel extends JPanel {
        private MainPanel() {
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Name: "), "span 1");
            add(new JLabel(myAgent.getLocalName()), "span 1");
            add(new JLabel("Class: "), "span 1");
            add(new JLabel(myAgent.getClass().getName()), "span 1");
            add(new JLabel("Container: "), "span 1");
            container = new JLabel(myAgent.here().getName());
            add(container, "span 1");
            add(new JLabel("Log:"), "span 2");
            log = new JTextArea("");
            log.setLineWrap(true);
            log.setEditable(false);
            JScrollPane logPane = new JScrollPane(log);
            logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            logPane.setPreferredSize(new Dimension(400, 250));
            add(logPane, "span 2, center");
        }
    }

    /**
     * Panel for selecting strategy for dutch auctions
     */
    private class SetStrategyPanel extends JPanel {
        private SetStrategyPanel() {
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Change your strategy"), "span 2");
            add(new JLabel("Select strategy"), "span 1");
            strategy = new JComboBox(myAgent.getStrategies().toArray());
            strategy.setSelectedItem(myAgent.getStrategy());
            //strategy.setSe
            add(strategy, "span 1");
            add(new JLabel("Your valuation of the good:"), "span 1");
            valuation = new JTextField(25);
            add(valuation, "span 1");
            valuation.setText(Double.toString(myAgent.getStrategy().getValuation()));
            JButton save = new JButton("Save");
            add(save, "span 2");
            save.addActionListener(e -> {
                Strategy strategy1 = (Strategy) strategy.getSelectedItem();
                strategy1.setValuation(Double.parseDouble(valuation.getText()));
                myAgent.updateStrategy(strategy1);
            });
        }
    }

    /**
     * Method for updating the current location of the agent.
     *
     * @param loc new location
     */
    public void setLocation(String loc) {

        this.container.setText(loc);
    }

    /**
     * Method for updating the log of the agent
     *
     * @param log new log
     */
    public void updateLog(String log) {
        this.log.setText(log);
    }

}
