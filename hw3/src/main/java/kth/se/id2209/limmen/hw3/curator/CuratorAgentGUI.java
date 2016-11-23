package kth.se.id2209.limmen.hw3.curator;

import jade.gui.GuiAgent;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kim Hammar on 2016-11-23.
 */
public class CuratorAgentGUI extends JFrame {
// -----------------------------------------

    private JLabel container;
    private JTextArea log;
    private GuiAgent myAgent;

    public CuratorAgentGUI(GuiAgent artistManagerAgent) {
        myAgent = artistManagerAgent;
        setTitle(myAgent.getLocalName());
        setLayout(new MigLayout());
        setContentPane(new Container());
        pack();
        setLocationRelativeTo(null); //center on screen
        setVisible(true);
    }

    private class Container extends JPanel{
        private Container(){
            setLayout(new MigLayout("wrap 1"));
            add(new MainPanel(), "span 1");
        }
    }

    private class MainPanel extends JPanel{
        private MainPanel(){
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Name: "), "span 1");
            add(new JLabel(myAgent.getLocalName()), "span 1");
            add(new JLabel("Class: "), "span 1");
            add(new JLabel(myAgent.getClass().getName()), "span 1");
            add(new JLabel("Container: "), "span 1");
            container = new JLabel(myAgent.here().getName());
            add(container, "span 1");
            add(new JLabel("Log:" ) , "span 2");
            log = new JTextArea("");
            log.setLineWrap(true);
            log.setEditable(false);
            JScrollPane logPane = new JScrollPane(log);
            logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            logPane.setPreferredSize(new Dimension(400, 250));
            add(logPane, "span 2, center");
        }
    }
    public void setLocation(String loc){

        this.container.setText(loc);
    }

    public void updateLog(String log){
        this.log.setText(log);
    }

}
