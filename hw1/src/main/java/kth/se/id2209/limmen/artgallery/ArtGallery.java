package kth.se.id2209.limmen.artgallery;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class ArtGallery {

    ArrayList<Artefact> gallery = new ArrayList();

    public ArtGallery(){
        gallery.add(new Artefact.ArtefactBuilder(1, "Accordionist").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artefact.ArtefactBuilder(2, "Acrobat").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artefact.ArtefactBuilder(3, "Asleep").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artefact.ArtefactBuilder(4, "Portrait of Dora Maar").creationDate(new Date())
                .creator("Pablo Picasso").genre("painting").placeOfCreation("Spain").build());
        gallery.add(new Artefact.ArtefactBuilder(5, "The Annunciation").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artefact.ArtefactBuilder(6, "Ginevra de' Benci").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artefact.ArtefactBuilder(7, "Madonna Litta").creationDate(new Date())
                .creator("Leonardo da Vinci").genre("painting").placeOfCreation("Italy").build());
        gallery.add(new Artefact.ArtefactBuilder(8, "Woman Sewing").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artefact.ArtefactBuilder(9, "Fisherman on the Beach").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artefact.ArtefactBuilder(10, "Girl in the Woods").creationDate(new Date())
                .creator("Vincent van Gogh").genre("painting").placeOfCreation("Netherlands").build());
        gallery.add(new Artefact.ArtefactBuilder(11, "Venus de Milo").creationDate(new Date())
                .creator("Alezandros of Antioch").genre("sculpture").placeOfCreation("France").build());
        gallery.add(new Artefact.ArtefactBuilder(12, "The Thinker").creationDate(new Date())
                .creator("Auguste Rodin").genre("sculpture").placeOfCreation("France").build());
        gallery.add(new Artefact.ArtefactBuilder(13, "Christ de Redeemer").creationDate(new Date())
                .creator("Paul Landowski").genre("sculpture").placeOfCreation("Brazil").build());
        gallery.add(new Artefact.ArtefactBuilder(14, "Statue of Liberty").creationDate(new Date())
                .creator("Frederic Auguste Bartholdi").genre("sculpture").placeOfCreation("USA").build());

    }

    public void addArtefact(Artefact artefact){
        gallery.add(artefact);
    }

    public void removeArtefact(Artefact artefact){
        gallery.remove(artefact);
    }

    public ArrayList<Artefact> getGallery(){
        return gallery;
    }
}
