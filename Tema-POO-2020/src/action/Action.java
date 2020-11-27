package action;

public interface Action {

    /**
     * execute action
     */

    void execute();

    /**
     * @return message depending on the success or failure of the execution
     */

    String getOutMessage();

    /**
     * @return the id of the action
     */

    int getId();
}
