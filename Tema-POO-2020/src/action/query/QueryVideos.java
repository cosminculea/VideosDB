package action.query;

import action.utils.OutMessage;
import entities.GeneralObject;
import entities.video.Video;
import java.util.ArrayList;
import java.util.List;

public final class QueryVideos {
    public static final String FAVOURITE = "favorite";
    public static final String RATINGS = "ratings";
    public static final String LONGEST = "longest";
    public static final String MOST_VIEWED = "most_viewed";
    public static final String DESCENDING = "desc";
    public static final String ASCENDING = "asc";
    public static final int YEAR_INDEX = 0;
    public static final int GENRE_INDEX = 1;

    private QueryVideos() { }

    /**
     * - call getVideosSorted which returns a filtered
     * and sorted list of videos depending on the criteria given and the sort type
     * - call method for creating the out message for the query
     * @return out message for the query
     */

    public static String queryMessage(final List<List<String>> filters,
                                      final String sortType,
                                      final ArrayList<Video> videos,
                                      final int number,
                                      final String criteria) {

        ArrayList<GeneralObject> videosSorted = getVideosSorted(filters, sortType,
                                                                videos, criteria);

        StringBuilder outMessage = new StringBuilder("Query result: ");
        return OutMessage.create(videosSorted, number, outMessage);
    }

    /**
     * - iterates through all videos in the data base, and creates a new
     * list only with videos which pass the filters and the zero verification
     * of the particular element of the criteria
     * - the list is sorted with the specific method which indicates the comparison
     * type depending on the criteria
     * @return a sorted and filtered list of videos
     */
    private static ArrayList<GeneralObject> getVideosSorted(final List<List<String>> filters,
                                                            final String sortType,
                                                            final ArrayList<Video> videos,
                                                            final String criteria) {
        ArrayList<GeneralObject> videosToSort = new ArrayList<>(videos.size());

        for (Video video : videos) {
            if (criteria.equals(MOST_VIEWED)) {
                if (video.getNumberViews() == 0) {
                    continue;
                }
            }

            if (criteria.equals(RATINGS)) {
                if (video.getAverageRating() == 0) {
                    continue;
                }
            }

            if (criteria.equals(LONGEST)) {
                if (video.getDuration() == 0) {
                    continue;
                }
            }

            if (criteria.equals(FAVOURITE)) {
                if (video.getNumberFavourites() == 0) {
                    continue;
                }
            }

            if (applyFilters(filters, video)) {
                videosToSort.add(video);
            }
        }

        if (criteria.equals(MOST_VIEWED)) {
            sortAfterViews(videosToSort, sortType);
        }

        if (criteria.equals(RATINGS)) {
            sortAfterRatings(videosToSort, sortType);
        }

        if (criteria.equals(LONGEST)) {
            sortAfterDuration(videosToSort, sortType);
        }

        if (criteria.equals(FAVOURITE)) {
            sortAfterFavourite(videosToSort, sortType);
        }

        return videosToSort;
    }

    /**
     * sort the list by the number of views of the videos
     */

    private static void sortAfterViews(final ArrayList<GeneralObject> videosSorted,
                                       final String sortType) {
        if (sortType.equals(DESCENDING)) {
            videosSorted.sort((video1, video2) -> compareViews((Video) video2, (Video) video1));
        } else if (sortType.equals(ASCENDING)) {
            videosSorted.sort((video1, video2) -> compareViews((Video) video1, (Video) video2));
        }
    }

    /**
     * compare 2 videos by number of views
     * if the number of views are equal the second criterion is title
     */

    private static int compareViews(final Video video1, final Video video2) {
        int views1 = video1.getNumberViews();
        int views2 = video2.getNumberViews();

        if (views1 == views2) {
            return video1.getOutMethod().compareTo(video2.getOutMethod());
        }

        return views1 - views2;
    }

    /**
     * sort the list by the duration of the videos
     */

    private static void sortAfterDuration(final ArrayList<GeneralObject> videosSorted,
                                          final String sortType) {
        if (sortType.equals(DESCENDING)) {
            videosSorted.sort((video1, video2) -> compareDuration((Video) video2, (Video) video1));
        } else if (sortType.equals(ASCENDING)) {
            videosSorted.sort((video1, video2) -> compareDuration((Video) video1, (Video) video2));
        }
    }

    /**
     * compare 2 videos by duration
     * if the duration is the same , the second criterion is title
     */

    private static int compareDuration(final Video video1, final Video video2) {
        int duration1 = video1.getDuration();
        int duration2 = video2.getDuration();

        if (duration1 == duration2) {
            return video1.getOutMethod().compareTo(video2.getOutMethod());
        }

        return duration1 - duration2;
    }

    /**
     * sort the list by the ratings of the videos
     */

    private static void sortAfterRatings(final ArrayList<GeneralObject> videosSorted,
                                         final String sortType) {
        if (sortType.equals(DESCENDING)) {
            videosSorted.sort((video1, video2) -> compareRating((Video) video2, (Video) video1));
        } else if (sortType.equals(ASCENDING)) {
            videosSorted.sort((video1, video2) -> compareRating((Video) video1, (Video) video2));
        }
    }

    /**
     * compare 2 videos by rating
     * if the rating is the equal, the second criterion is title
     */

    private static int compareRating(final Video video1, final Video video2) {
        double rating1 = video1.getAverageRating();
        double rating2 = video2.getAverageRating();

        if (Double.compare(rating1, rating2) == 0) {
            return video1.getOutMethod().compareTo(video2.getOutMethod());
        }

        return Double.compare(rating1, rating2);
    }

    /**
     * sort the list by the number of appearances in the "favourite" lists
     * of the users
     */

    private static void sortAfterFavourite(final ArrayList<GeneralObject> videosSorted,
                                           final String sortType) {
        if (sortType.equals(DESCENDING)) {
            videosSorted.sort((video1, video2) -> compareFavourite((Video) video2, (Video) video1));
        } else if (sortType.equals(ASCENDING)) {
            videosSorted.sort((video1, video2) -> compareFavourite((Video) video1, (Video) video2));
        }

    }

    /**
     * compare 2 videos by the number of appearances in users lists of favourites.
     * if these are equal, the second criterion is the title
     */

    private static int compareFavourite(final Video video1, final Video video2) {
        int numberFavourite1 = video1.getNumberFavourites();
        int numberFavourite2 = video2.getNumberFavourites();

        if (numberFavourite1 == numberFavourite2) {
            return video1.getOutMethod().compareTo(video2.getOutMethod());
        }

        return numberFavourite1 - numberFavourite2;
    }

    /**
     * verify if the given video has one of the years in the filters list
     */

    private static boolean applyFilterYear(final Video video, final List<String> filterYear) {
        for (String year : filterYear) {
            if (year != null && year.equals(String.valueOf(video.getYear()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * verify if the given video has one of the genres in the filters list
     */

    private static boolean applyFilterGenre(final Video video, final List<String> filterGenre) {
        for (String genre : filterGenre) {
            if (genre != null && video.getGenres().contains(genre)) {
                return true;
            }
        }
        return false;
    }

    /**
     * combines both filters of years and genres
     */

    private static boolean applyFilters(final List<List<String>> filters, final Video video) {
        if (filters.get(YEAR_INDEX) != null && verifyAllNull(filters.get(YEAR_INDEX))) {
            if (!applyFilterYear(video, filters.get(YEAR_INDEX))) {
                return false;
            }
        }

        if (filters.get(GENRE_INDEX) != null && verifyAllNull(filters.get(GENRE_INDEX))) {
            return applyFilterGenre(video, filters.get(GENRE_INDEX));
        }
        return true;
    }

    /**
     * verify if the list of filters for year or genre has all elements null
     */

    private static boolean verifyAllNull(final List<String> filters) {
        for (String filter : filters) {
            if (filter != null) {
                return true;
            }
        }

        return false;
    }
}
