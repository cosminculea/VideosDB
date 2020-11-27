package entities.video;

import fileio.ShowInput;
import entities.GeneralObject;

import java.util.ArrayList;

public abstract class Video implements GeneralObject {
    /**
     * title of the video
     */

    protected String title;

    /**
     * year of the video
     */
    protected int year;

    /**
     * list of genres for the video
     */

    protected ArrayList<String> genres;

    /**
     * a list with the name of actors which played in the video
     */

    protected ArrayList<String> cast;

    /**
     * total views of all users in the data base for the video
     */

    protected int numberViews;

    /**
     * the number of appearances in the favourites list of all users
     */

    protected int numberFavourites;

    /**
     * duration of the film
     */

    protected int duration;

    /**
     * rating of the film
     */

    protected double averageRating;

    /**
     * constructor for video with input data
     * @param videoInput data from input for video
     */

    public Video(final ShowInput videoInput) {
        title = videoInput.getTitle();
        year = videoInput.getYear();
        genres = videoInput.getGenres();
        cast = videoInput.getCast();
    }

    /**
     * abstract method for adding and updating the rating
     */

    public abstract boolean addRating(String video, double grade, int numberSeason);

    /**
     * @return title of the video (all output messages for videos actions should print the title)
     */

    @Override
    public final String getOutMethod() {
        return title;
    }

    /**
     * add the number to total views
     */

    public final void addToNumberViews(final int numberToAdd) {
        numberViews += numberToAdd;
    }

    /**
     * increment the total number of appearances
     */

    public final void incrementNumberFavourites() {
        numberFavourites++;
    }

    /**
     * increment the total number of views
     */

    public final void incrementViews() {
        numberViews++;
    }

    public final int getYear() {
        return year;
    }

    public final String getTitle() {
        return title;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final double getAverageRating() {
        return averageRating;
    }

    public final int getNumberViews() {
        return numberViews;
    }

    public final int getDuration() {
        return duration;
    }

    public final int getNumberFavourites() {
        return numberFavourites;
    }

}
