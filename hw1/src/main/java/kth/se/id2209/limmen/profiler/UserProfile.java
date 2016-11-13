package kth.se.id2209.limmen.profiler;

import kth.se.id2209.limmen.artgallery.Artifact;

import java.util.ArrayList;

/**
 * Class representing a userprofile.
 *
 * @author Kim Hammar on 2016-11-10.
 */
public class UserProfile {
    private int age;
    private String occupation;
    private String gender;
    private String interest;
    private String name;
    private ArrayList<Artifact> visitedArtifacts = new ArrayList();


    public UserProfile(UserProfileBuilder userProfileBuilder) {
        age = userProfileBuilder.age;
        occupation = userProfileBuilder.occupation;
        gender = userProfileBuilder.gender;
        interest = userProfileBuilder.interest;
        name = userProfileBuilder.name;
    }

    @Override
    public String toString() {
        String string = "name: " + name + " | age: " + age + " | gender: " + gender + " | interest: " + interest + " | occupation: " + occupation +
                " | visited artifacts:";
        for (Artifact artifact : visitedArtifacts) {
            string = string + "\n" + artifact.toString();
        }
        return string;
    }

    public int getAge() {
        return age;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getInterest() {
        return interest;
    }

    public ArrayList<Artifact> getVisitedArtifacts() {
        return visitedArtifacts;
    }

    public void addVisitedArtifact(Artifact artifact) {
        if (!visitedArtifacts.contains(artifact)){
            visitedArtifacts.add(artifact);
        }
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public static class UserProfileBuilder {
        private int age;
        private String occupation;
        private String gender;
        private String interest;
        private String name;

        public UserProfileBuilder() {

        }

        public UserProfileBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserProfileBuilder occupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        public UserProfileBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public UserProfileBuilder interest(String interest) {
            this.interest = interest;
            return this;
        }

        public UserProfileBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserProfile build() {
            return new UserProfile(this);
        }

    }
}
