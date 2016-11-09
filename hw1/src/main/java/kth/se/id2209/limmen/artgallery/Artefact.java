package kth.se.id2209.limmen.artgallery;

import java.util.Date;

/**
 * @author Kim Hammar on 2016-11-09.
 */
public class Artefact {
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
        private int id;
        private String name;
        private String creator;
        private Date creationDate;
        private String placeOfCreation;
        private String genre;

        public ArtefactBuilder(int id, String name) {
            this.id = id;
            this.name = name;
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
