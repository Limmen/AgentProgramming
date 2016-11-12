package kth.se.id2209.limmen.profiler;

import jade.core.AID;

import java.util.ArrayList;

/**
 * Class representing a tourguide for the profiler. Contains AID and supported interests.
 *
 * @author Kim Hammar on 2016-11-12.
 */
public class TourGuide {
    private AID tourGuide;
    private ArrayList<String> supportedInterests;

    public TourGuide(AID tourGuide, ArrayList<String> supportedInterests) {
        this.tourGuide = tourGuide;
        this.supportedInterests = supportedInterests;
    }

    @Override
    public String toString(){
        String string = tourGuide.getName() + " Supported Interests: \n";
        for(String interest : supportedInterests){
            string = string + interest + "\n";
        }
        return  string;
    }

    public AID getTourGuide() {
        return tourGuide;
    }

    public ArrayList<String> getSupportedInterests() {
        return supportedInterests;
    }
}
