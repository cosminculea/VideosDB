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
                                int number,
                                final StringBuilder outMessage) {

        if (objects.isEmpty()) {
            outMessage.append("[]");
            return outMessage.toString();
        }

        if (objects.size() < number) {
            number = objects.size();
        }

        outMessage.append("[");

        for (int i = 0; i < number - 1; i++) {
            outMessage.append(objects.get(i).getOutMethod());
            outMessage.append(", ");
        }

        outMessage.append(objects.get(number - 1).getOutMethod());
        outMessage.append("]");
        return outMessage.toString();
    }
}
