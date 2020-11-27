package entities.video;

import entertainment.Season;
import fileio.SerialInputData;
import java.util.ArrayList;

public final class Show extends Video {
    /**
     * number of seasons to the show
     */

    private final int numberOfSeasons;

    /**
     * list of seasons of the show
     */

    private final ArrayList<Season> seasons;

    /**
     * array of users which rated each season
     */

    private final ArrayList<ArrayList<String>> usersRatedSeasons;

    /**
     * constructor witch initialises the object with data from input
     * + initialise the array with users who rated each season with
     * a dimension of number of seasons of the current show
     * @param show the show as he is given to the input
     */

    public Show(final SerialInputData show) {
        super(show);
        numberOfSeasons = show.getNumberSeason();
        seasons = show.getSeasons();
        usersRatedSeasons = new ArrayList<>(numberOfSeasons);

        for (int i = 0; i < numberOfSeasons; i++) {
            usersRatedSeasons.add(new ArrayList<>());
        }
    }

    /**
     * update the average rating of the show by calculating
     * the average rating per season
     */

    public void updateAverageRating() {
        int sumRating = 0;

        for (Season season : seasons) {
            int sumRatingSeason = 0;

            if (season.getRatings().size() != 0) {
                for (double rating : season.getRatings()) {
                    sumRatingSeason += rating;
                }

                sumRating += sumRatingSeason / season.getRatings().size();
            }
        }

        averageRating = sumRating / (double) numberOfSeasons;
    }

    /**
     * verify if entities.user rated the season of the film
     * if user rated then he is not allowed to rate again
     * if user did not rate:
     *      - the grade is added to the season' s ratings
     *      - the user is added to the list of users who rated the season
     *      - update the average rating
     * @param username the username which wants to rate the season
     * @param grade the grade given by the user to the season
     * @param numberSeason the number of the season the username wants to rate
     */
    @Override
    public boolean addRating(final String username,
                             final double grade,
                             final int numberSeason) {
        if (usersRatedSeasons.get(numberSeason - 1).contains(username)) {
            return false;
        }

        seasons.get(numberSeason - 1).getRatings().add(grade);
        usersRatedSeasons.get(numberSeason - 1).add(username);
        updateAverageRating();
        return true;
    }

    /**
     * add to duration the duration of one season (used when creating data base)
     */

    public void addToDuration(final int durationToAdd) {
        duration += durationToAdd;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
