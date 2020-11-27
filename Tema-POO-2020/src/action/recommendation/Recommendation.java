package action.recommendation;

import action.Action;
import database.DataBase;
import fileio.ActionInputData;
import entities.GetObject;
import entities.user.User;
import entities.video.Video;
import java.util.ArrayList;

public final class Recommendation implements Action {
    private static final String STANDARD = "standard";
    private static final String BEST_UNSEEN = "best_unseen";
    private static final String POPULAR = "popular";
    private static final String SEARCH = "search";
    private static final String FAVOURITE = "favorite";

    /**
     * recommendation id
     */

    private final int id;

    /**
     * username of the user who wants to get the recommendation
     */

    private final String username;

    /**
     * the type of recommendation
     */

    private final String type;

    /**
     * the genre of the video for search recommendation
     */

    private final String genre;

    /**
     * all users in the data base
     */

    private final ArrayList<User> users;

    /**
     * all videos in data base
     */

    private final ArrayList<Video> videos;

    /**
     * the message of the recommendation depending on the execution success or failure
     */

    private String outMessage;

    /** constructor of the recommendation with the input data
     * @param action the action given by the input
     * @param dataBase data base with all actors, users and videos
     */

    public Recommendation(final ActionInputData action,
                          final DataBase dataBase) {
        id = action.getActionId();
        username = action.getUsername();
        genre = action.getGenre();
        type = action.getType();
        users = dataBase.getUsers();
        videos = dataBase.getVideos();
    }

    /**
     * find entities.user who wants to get the recommendation (if cannot be found -> error message)
     * verify the type of the recommendation (standard, best unseen, popular, favourite, search)
     */

    public void execute() {
        User user = GetObject.getUser(users, username);

        if (user == null) {
            outMessage = "error user not found";
            return;
        }

        if (type.equals(STANDARD)) {
          outMessage = user.recommendStandard(videos);
        }

        if (type.equals(BEST_UNSEEN)) {
            outMessage = user.recommendBestUnseen(videos);
        }

        if (type.equals(POPULAR)) {
            outMessage = user.recommendPopular(videos);
        }

        if (type.equals(SEARCH)) {
            outMessage = user.recommendSearch(videos, genre);
        }

        if (type.equals(FAVOURITE)) {
            outMessage = user.recommendFavourite(videos);
        }
    }

    public int getId() {
        return id;
    }

    public String getOutMessage() {
        return outMessage;
    }
}
