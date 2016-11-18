package kth.se.id2209.limmen.artistmanager;

import jade.core.AID;

import java.util.ArrayList;

/**
 * Class representing an auction.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class Auction {

    private double initialPrice;
    private String artifactTitle;
    private double reservePrice;
    private double rateOfReduction;
    private double currentPrice;
    private boolean firstRound;
    private ArrayList<AID> participants = new ArrayList<>();

    public Auction(double initialPrice, String artifactTitle, double reservePrice, double rateOfReduction) {
        this.initialPrice = initialPrice;
        this.artifactTitle = artifactTitle;
        this.reservePrice = reservePrice;
        this.rateOfReduction = rateOfReduction;
        this.currentPrice = initialPrice;
        firstRound = true;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public String getArtifactTitle() {
        return artifactTitle;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public double getRateOfReduction() {
        return rateOfReduction;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public boolean isFirstRound() {
        return firstRound;
    }

    public void setFirstRound(boolean firstRound) {
        this.firstRound = firstRound;
    }

    public ArrayList<AID> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<AID> participants) {
        this.participants = participants;
    }
}
