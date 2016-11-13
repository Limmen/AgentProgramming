package kth.se.id2209.limmen.artgallery;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class representing an art-gallery containing a list of artifacts.
 * If no gallery is passed as argument in the constructor it will create a default gallery for convenience.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class ArtGallery {

    ArrayList<Artifact> gallery = new ArrayList();

    public ArtGallery(){
        gallery.add(new Artifact.ArtifactBuilder().name("Accordionist").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Acrobat").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Asleep").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Portrait of Dora Maar").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder().name("The Annunciation").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Ginevra de' Benci").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Madonna Litta").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Woman Sewing").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Fisherman on the Beach").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Girl in the Woods").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Venus de Milo").creationDate(new Date())
                .creator("Alezandros of Antioch").genre("sculpture").placeOfCreation("France").build());
        gallery.add(new Artifact.ArtifactBuilder().name("The Thinker").creationDate(new Date())
                .creator("Auguste Rodin").genre("sculpture").placeOfCreation("France").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Christ de Redeemer").creationDate(new Date())
                .creator("Paul Landowski").genre("sculpture").placeOfCreation("Brazil").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Statue of Liberty").creationDate(new Date())
                .creator("Frederic Auguste Bartholdi").genre("sculpture").placeOfCreation("USA").build());
        gallery.add(new Artifact.ArtifactBuilder().name("My Bed").creationDate(new Date())
                .creator("Tracey Emin").genre("conceptual art").placeOfCreation("UK").build());
        gallery.add(new Artifact.ArtifactBuilder().name("MOMA Poll").creationDate(new Date())
                .creator("Hans Haacke").genre("conceptual art").placeOfCreation("USA").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Away from the Flock").creationDate(new Date())
                .creator("Damien Hirst").genre("conceptual art").placeOfCreation("UK").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Valparaiso").creationDate(new Date())
                .creator("Mario Celedon").genre("street art").placeOfCreation("Chile").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Glasgow").creationDate(new Date())
                .creator("Smug").genre("street art").placeOfCreation("Scotland").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Coffee Time").creationDate(new Date())
                .creator("Pedroconti").genre("digital art").placeOfCreation("unknown").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Floating").creationDate(new Date())
                .creator("jcsketch").genre("digital art").placeOfCreation("unknown").build());
        gallery.add(new Artifact.ArtifactBuilder().name("Motorstorm Apocalypse").creationDate(new Date())
                .creator("PE-Travers").genre("digital art").placeOfCreation("unknown").build());


    }

    public ArtGallery(ArrayList<Artifact> gallery){
        this.gallery = gallery;
    }

    public void addArtefact(Artifact artifact){
        gallery.add(artifact);
    }

    public void removeArtefact(Artifact artifact){
        gallery.remove(artifact);
    }

    public ArrayList<Artifact> getGallery(){
        return gallery;
    }
}
