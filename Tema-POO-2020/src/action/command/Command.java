package action.command;

import action.Action;
import database.DataBase;
import fileio.ActionInputData;
import entities.GetObject;
import entities.user.User;
import entities.video.Video;

import java.util.ArrayList;

public final class Command implements Action {
    private static final String VIEW = "view";
    private static final String FAVOURITE = "favorite";
    private static final String RATING = "rating";

    /**
     * command id
     */

    private final int id;

    /**
     * username of the use who execute the command
     */

    private final String username;

    /**
     * title of the film the entities.user wants to execute the command on
     */

    private final String title;

    /**
     * the type of command (view, favorite, rating)
     */

    private final String type;

    /**
     * the grade for the rating command
     */

    private final double grade;

    /**
     * number season which should be rated
     */

    private final int numberSeason;

    /**
     * all users in data base
     */

    private final ArrayList<User> users;

    /**
     * all videos in data base
     */
    private final ArrayList<Video> videos;

    /**
     * the message of the command depending on the execution success or failure
     */

    private String outMessage;


    /**
     * constructor of the command with the input data
     * @param action the action given by the input
     * @param dataBase data base with all actors, users and videos
     */

    public Command(final ActionInputData action,
                   final DataBase dataBase) {
        id = action.getActionId();
        username = action.getUsername();
        title = action.getTitle();
        type = action.getType();
        grade = action.getGrade();
        numberSeason = action.getSeasonNumber();
        users = dataBase.getUsers();
        videos = dataBase.getVideos();
    }

    /**
     * find user which executes de command
     * find video that the entities.user wants to mark as seen, favourite or rate
     * verify the type of the command in order to be executed by the entities.user
     * in addition:
     *  - for commandView, the number of total views of the video is incremented
     */
    public void execute() {
        User user = GetObject.getUser(users, username);
        Video video = GetObject.getVideo(videos, title);

        if (video == null) {
            outMessage = "error video not found";
            return;
        }

        if (user == null) {
            outMessage = "error user not found";
            return;
        }

        if (type.equals(VIEW)) {
            outMessage = user.commandView(video);
            return;
        }

        if (type.equals(FAVOURITE)) {
            outMessage = user.commandFavourite(video);
            return;
        }

        if (type.equals(RATING)) {
            outMessage = user.commandRatingVideo(video, grade, numberSeason);
        }
    }

    public String getOutMessage() {
        return outMessage;
    }

    public int getId() {
        return id;
    }
}
