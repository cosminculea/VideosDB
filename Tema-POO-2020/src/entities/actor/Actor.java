package entities.actor;

import fileio.ActorInputData;
import entities.GeneralObject;
import actor.ActorsAwards;
import java.util.ArrayList;
import java.util.Map;

public final class Actor implements GeneralObject {
    /**
     * name of the actor
     */

    private final String name;

    /**
     * the description of the actor's career
     */

    private final String careerDescription;

    /**
     * list of all videos the actor played in
     */

    private final ArrayList<String> filmography;

    /**
     * list of awards which the actor has achieved
     */

    private final Map<ActorsAwards, Integer> awards;

    /**
     * the rating of all actor's filmography
     */

    private double ratingVideos;

    /**
     * the number of total awards
     */

    private int totalAwards;

    /**
     * constructor with the input data
     */

    public Actor(final ActorInputData actor) {
        this.name = actor.getName();
        this.careerDescription = actor.getCareerDescription();
        this.filmography = actor.getFilmography();
        this.awards = actor.getAwards();
        ratingVideos = 0;
        totalAwards = 0;

        for (Integer numberAwards : awards.values()) {
            totalAwards += numberAwards;
        }
    }

    /**
     * updates the rating of all videos in which the actor played
     */

    public void updateRatingVideos(final double newRatingVideos) {
        ratingVideos = newRatingVideos;
    }

    /**
     * the out message for query with actors implies their names
     */

    @Override
    public String getOutMethod() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public double getRatingVideos() {
        return ratingVideos;
    }

    public int getTotalAwards() {
        return totalAwards;
    }
}
