package action.utils;

import entities.GeneralObject;
import java.util.ArrayList;

public final class OutMessage {
    /**
     * private constructor -> utility class
     */
    private OutMessage() { }

    /**
     * create the message for output in format [x1, x2, ..] where x can be actor, user, video
     */

    public static String create(final ArrayList<? extends GeneralObject> objects,
                                final int number,
                                final StringBuilder outMessage) {
        int numberOfObjects = number;
        if (objects.isEmpty()) {
            outMessage.append("[]");
            return outMessage.toString();
        }

        if (objects.size() < number) {
            numberOfObjects = objects.size();
        }

        outMessage.append("[");

        for (int i = 0; i < numberOfObjects - 1; i++) {
            outMessage.append(objects.get(i).getOutMethod());
            outMessage.append(", ");
        }

        outMessage.append(objects.get(numberOfObjects - 1).getOutMethod());
        outMessage.append("]");
        return outMessage.toString();
    }
}
