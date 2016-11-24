package kth.se.id2209.limmen.hw3.curator.model;

import java.io.Serializable;

/**
 * Class representing a Strategy for a bidder in a dutch auction
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class Strategy implements Serializable {
    private double percentOfValuation;
    private double valuation = 100;

    public Strategy(double percentOfValuation) {
        this.percentOfValuation = percentOfValuation;
    }

    public double getPercentOfValuation() {
        return percentOfValuation;
    }

    @Override
    public String toString() {
        return "Accept auctioneers price if it is less than or equal to " + percentOfValuation * 100 + "% of your valuation";
    }

    public double getPrice() {
        return valuation * percentOfValuation;
    }

    public boolean acceptPrice(double price) {
        return (price <= valuation * percentOfValuation);
    }

    public double getValuation() {
        return valuation;
    }

    public void setValuation(double valuation) {
        this.valuation = valuation;
    }
}
