package entities.user;

import action.utils.OutMessage;
import fileio.UserInputData;
import entities.video.Video;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public final class PremiumUser extends User {

    public PremiumUser(final UserInputData user) {
        super(user);
    }

    /**
     * - the method returns a message of the recommendation popular result, depending on the success
     * of failure of the execution
     * - creates a hashmap with genres as keys and number of views as values by iterating through
     * all the videos genres and adding or updating the hashmap. In the end the hashmap will contain
     * every genre with their total number of views.
     * - then while there are genres in the hashmap, it finds the popular genre, iterate through all
     * the videos in the data base and finds the first video unseen by the user which is included
     * in the popular genre. If it does not exist, the popular genre is removed and pass to the next
     * popular genre
     */

    @Override
    public String recommendPopular(final ArrayList<Video> videos) {
        HashMap<String, Integer> genreViews = new HashMap<>();

        for (Video video : videos) {
            for (String genre : video.getGenres()) {
                if (genreViews.containsKey(genre)) {
                    genreViews.put(genre, genreViews.get(genre) + video.getNumberViews());
                } else {
                    genreViews.put(genre, video.getNumberViews());
                }
            }
        }

        while (!genreViews.isEmpty()) {
            Integer maxViews = Collections.max(genreViews.values());
            String popularGenre = null;

            for (String genre : genreViews.keySet()) {
                if (genreViews.get(genre).equals(maxViews)) {
                    popularGenre = genre;
                    break;
                }
            }

            for (Video video : videos) {
                if (!history.containsKey(video.getTitle())) {
                    if (video.getGenres().contains(popularGenre)) {
                        return "PopularRecommendation result: " + video.getTitle();
                    }
                }
            }

            genreViews.remove(popularGenre);
        }

        return "PopularRecommendation cannot be applied!";
    }

    /**
     * - the method returns a message of the recommendation search result, depending on
     * the success of failure of the execution
     */

    @Override
    public String recommendSearch(final ArrayList<Video> videos, final String genre) {
        ArrayList<Video> videosSorted = new ArrayList<>();

        filterForSearch(videos, genre, videosSorted);
        sortForSearch(videosSorted);

        if (videosSorted.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }

        StringBuilder outMessage = new StringBuilder("SearchRecommendation result: ");
        return OutMessage.create(videosSorted, videosSorted.size(), outMessage);

    }

    /**
     * filter all the videos in data base (which were not seen by the user) by the genre given as
     * parameter
     */

    private void filterForSearch(final ArrayList<Video> videos,
                                 final String genre,
                                 final ArrayList<Video> filteredVideos) {
        for (Video video : videos) {
            if (video.getGenres().contains(genre)
                    && !getHistory().containsKey(video.getTitle())) {
                filteredVideos.add(video);
            }
        }
    }

    /**
     * sort the videos by their rating and by their titles
     */

    private void sortForSearch(final ArrayList<Video> videos) {
        videos.sort(this::compareForSearch);
    }

    /**
     * compare two videos by their rating and then by their titles
     */

    private int compareForSearch(final Video video1, final Video video2) {
        double rating1 = video1.getAverageRating();
        double rating2 = video2.getAverageRating();

        if (Double.compare(rating1, rating2) == 0) {
            return video1.getOutMethod().compareTo(video2.getOutMethod());
        }

        return Double.compare(rating1, rating2);
    }

    /**
     * - the method returns a message of the recommendation favourite result, depending on
     * the success of failure of the execution
     * - iterates through all the video in the database and finds the first video which is not
     * seen by the user and has the maximum number of appearances in the favourite lists of all
     * users
     */

    @Override
    public String recommendFavourite(final ArrayList<Video> videos) {
        Video mostFavouriteVideo = null;
        int greatestNumber = 0;

        for (Video video : videos) {
            if (!history.containsKey(video.getTitle())
                    && video.getNumberFavourites() > greatestNumber) {
                mostFavouriteVideo = video;
                greatestNumber = video.getNumberFavourites();
            }
        }

        if (mostFavouriteVideo == null) {
            return "FavoriteRecommendation cannot be applied!";
        }

        return "FavoriteRecommendation result: " + mostFavouriteVideo.getTitle();
    }
}
