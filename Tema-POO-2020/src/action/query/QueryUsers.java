package action.query;

import action.utils.OutMessage;
import entities.GeneralObject;
import entities.user.User;
import java.util.ArrayList;

public final class QueryUsers {
    public static final String DESCENDING = "desc";
    public static final String ASCENDING = "asc";

    /**
     * private constructor -> utility class
     */

    private QueryUsers() { }

    /**
     * - call getUsersSorted which returns a filtered
     * and sorted list of users depending on the criteria given and the sort type
     * - call method for creating the out message for the query
     * @return out message for the query
     */

    public static String queryMessage(final ArrayList<User> users,
                                      final int number,
                                      final  String sortType) {
        ArrayList<GeneralObject> sortedUsers = getUsersSorted(users, sortType);

        StringBuilder outMessage = new StringBuilder("Query result: ");
        return OutMessage.create(sortedUsers, number, outMessage);
    }

    /**
     * the method filter all users who rated at least one time, add them to an array list
     * and then sort it by the number of ratings they had given
     * @param users all users in the database
     * @param sortType ascending or descending
     * @return the users sorted and filtered
     */

    private static ArrayList<GeneralObject> getUsersSorted(final ArrayList<User> users,
                                                           final String sortType) {
        ArrayList<GeneralObject> sortedUsers = new ArrayList<>(users.size());

        for (User user : users) {
            if (user.getNumberRatings() != 0) {
                sortedUsers.add(user);
            }
        }

        sortAfterRating(sortedUsers, sortType);
        return sortedUsers;
    }

    /**
     * sort the list of users given as parameter, depending on the sort type
     */

    private static void sortAfterRating(final ArrayList<GeneralObject> usersSorted,
                                        final String sortType) {
        if (sortType.equals(DESCENDING)) {
            usersSorted.sort((user1, user2) -> compareRating((User) user2, (User) user1));
        } else if (sortType.equals(ASCENDING)) {
            usersSorted.sort((user1, user2) -> compareRating((User) user1, (User) user2));
        }
    }

    /**
     * compare two users by their number of ratings
     * - if they have the same number of ratings, their username is compared
     */

    private static int compareRating(final User user1, final User user2) {
        double rating1 = user1.getNumberRatings();
        double rating2 = user2.getNumberRatings();

        if (Double.compare(rating1, rating2) == 0) {
            return user1.getOutMethod().compareTo(user2.getOutMethod());
        }

        return Double.compare(rating1, rating2);
    }
}
