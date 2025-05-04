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

public class UpdateScoutTransaction extends Transaction {
    private ScoutCollection scouts;
    private Scout selectedScout;

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";

    protected UpdateScoutTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelScoutSearch", "CancelTransaction");
        dependencies.setProperty("ScoutInfoEntered", "TransactionError");
        dependencies.setProperty("CancelScoutList", "CancelTransaction");
        dependencies.setProperty("UpdateScoutInfo", "UpdateScout");
        dependencies.setProperty("CancelScoutUpdate", "CancelTransaction");
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
            } catch (Exception ex) {
                transactionErrorMessage = "Scout ID not in database";
            }
            createAndShowUpdateScoutView();
        } else if (key.equals("UpdateScoutInfo")) {
            updateScoutInDatabase((Properties) value);
        } else if (key.equals("CancelScoutUpdate")) {
            createAndShowScoutListView();
        }
        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    private void updateScoutInDatabase(Properties props) {
        if (selectedScout != null) {
            // Make sure we preserve the Scout ID
            props.setProperty("ID", (String) selectedScout.getState("ID"));

            // Update the scout's persistent state with new values
            Enumeration allKeys = props.propertyNames();
            while (allKeys.hasMoreElements()) {
                String nextKey = (String) allKeys.nextElement();
                String nextValue = props.getProperty(nextKey);

                // Update the scout object's state
                if (nextValue != null) {
                    selectedScout.stateChangeRequest(nextKey, nextValue);
                }
            }

            // Save the updated scout to the database
            try {
                selectedScout.save();
                updateStatusMessage = "Scout successfully updated in the database!";
            } catch (Exception e) {
                updateStatusMessage = "Error updating scout: " + e.getMessage();
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
    protected void createAndShowUpdateScoutView() {
        // create our new view
        View newView = ViewFactory.createView("UpdateScoutView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
}
