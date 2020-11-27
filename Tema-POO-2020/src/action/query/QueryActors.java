package action.query;

import action.utils.OutMessage;
import entities.GeneralObject;
import entities.GetObject;
import entities.actor.Actor;
import entities.video.Video;
import utils.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QueryActors {
    public static final String DESCENDING = "desc";
    public static final String ASCENDING = "asc";
    public static final String AVERAGE = "average";
    public static final String AWARDS = "awards";
    public static final String DESCRIPTION = "filter_description";
    public static final int AWARDS_IN_FILTERS = 3;
    public static final int KEYWORDS_IN_FILTERS = 2;

    private QueryActors() { }

    /**
     * - call getActorsSorted which returns a filtered
     * and sorted list of actors depending on the criteria given and the sort type
     * - for cases DESCRIPTION and AWARDS, the number which represents how many actors to be printed
     * is changed witch the size of the array, in order to print all the actors in the list
     * - call method for creating the out message for the query
     * @return out message for the query
     */

    public static String queryMessage(final ArrayList<Actor> actors,
                                      final ArrayList<Video> videos,
                                      final List<List<String>> filters,
                                      final String sortType,
                                      final String criteria,
                                      final int number) {

        ArrayList<GeneralObject> sortedActors = getActorsSorted(actors, videos, filters,
                                                                                sortType, criteria);
        int numberOfActors = number;
        if (criteria.equals(DESCRIPTION)) {
            numberOfActors = sortedActors.size();
        }

        if (criteria.equals(AWARDS)) {
            numberOfActors = sortedActors.size();
        }

        StringBuilder outMessage = new StringBuilder("Query result: ");
        return OutMessage.create(sortedActors, numberOfActors, outMessage);

    }

    /**
     * the method returns a filtered and sorted list of actors, depending on the criteria and
     * sort type
     */

    public static ArrayList<GeneralObject> getActorsSorted(final ArrayList<Actor> actors,
                                                           final ArrayList<Video> videos,
                                                           final List<List<String>> filters,
                                                           final String sortType,
                                                           final String criteria) {
        ArrayList<GeneralObject> sortedActors = new ArrayList<>();

        if (criteria.equals(AVERAGE)) {
            filterActorsRating(actors, videos, sortedActors);
            sortAfterRatingVideos(sortedActors, sortType);
        }

        if (criteria.equals(AWARDS)) {
            filterActorsAwards(actors, filters, sortedActors);
            sortAfterAwards(sortedActors, sortType);
        }

        if (criteria.equals(DESCRIPTION)) {
            filterAfterDescription(actors, filters, sortedActors);
            sortAfterDescription(sortedActors, sortType);
        }

        return sortedActors;
    }

    /**
     * - the method filters all actors in the database by the average rating of all videos the
     * actor has played in
     * - it calculates the average rating by iterating through the filmography of the actor, finding
     * the video in the database, adding to ratingVideos the rating of the all videos which have
     * been rated at least one time and then dividing it to the total number of videos found
     * - if the final rate is a non zero value, then it is added to the filteredActors
     * @param actors all actors in the database
     * @param videos all videos in the database
     * @param filteredActors a list returned with all filtered actors
     */

    private static void filterActorsRating(final ArrayList<Actor> actors,
                                           final ArrayList<Video> videos,
                                           final ArrayList<GeneralObject> filteredActors) {

        for (Actor actor : actors) {
            double ratingVideos = 0;
            int numberVideosRated = 0;

            for (String videoTitle : actor.getFilmography()) {
                Video video =  GetObject.getVideo(videos, videoTitle);

                if (video != null && video.getAverageRating() != 0) {
                    ratingVideos += video.getAverageRating();
                    numberVideosRated++;
                }
            }

            if (numberVideosRated != 0) {
                actor.updateRatingVideos(ratingVideos / (double) numberVideosRated);
                filteredActors.add(actor);
            }
        }
    }

    /**
     * - the method filters all actors in the database by their career description
     * - for every actor, every keyword required by the filters is verified if it matches the
     * description
     *      - if there exists one keyword which is not included in the description, it goes on
     *      with the iteration through the actors
     *      - if all keyword are included in the description, the actor is added to the
     *      filteredActors
     * @param actors all actors in the database
     * @param filters the filters required in the input
     * @param filteredActors a list returned with all filtered actors
     */

    private static void filterAfterDescription(final ArrayList<Actor> actors,
                                               final List<List<String>> filters,
                                               final ArrayList<GeneralObject> filteredActors) {
        List<String> keyWords = filters.get(KEYWORDS_IN_FILTERS);

        for (Actor actor : actors) {
            boolean containAllWords = true;

            for (String keyword : keyWords) {
                Pattern pattern = Pattern.compile("[ ,!.'(-]" + keyword + "[ ,!.')-]",
                                                    Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(actor.getCareerDescription());

                if (!matcher.find()) {
                    containAllWords = false;
                    break;
                }
            }

            if (containAllWords) {
                filteredActors.add(actor);
            }
        }
    }

    /**
     * - the method filters all actors in the database by their awards
     * - for every actor, every award given by the filters is verified if it matches with the
     * actor's awards
     *      - if there exists one award which is not included in the awards list of the actor,
     *      it goes on with the iteration through the actors
     *      - if all awards are included in the awards list, the actor is added to the
     *      filteredActors
     * @param actors all actors in the database
     * @param filters the filters required in the input
     * @param filteredActors a list returned with all filtered actors
     */

    private static void filterActorsAwards(final ArrayList<Actor> actors,
                                           final List<List<String>> filters,
                                           final ArrayList<GeneralObject> filteredActors) {

        for (Actor actor : actors) {
            boolean containAllAwards = true;

            for (String award : filters.get(AWARDS_IN_FILTERS)) {
                if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                    containAllAwards = false;
                    break;
                }
            }

            if (containAllAwards) {
                filteredActors.add(actor);
            }
        }
    }

    /**
     * sort the list of actors given by parameter by the rating of all videos he played in,
     * depending on the sort type given (ascending, descending)
     */

    private static void sortAfterRatingVideos(final ArrayList<GeneralObject> actorsSorted,
                                              final String sortType) {
        if (sortType.equals(DESCENDING)) {
            actorsSorted.sort((actor1, actor2) ->
                    compareAfterRating((Actor) actor2, (Actor) actor1));
        } else if (sortType.equals(ASCENDING)) {
            actorsSorted.sort((actor1, actor2) ->
                    compareAfterRating((Actor) actor1, (Actor) actor2));
        }
    }

    /**
     * compare two actors by their rating
     * - if the rating is equal, the second criterion is their name (also the outMethod)
     */

    private static int compareAfterRating(final Actor actor1, final Actor actor2) {
        double rating1 = actor1.getRatingVideos();
        double rating2 = actor2.getRatingVideos();

        if (Double.compare(rating1, rating2) == 0) {
            return actor1.getOutMethod().compareTo(actor2.getOutMethod());
        }

        return Double.compare(rating1, rating2);
    }

    /**
     * sort the list of actors given by parameter by their total number of awards,
     * depending on the sort type given (ascending, descending)
     */

    private static void sortAfterAwards(final ArrayList<GeneralObject> actorsSorted,
                                        final String sortType) {
        if (sortType.equals(DESCENDING)) {
            actorsSorted.sort((actor1, actor2) ->
                    compareAfterAwards((Actor) actor2, (Actor) actor1));
        } else if (sortType.equals(ASCENDING)) {
            actorsSorted.sort((actor1, actor2) ->
                    compareAfterAwards((Actor) actor1, (Actor) actor2));
        }
    }

    /**
     * compare two actors by their total number of awards
     * - if the number is equal, the second criterion is their name (also the outMethod)
     */

    private static int compareAfterAwards(final Actor actor1, final Actor actor2) {
        int numberAwards1 = actor1.getTotalAwards();
        int numberAwards2 = actor2.getTotalAwards();

            if (numberAwards1 == numberAwards2) {
                return actor1.getName().compareTo(actor2.getName());
            }

        return numberAwards1 - numberAwards2;
    }

    /**
     * sort the list of actors given by parameter by their name (it is used in the query
     * DESCRIPTION)
     */

    private static void sortAfterDescription(final ArrayList<GeneralObject> sortedActors,
                                             final String sortType) {
        if (sortType.equals(DESCENDING)) {
            sortedActors.sort((actor1, actor2) ->
                    actor2.getOutMethod().compareTo(actor1.getOutMethod()));
        } else if (sortType.equals(ASCENDING)) {
            sortedActors.sort(Comparator.comparing(GeneralObject::getOutMethod));
        }
    }

}
