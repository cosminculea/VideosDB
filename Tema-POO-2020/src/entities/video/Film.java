package entities.video;

import fileio.MovieInputData;
import entities.GeneralObject;
import java.util.ArrayList;

public final class Film extends Video implements GeneralObject {

    /**
     * sum of ratings given by users
     */

    private double ratingSum;

    /**
     * number of ratings given by users
     */

    private int numberOfRatings;

    /**
     * list with users which rated the current film
     */

    private final ArrayList<String> usersRatedFilm;

    /**
     * constructor witch initialise the object with data from input
     * @param movie the film as he is given to the input
     */
    public Film(final MovieInputData movie) {
        super(movie);
        duration = movie.getDuration();
        averageRating = 0;
        numberOfRatings = 0;
        usersRatedFilm = new ArrayList<>();
    }

    /**
     * update the average rating of the film
     */
    public void updateAverageRating() {
        if (numberOfRatings != 0) {
            averageRating =  ratingSum / numberOfRatings;
        }
    }

    /**
     * verify if user rated the film
     * if user rated then he is not allowed to rate again
     * if user did not rate:
     *      - the grade is added to sum rating
     *      - increment number of ratings
     *      - the user is added to the list of users who rated
     *      - update the average rating
     * @param username the name of the user which request the rating
     * @param grade the grade which he wants to add to the rating
     */

    public boolean addRating(final String username, final double grade, final int numberSeason) {
        if (usersRatedFilm.contains(username)) {
            return false;
        }

        ratingSum += grade;
        numberOfRatings++;
        usersRatedFilm.add(username);
        updateAverageRating();
        return true;
    }
}
