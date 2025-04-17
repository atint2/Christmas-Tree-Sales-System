// specify the package
package model;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

public class UpdateScoutTransaction extends Transaction {

    protected UpdateScoutTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {

    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("ScoutSearchView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ScoutSearchView", this);
            currentScene = new Scene(newView);
            myViews.put("ScoutSearchView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob"))
        {
            doYourJob();
        }
    }
}
