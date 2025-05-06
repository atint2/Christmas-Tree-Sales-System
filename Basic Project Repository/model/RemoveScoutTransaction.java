// specify the package
package model;

// system imports

import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

public class RemoveScoutTransaction extends Transaction {
    private ScoutCollection scouts;
    private Scout selectedScout;

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";

    protected RemoveScoutTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelScoutSearch", "CancelTransaction");
        dependencies.setProperty("ScoutInfoEntered", "TransactionError");
        dependencies.setProperty("CancelScoutList", "CancelTransaction");
        dependencies.setProperty("RemoveScout", "RemoveScout");
        dependencies.setProperty("CancelScoutRemove", "CancelTransaction");
        dependencies.setProperty("Done", "CancelTransaction");

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
        if (key.equals("ScoutList")) {
            return scouts;
        } else if (key.equals("SelectedScout")) {
            return selectedScout;
        } else if (key.equals("TransactionError")) {
            return transactionErrorMessage;
        } else if (key.equals("UpdateStatusMessage")) {
            return updateStatusMessage;
        }
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else if (key.equals("ScoutInfoEntered")) {
            Properties scoutInfo = (Properties) value;
            try {
                scouts = new ScoutCollection(scoutInfo);
                createAndShowScoutListView();
            } catch (Exception ex) {
                transactionErrorMessage = "Error getting scout list";
            }
        } else if (key.equals("ScoutSelected")) {
            String scoutID = (String) value;
            try {
                selectedScout = scouts.retrieve(scoutID);
                createAndShowConfirmRemoveScoutView();
            } catch (Exception ex) {
                transactionErrorMessage = "Scout ID not in database";
            }
        } else if (key.equals("RemoveScout")) {
            removeScoutFromDatabase();
        } else if (key.equals("CancelScoutRemove")) {
            createAndShowScoutListView();
        }
        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    private void removeScoutFromDatabase() {
        if (selectedScout != null) {
            // Remove the scout from the database
            try {
                selectedScout.deleteScout();
                updateStatusMessage = "Scout successfully removed from the database!";
            } catch (Exception e) {
                updateStatusMessage = "Error removing scout: " + e.getMessage();
            }
        }
    }

    //------------------------------------------------------
    protected void createAndShowScoutListView() {
        View newView = ViewFactory.createView("ScoutCollectionView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the stage
        swapToView(newScene);
    }

    //------------------------------------------------------
    protected void createAndShowConfirmRemoveScoutView() {
        // create our new view
        View newView = ViewFactory.createView("ConfirmScoutRemovalView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
}
