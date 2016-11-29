package kth.se.id2209.limmen.hw3.artistmanager.model;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing an auction result.
 *
 * @author Kim Hammar on 2016-11-25.
 */
public class AuctionResult implements Serializable {

    private String result;
    private ArrayList<ACLMessage> bids = new ArrayList<>();
    private double price;
    private boolean sold;
    private AID auctioneer;

    public AuctionResult(String result, double price, boolean sold, AID auctioneer) {
        this.result = result;
        this.price = price;
        this.sold = sold;
        this.auctioneer = auctioneer;
    }

    public String getResult() {
        return result;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSold() {
        return sold;
    }

    public AID getAuctioneer() {
        return auctioneer;
    }

    public ArrayList<ACLMessage> getBids() {
        return bids;
    }

    public void setBids(ArrayList<ACLMessage> bids) {
        this.bids = bids;
    }
}
