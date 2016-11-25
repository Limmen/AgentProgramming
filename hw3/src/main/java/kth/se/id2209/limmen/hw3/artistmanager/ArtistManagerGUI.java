package kth.se.id2209.limmen.hw3.artistmanager;

import kth.se.id2209.limmen.hw3.artgallery.Artifact;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kim Hammar on 2016-11-23.
 */
public class ArtistManagerGUI extends JFrame {
// -----------------------------------------

    private JLabel container, subResults;
    private JTextArea log;
    private ArtistManagerAgent myAgent;
    private JComboBox good;
    private JTextField initialPrice, reservePrice, rateOfReduction;

    public ArtistManagerGUI(ArtistManagerAgent artistManagerAgent) {
        myAgent = artistManagerAgent;
        setTitle(myAgent.getLocalName());
        setLayout(new MigLayout());
        setContentPane(new Container());
        pack();
        setLocationRelativeTo(null); //center on screen
        setVisible(true);
    }

    private class Container extends JPanel {
        private Container() {
            setLayout(new MigLayout("wrap 1"));
            add(new MainPanel(), "span 1");
            add(new CreateAuction(), "span 1, gaptop 30");
        }
    }

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
            add(new ClonesResult(), "span 2");
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

    private class CreateAuction extends JPanel {
        private CreateAuction() {
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Create Auction"), "span 2");
            add(new JLabel("Good for auction: "), "span 1");
            good = new JComboBox(myAgent.getArtGallery().getGallery().toArray());
            add(good, "span 1");
            add(new JLabel("Initial price: "), "span 1");
            initialPrice = new JTextField(25);
            add(initialPrice, "span 1");
            add(new JLabel("Reserve price: "), "span 1");
            reservePrice = new JTextField(25);
            add(reservePrice, "span 1");
            add(new JLabel("Rate of reduction: "), "span 1");
            rateOfReduction = new JTextField(25);
            add(rateOfReduction, "span 1");
            JButton startAuction = new JButton("StartAuction");
            add(startAuction, "span 2");
            startAuction.addActionListener(e -> {
                Double initPrice = Double.parseDouble(initialPrice.getText());
                Double resPrice = Double.parseDouble(reservePrice.getText());
                Double rate = Double.parseDouble(rateOfReduction.getText());
                Auction auction = new Auction(initPrice, ((Artifact) good.getSelectedItem()).getName(), resPrice, rate);
                myAgent.startAuction(auction);
            });
        }
    }

    private class ClonesResult extends JPanel {
        private ClonesResult(){
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Number of sub-results received from clones:"), "span 1");
            subResults = new JLabel(Integer.toString(myAgent.getClonesResult().size()));
            add(subResults, "span 1");
            JButton synthesizeResults = new JButton("Synthesize results");
            add(synthesizeResults, "span 2");
            synthesizeResults.addActionListener(e -> {
                myAgent.synthesizeResults();
            });
        }
    }

    public void updateClonesResult(){
        this.subResults.setText(Integer.toString(myAgent.getClonesResult().size()));
    }

    public void setLocation(String loc) {

        this.container.setText(loc);
    }

    public void updateLog(String log) {
        this.log.setText(log);
    }

}
