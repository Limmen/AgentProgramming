package kth.se.id2209.limmen.tourguide;

import jade.core.AID;
import java.io.Serializable;

/**
 *
 * Class representing an item in a virtual tour
 *
 * @author Kim Hammar on 2016-11-10.
 */
public class TourItem implements Serializable{

    private String title;
    private AID curator;

    public TourItem(String title, AID curator) {
        this.title = title;
        this.curator = curator;
    }

    @Override
    public String toString(){
        return "Artifact Name: " + title + "   |   Art-Gallery Curator: " + curator.getName();
    }

    @Override
    public boolean equals(Object object){
        if(((TourItem) object).getTitle().equals(title))
            return true;
        else
            return false;
    }

    public String getTitle() {
        return title;
    }

    public AID getCurator() {
        return curator;
    }
}
