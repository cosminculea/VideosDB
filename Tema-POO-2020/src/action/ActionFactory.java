package action;

import action.command.Command;
import action.query.Query;
import action.recommendation.Recommendation;
import database.DataBase;
import fileio.ActionInputData;

public final class ActionFactory {

    /**
     * private constructor -> utility class
     */

    private ActionFactory() { }

    /**
     * decide what type of command is going to be executed
     * @param action input data action
     * @param database data base
     * @return action with a specific type
     */

    public static Action getAction(final ActionInputData action, final DataBase database) {
        String type = action.getActionType();

        if (type.equals("command")) {
            return new Command(action, database);
        }

        if (type.equals("query")) {
            return new Query(action, database);
        }

        if (type.equals("recommendation")) {
            return new Recommendation(action, database);
        }

        return null;
    }
}
