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
    private ScoutCollection scouts;
    private Scout selectedScout;

    // GUI Components

    private String transactionErrorMessage = "";
    private String scoutUpdateStatusMessage = "";

    protected UpdateScoutTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelScoutSearch", "CancelTransaction");
        dependencies.setProperty("ScoutInfoEntered", "TransactionError");
        dependencies.setProperty("CancelScoutList", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("ScoutSearchView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("ScoutSearchView", this);
            currentScene = new Scene(newView);
            myViews.put("ScoutSearchView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }

    public Object getState(String key) {
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else if (key.equals("ScoutInfoEntered")) {
            Properties scoutInfo = (Properties)value;
                try {
                    scouts = new ScoutCollection(scoutInfo);
                    System.out.println("About to create and show scout list view");
                    createAndShowScoutListView();
                } catch (Exception ex) {
                    transactionErrorMessage = "Error getting scout list";
                }
        } else if (key.equals("ScoutSelected")) {
            String scoutID = (String)value;
            selectedScout = scouts.retrieve(scoutID);

            createAndShowUpdateScoutView();
        }
        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    protected void createAndShowScoutListView()
    {
        System.out.println("Create and show scout list view");
        View newView = ViewFactory.createView("ScoutCollectionView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the stage
        swapToView(newScene);
    }

    //------------------------------------------------------
    protected void createAndShowUpdateScoutView()
    {
        // create our new view
        View newView = ViewFactory.createView("UpdateScoutView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
}
