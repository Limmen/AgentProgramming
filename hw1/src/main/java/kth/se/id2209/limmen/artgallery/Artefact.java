package kth.se.id2209.limmen.artgallery;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class Artefact implements Serializable {
    private int id;
    private String name;
    private String creator;
    private Date creationDate;
    private String placeOfCreation;
    private String genre;

    public Artefact(ArtefactBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.creator = builder.creator;
        this.creationDate = builder.creationDate;
        this.placeOfCreation = builder.placeOfCreation;
        this.genre = builder.genre;
    }

    @Override
    public String toString(){
        String creationDateString = "null";
        if(creationDate != null)
            creationDateString = creationDate.toString();
        return "" + id + " " + name + " " + creator + " " + creationDateString + " " + placeOfCreation + " " + genre;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getPlaceOfCreation() {
        return placeOfCreation;
    }

    public String getGenre() {
        return genre;
    }

    public static class ArtefactBuilder {
        private int id = -1;
        private String name;
        private String creator;
        private Date creationDate;
        private String placeOfCreation;
        private String genre;

        public ArtefactBuilder() {
        }

        public ArtefactBuilder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public ArtefactBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArtefactBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ArtefactBuilder creator(String creator) {
            this.creator = creator;
            return this;
        }

        public ArtefactBuilder creationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public ArtefactBuilder placeOfCreation(String placeOfCreation) {
            this.placeOfCreation = placeOfCreation;
            return this;
        }

        public ArtefactBuilder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Artefact build() {
            return new Artefact(this);
        }
    }

}
