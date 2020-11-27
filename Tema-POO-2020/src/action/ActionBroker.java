package action;

import fileio.Writer;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.util.ArrayList;

public class ActionBroker {
    /**
     * list of all actions to be executed
     */

    private final ArrayList<Action> actions;

    /**
     * constructor which initialise the list of actions
     * @param size the size of input actions list
     */

    public ActionBroker(final int size) {
        actions = new ArrayList<>(size);
    }

    /**
     * add a new action to the list in order to be executed
     * @param action to be added
     */

    public void takeAction(final Action action) {
        actions.add(action);
    }

    /**
     * execute all actions in the list and add the output message for each of them in the JSONArray.
     * In the end, the list is cleared.
     * @param arrayResult json array in which the output of all actions are stored
     * @param fileWriter file writer of the message with format id, field, message
     */

    public void placeActions(final JSONArray arrayResult, final Writer fileWriter)
                                                                        throws IOException {
        for (Action action : actions) {
            action.execute();
            //noinspection unchecked
            arrayResult.add(fileWriter.writeFile(action.getId(), "",
                        action.getOutMessage()));
        }

        actions.clear();
    }
}

