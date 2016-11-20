package kth.se.id2209.limmen.curator.model;

/**
 *
 * Class representing a Strategy for a bidder in a dutch auction
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class Strategy {
    private double percentOfValuation;

    public Strategy(double percentOfValuation) {
        this.percentOfValuation = percentOfValuation;
    }

    public double getPercentOfValuation() {
        return percentOfValuation;
    }

    @Override
    public String toString(){
        return "Accept auctioneers price if it is less than or equal to " + percentOfValuation*100 + "% of your valuation";
    }

    public double getPrice(double valuation){
        return valuation * percentOfValuation;
    }

    public boolean acceptPrice(double price, double valuation){
        return (price <= valuation * percentOfValuation);
    }
}
