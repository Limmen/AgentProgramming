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
        gallery.add(new Artifact.ArtifactBuilder(1, "Accordionist").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder(2, "Acrobat").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder(3, "Asleep").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder(4, "Portrait of Dora Maar").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artifact.ArtifactBuilder(5, "The Annunciation").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artifact.ArtifactBuilder(6, "Ginevra de' Benci").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artifact.ArtifactBuilder(7, "Madonna Litta").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artifact.ArtifactBuilder(8, "Woman Sewing").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artifact.ArtifactBuilder(9, "Fisherman on the Beach").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artifact.ArtifactBuilder(10, "Girl in the Woods").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artifact.ArtifactBuilder(11, "Venus de Milo").creationDate(new Date())
                .creator("Alezandros of Antioch").genre("sculpture").placeOfCreation("France").build());
        gallery.add(new Artifact.ArtifactBuilder(12, "The Thinker").creationDate(new Date())
                .creator("Auguste Rodin").genre("sculpture").placeOfCreation("France").build());
        gallery.add(new Artifact.ArtifactBuilder(13, "Christ de Redeemer").creationDate(new Date())
                .creator("Paul Landowski").genre("sculpture").placeOfCreation("Brazil").build());
        gallery.add(new Artifact.ArtifactBuilder(14, "Statue of Liberty").creationDate(new Date())
                .creator("Frederic Auguste Bartholdi").genre("sculpture").placeOfCreation("USA").build());

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
