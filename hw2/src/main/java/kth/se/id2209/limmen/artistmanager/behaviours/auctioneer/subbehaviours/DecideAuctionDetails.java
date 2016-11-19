package kth.se.id2209.limmen.artistmanager.behaviours.auctioneer.subbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import kth.se.id2209.limmen.artgallery.Artifact;
import kth.se.id2209.limmen.artistmanager.ArtistManagerAgent;
import kth.se.id2209.limmen.artistmanager.model.Auction;

import java.util.Scanner;

/**
 * Behaviour for interacting with the user and choosing details of the auction.
 *
 * @author Kim Hammar on 2016-11-17.
 */
public class DecideAuctionDetails extends OneShotBehaviour {

    /**
     * Main action of the behaviour
     */
    @Override
    public void action() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("Please enter details for the auction");
        System.out.println("Select one of the following artifacts to sell by entering the id:");
        for (Artifact artifact : ((ArtistManagerAgent) myAgent).getArtGallery().getGallery()) {
            System.out.println(artifact.toString());
        }
        int id = Integer.parseInt(scanner.nextLine());
        Artifact artifact = ((ArtistManagerAgent) myAgent).getArtGallery().getGallery().get(id);
        System.out.println("Enter your initial price [ENTER]");
        double initialPrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter your reserve price (lowest price to sell for) [ENTER]");
        double reservePrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter rate of reduction [ENTER]");
        double rateOfReduction = Double.parseDouble(scanner.nextLine());
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        Auction auction = new Auction(initialPrice, artifact.getName(), reservePrice, rateOfReduction);
        getDataStore().put(ArtistManagerAgent.AUCTION, auction);
    }
}
