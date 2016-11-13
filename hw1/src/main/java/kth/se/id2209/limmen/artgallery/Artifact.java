package kth.se.id2209.limmen.artgallery;

import java.io.Serializable;
import java.util.Date;

/**
 * Class representing an Art-Artifact.
 *
 * @author Kim Hammar on 2016-11-09.
 */
public class Artifact implements Serializable {

    private static int staticReferenceCounter = 0;
    private int id;
    private String name;
    private String creator;
    private Date creationDate;
    private String placeOfCreation;
    private String genre;

    public Artifact(ArtifactBuilder builder) {
        this.id = staticReferenceCounter++;
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

    @Override
    public boolean equals(Object obect){
        return ((Artifact) obect).getName().equals(name);
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

    public static class ArtifactBuilder {
        private String name;
        private String creator;
        private Date creationDate;
        private String placeOfCreation;
        private String genre;

        public ArtifactBuilder() {
        }

        public ArtifactBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ArtifactBuilder creator(String creator) {
            this.creator = creator;
            return this;
        }

        public ArtifactBuilder creationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public ArtifactBuilder placeOfCreation(String placeOfCreation) {
            this.placeOfCreation = placeOfCreation;
            return this;
        }

        public ArtifactBuilder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Artifact build() {
            return new Artifact(this);
        }
    }

}
