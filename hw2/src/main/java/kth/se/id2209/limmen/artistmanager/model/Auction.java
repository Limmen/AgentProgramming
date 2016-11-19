package kth.se.id2209.limmen.artistmanager.model;

import jade.core.AID;

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
    private AID winner;

    public Auction(double initialPrice, String artifactTitle, double reservePrice, double rateOfReduction) {
        this.initialPrice = initialPrice;
        this.artifactTitle = artifactTitle;
        this.reservePrice = reservePrice;
        this.rateOfReduction = rateOfReduction;
        this.currentPrice = initialPrice;
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

    public AID getWinner() {
        return winner;
    }

    public void setWinner(AID winner) {
        this.winner = winner;
    }
}
