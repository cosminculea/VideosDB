package entities;

import entities.user.User;
import entities.video.Video;
import java.util.ArrayList;

public final class GetObject {

    /**
     * private constructor for maintaining the uti
     */
    private GetObject() { }

    /**
     * find a user in the database by the username
     */

    public static User getUser(final ArrayList<User> users, final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    /**
     * find a video in the database by the title
     */

    public static Video getVideo(final ArrayList<Video> videos, final String title) {
        for (Video video : videos) {
            if (video.getTitle().equals(title)) {
                return video;
            }
        }
        return null;
    }
}
