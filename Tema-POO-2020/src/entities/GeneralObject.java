package entities;

public interface GeneralObject {
    /**
     * @return specific data for the out message depending on the object
     * e.g name for actors, username for users, title for videos
     */
    String getOutMethod();
}
