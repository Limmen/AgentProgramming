package kth.se.id2209.limmen.hw3;

import jade.core.AID;
import jade.core.Location;

import javax.swing.*;

/**
 * @author Kim Hammar on 2016-11-23.
 */
public interface HW3Agent {

    public AID getController();

    public Location getDestination();

    public JFrame getMyGui();

    public void setDestination(Location destination);
}
