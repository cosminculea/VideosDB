package entities.user;

import fileio.UserInputData;
import entities.GeneralObject;
import entities.video.Video;

import java.util.ArrayList;
import java.util.Map;

public class User implements GeneralObject {
    private static final int NO_VIDEO_RATING = -1;
    private static final int FIRST_VIEW = 1;

    /**
     * the username of the user
     */

    protected final String username;

    /**
     * list of videos marked as favourite as the user
     */

    protected final ArrayList<String> favouriteVideos;

    /**
     * list of videos viewed by the user
     */

    protected final Map<String, Integer> history;

    /**
     * the number of total ratings the user has done
     */

    protected int numberRatings;

    /**
     * constructor for user
     * @param user input data for user
     */
    public User(final UserInputData user) {
        this.username = user.getUsername();
        this.favouriteVideos = user.getFavoriteMovies();
        this.history = user.getHistory();
        numberRatings = 0;
    }

    /**
     * increment the number of ratings the user has done
     */

    public final void incrementNumberRatings() {
        numberRatings++;
    }

    /**
     * verify if the video is in history
     *  - in case it is already in the history, the number of views is incremented for this user and
     *  then for all users as well
     *  - in case it is not, the title of the video is added and marked with first view
     * @param videoToAdd the video which the user wants to mark as viewed
     */
    public final String commandView(final Video videoToAdd) {
        if (history.containsKey(videoToAdd.getTitle())) {
            history.replace(videoToAdd.getTitle(), history.get(videoToAdd.getTitle()) + 1);
            videoToAdd.incrementViews();
        } else {
            history.put(videoToAdd.getTitle(), FIRST_VIEW);
        }

        return "success -> " + videoToAdd.getTitle()
                + " was viewed with total views of "
                + history.get(videoToAdd.getTitle());

    }

    /**
     * - if the video is not in the history, the method return an error message
     * - if the video is already in the favourite list, the method returns an error message
     * - if the video is viewed and not in the favourite list , the video is added to the list and
     * the number of appearances in favourite lists of the video is incremented
     * @param videoToAdd the video which the entities.user wants to mark as favourite
     */

    public final String commandFavourite(final Video videoToAdd) {
        if (!history.containsKey(videoToAdd.getTitle())) {
            return "error -> " +  videoToAdd.getOutMethod() + " is not seen";
        }

        if (favouriteVideos.contains(videoToAdd.getTitle())) {
            return "error -> " + videoToAdd.getOutMethod() + " is already in favourite list";
        }

        favouriteVideos.add(videoToAdd.getTitle());
        videoToAdd.incrementNumberFavourites();
        return "success -> " + videoToAdd.getOutMethod() + " was added as favourite";
    }

    /**
     * - if the video is not in the history then the method returns an error message
     * - if the process of adding the rating is successful, the number of ratings the has done
     * is incremented and returns a success message, otherwise another error message
     */

    public final String commandRatingVideo(final Video videoToRate,
                                          final double grade,
                                          final int numberSeason) {

        if (!history.containsKey(videoToRate.getTitle())) {
            return "error -> " + videoToRate.getTitle() + " is not seen";
        }

        if (videoToRate.addRating(username, grade, numberSeason)) {
            incrementNumberRatings();
            return "success -> " + videoToRate.getTitle()
                    + " was rated with " + grade
                    + " by " + username;
        }

        return "error -> " + videoToRate.getTitle() + " has been already rated";
    }

    /**
     * iterate through all videos in data base and find the first video unseen
     * if all videos in data base are seen by the user, an error message is returned
     * @param videos all videos in the data base
     */

    public final String recommendStandard(final ArrayList<Video> videos) {
        for (Video video : videos) {
            if (!history.containsKey(video.getTitle())) {
                return "StandardRecommendation result: " + video.getTitle();
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    /**
     *
     * @param videos all videos in the data base
     */

    public final String recommendBestUnseen(final ArrayList<Video> videos) {
        Video bestUnseen = null;
        double greatestRating = NO_VIDEO_RATING;

        for (Video video : videos) {
            if (!history.containsKey(video.getTitle())) {
                if (Double.compare(video.getAverageRating(), greatestRating) >  0) {
                    bestUnseen = video;
                    greatestRating = video.getAverageRating();
                }
            }
        }

        if (bestUnseen == null) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }

        return "BestRatedUnseenRecommendation result: " + bestUnseen.getTitle();
    }

    /**
     * for a standard user the popular recommend cannot be applied
     * @return out message error
     */

    public String recommendPopular(final ArrayList<Video> videos) {
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * for a standard user the recommend search cannot be applied
     * @return out message error
     */

    public String recommendSearch(final ArrayList<Video> videos, final String genre) {
        return "SearchRecommendation cannot be applied!";
    }

    /**
     * for a standard user the favourite recommend cannot be applied
     * @return out message error
     */

    public String recommendFavourite(final ArrayList<Video> videos) {
        return "FavoriteRecommendation cannot be applied!";
    }


    public final String getUsername() {
        return username;
    }

    public final Map<String, Integer> getHistory() {
        return history;
    }

    public final ArrayList<String> getFavouriteVideos() {
        return favouriteVideos;
    }

    public final int getNumberRatings() {
        return numberRatings;
    }

    @Override
    public final String getOutMethod() {
        return username;
    }
}
