package kth.se.id2209.limmen.hw3.artistmanager;

import kth.se.id2209.limmen.hw3.artgallery.Artifact;
import kth.se.id2209.limmen.hw3.artistmanager.model.Auction;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame for representing the status of a ArtistManagerAgent and for taking commands from the user.
 *
 * @author Kim Hammar on 2016-11-23.
 */
public class ArtistManagerGUI extends JFrame {
    private JLabel container, subResults, auctionOnGoing;
    private JTextArea logArea;
    private JButton closeAuction, reportToParent, startAuction, synthesizeResults;
    private ArtistManagerAgent myAgent;
    private JComboBox auctiongoodComboBox;
    private JTextField initialPriceField, reservePriceField, rateOfReductionField;

    /**
     * Class constructor initializing the frame
     *
     * @param artistManagerAgent agent that his frame represents
     */
    public ArtistManagerGUI(ArtistManagerAgent artistManagerAgent) {
        myAgent = artistManagerAgent;
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
            add(new CreateAuction(), "span 1, gaptop 30");
        }
    }

    /**
     * Panel containing general information about the agent
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
            add(new JLabel("Auction onGoing: "), "span 1");
            auctionOnGoing = new JLabel(Boolean.toString(myAgent.isAuctionOngoing()));
            add(auctionOnGoing, "span 1");
            add(new ClonesResult(), "span 2");
            add(new JLabel("Log:"), "span 2");
            logArea = new JTextArea("");
            logArea.setLineWrap(true);
            logArea.setEditable(false);
            JScrollPane logPane = new JScrollPane(logArea);
            logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            logPane.setPreferredSize(new Dimension(400, 250));
            add(logPane, "span 2, center");
        }
    }

    /**
     * Panel for creating a new auction
     */
    private class CreateAuction extends JPanel {
        private CreateAuction() {
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Create Auction"), "span 2");
            add(new JLabel("Good for auction: "), "span 1");
            auctiongoodComboBox = new JComboBox(myAgent.getArtGallery().getGallery().toArray());
            add(auctiongoodComboBox, "span 1");
            add(new JLabel("Initial price: "), "span 1");
            initialPriceField = new JTextField(25);
            add(initialPriceField, "span 1");
            add(new JLabel("Reserve price: "), "span 1");
            reservePriceField = new JTextField(25);
            add(reservePriceField, "span 1");
            add(new JLabel("Rate of reduction: "), "span 1");
            rateOfReductionField = new JTextField(25);
            add(rateOfReductionField, "span 1");
            startAuction = new JButton("StartAuction");
            startAuction.setEnabled(!myAgent.isBiddingClosed());
            add(startAuction, "span 2");
            startAuction.addActionListener(e -> {
                Double initPrice = Double.parseDouble(initialPriceField.getText());
                Double resPrice = Double.parseDouble(reservePriceField.getText());
                Double rate = Double.parseDouble(rateOfReductionField.getText());
                Auction auction = new Auction(initPrice, ((Artifact) auctiongoodComboBox.getSelectedItem()).getName(), resPrice, rate);
                myAgent.startAuction(auction);
            });
            closeAuction = new JButton("CloseAuction");
            closeAuction.setEnabled(myAgent.isAuctionReadyToBeClosed());
            add(closeAuction, "span 2");
            closeAuction.addActionListener(e -> {
                myAgent.closeAuction();
            });
        }
    }

    /**
     * Panel for managing auction-results received from clones
     */
    private class ClonesResult extends JPanel {
        private ClonesResult() {
            setLayout(new MigLayout("wrap 2"));
            add(new JLabel("Number of sub-results received from clones:"), "span 1");
            subResults = new JLabel(Integer.toString(myAgent.getClonesResult().size()));
            add(subResults, "span 1");
            synthesizeResults = new JButton("Synthesize results");
            synthesizeResults.setEnabled(myAgent.getClonesResult().size() > 0);
            add(synthesizeResults, "span 2");
            synthesizeResults.addActionListener(e -> {
                myAgent.synthesizeResults();
            });
            reportToParent = new JButton("Report to parent-agent");
            reportToParent.setEnabled(myAgent.isBiddingClosed() && !myAgent.isHaveReported());
            add(reportToParent, "span 2");
            reportToParent.addActionListener(e -> {
                myAgent.reportToParent();
            });
        }
    }

    /**
     * Method for updating the number of sub-results received from clones
     */
    public void updateClonesResult() {
        this.subResults.setText(Integer.toString(myAgent.getClonesResult().size()));
        synthesizeResults.setEnabled(myAgent.getClonesResult().size() > 0);
    }

    /**
     * Method for updating the label that tells if the auction is ongoing
     */
    public void updateAuctionOnGoing() {
        this.auctionOnGoing.setText(Boolean.toString(myAgent.isAuctionOngoing()));
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
     * Method to update gui after bidding closed/opened
     */
    public void updateBiddingClosed() {
        reportToParent.setEnabled(myAgent.isBiddingClosed() && !myAgent.isHaveReported());
        startAuction.setEnabled(!myAgent.isBiddingClosed());
    }

    /**
     * Method for updating the logArea of the agent
     *
     * @param log new logArea
     */
    public void updateLog(String log) {
        this.logArea.setText(log);
    }

    /**
     * Method to update gui after receiving auction result from parent
     */
    public void updateAuctionDone() {
        closeAuction.setEnabled(myAgent.isAuctionReadyToBeClosed());
    }

}
