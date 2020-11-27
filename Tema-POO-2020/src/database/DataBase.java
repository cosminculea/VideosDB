package database;

import entertainment.Season;
import fileio.*;
import entities.actor.Actor;
import entities.user.PremiumUser;
import entities.user.User;
import entities.video.Film;
import entities.video.Show;
import entities.video.Video;

import java.util.ArrayList;
import java.util.List;

public final class DataBase {
    /**
     * all users in the system
     */

    private ArrayList<User> users;

    /**
     * all actors in the system
     */
    private ArrayList<Actor> actors;

    /**
     * all video (films + shows) in the system
     */

    private ArrayList<Video> videos;

    /**
     * all shows in the system
     */

    private ArrayList<Video> shows;

    /**
     * all films in the system
     */

    private ArrayList<Video> films;

    /**
     * constructor which creates all the users, actors and videos in database with the input data
     */

    public DataBase(final Input input) {
        createUsersDataBase(input.getUsers());
        createActorsDataBase(input.getActors());
        createVideoDataBase(input.getMovies(), input.getSerials());
    }

    /**
     * method which creates the users in data base and add them to an array list
     * - the users subscriptions are either standard or premium
     */

    private void createUsersDataBase(final List<UserInputData> usersInput) {
        users = new ArrayList<>(usersInput.size());

        for (UserInputData user : usersInput) {
            if (user.getSubscriptionType().equals("BASIC")) {
                users.add(new User(user));
            } else {
                users.add(new PremiumUser(user));
            }
        }
    }

    /**
     * the method creates all actors in database with the input data
     */

    private void createActorsDataBase(final List<ActorInputData> actorsInput) {
        actors = new ArrayList<>(actorsInput.size());

        for (ActorInputData actor : actorsInput) {
            actors.add(new Actor(actor));
        }
    }

    /**
     * the method creates all the videos in the database and organise them in 3 array lists (films,
     * shows, and videos which contains the first two)
     * - for every film or show which is added in database, the number of appearances in the
     * favourite lists and the number of views are created by iterating through all the users
     * - in addition, for shows, the total duration is calculated by iterating through all seasons
     */

   private void createVideoDataBase(final List<MovieInputData> filmsInput,
                                    final List<SerialInputData> showsInput) {
       videos = new ArrayList<>(filmsInput.size() + showsInput.size());
       shows = new ArrayList<>(showsInput.size());
       films = new ArrayList<>(filmsInput.size());

       for (MovieInputData film : filmsInput) {
           Video newFilm = new Film(film);
           films.add(newFilm);
           videos.add(newFilm);

           for (User user : users) {
               if (user.getHistory().containsKey(film.getTitle())) {
                   newFilm.addToNumberViews(user.getHistory().get(newFilm.getTitle()));
               }

               if (user.getFavouriteVideos().contains(newFilm.getTitle())) {
                   newFilm.incrementNumberFavourites();
               }
           }


       }

       for (SerialInputData show : showsInput) {
           Video newShow = new Show(show);
           shows.add(newShow);
           videos.add(newShow);

           for (Season season : ((Show) newShow).getSeasons()) {
               ((Show) newShow).addToDuration(season.getDuration());
           }

           for (User user : users) {
               if (user.getHistory().containsKey(newShow.getTitle())) {
                   newShow.addToNumberViews(user.getHistory().get(newShow.getTitle()));
               }

               if (user.getFavouriteVideos().contains(newShow.getTitle())) {
                   newShow.incrementNumberFavourites();
               }
           }
       }
   }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public ArrayList<Video> getShows() {
        return shows;
    }

    public ArrayList<Video> getFilms() {
        return films;
    }
}
