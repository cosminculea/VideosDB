package action.query;

import action.Action;
import common.Constants;
import database.DataBase;
import fileio.ActionInputData;
import entities.actor.Actor;
import entities.user.User;
import entities.video.Video;

import java.util.ArrayList;
import java.util.List;

public final class Query implements Action {

    /**
     * query id
     */

    private final int id;
    /**
     * object to which it is made the query
     * possible valid values: "movies", "films", "actors", "users"
     */
    private final String objectType;

    /**
     * number of objects returned by the query
     */

    private final int number;

    /**
     * filters for the object in order to add them
     */

    private final List<List<String>> filters;

    /**
     * the type of sort in ascending / descending order
     */

    private final String sortType;

    /**
     * criteria for sorting
     */

    private final String criteria;

    /**
     * all users in the data base
     */
    private final ArrayList<User> users;

    /**
     * all actors in data base
     */

    private final ArrayList<Actor> actors;

    /**
     * all shows in data base
     */

    private final ArrayList<Video> shows;

    /**
     * all films in data base
     */
    private final ArrayList<Video> films;

    /**
     * all videos in data base
     */

    private final ArrayList<Video> videos;

    /**
     * the out message for the query result
     */

    private String outMessage;

    /** constructor of the query with the input data
     * @param action the action given by the input
     * @param dataBase data base with all actors, users and videos
     */

    public Query(final ActionInputData action, final DataBase dataBase) {
        id = action.getActionId();
        objectType = action.getObjectType();
        number = action.getNumber();
        filters = action.getFilters();
        sortType = action.getSortType();
        criteria = action.getCriteria();
        users = dataBase.getUsers();
        actors = dataBase.getActors();
        videos = dataBase.getVideos();
        films = dataBase.getFilms();
        shows = dataBase.getShows();
    }

    /**
     * execution of query depending on the object type
     */

    public void execute() {
        if (objectType.equals(Constants.MOVIES)) {
            outMessage = QueryVideos.queryMessage(filters, sortType, films, number, criteria);
        }

        if (objectType.equals(Constants.SHOWS)) {
            outMessage = QueryVideos.queryMessage(filters, sortType, shows, number, criteria);
        }

        if (objectType.equals(Constants.USERS)) {
            outMessage = QueryUsers.queryMessage(users, number, sortType);
        }

        if (objectType.equals((Constants.ACTORS))) {
            outMessage = QueryActors.queryMessage(actors, videos, filters,
                                                    sortType, criteria, number);
        }
    }

    public int getId() {
        return id;
    }

    public String getOutMessage() {
        return outMessage;
    }
}
