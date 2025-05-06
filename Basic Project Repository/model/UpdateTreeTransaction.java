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

public class UpdateTreeTransaction extends Transaction {
    private TreeCollection trees;
    private Tree selectedTree;

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";

    protected UpdateTreeTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelTreeSearch", "CancelTransaction");
        dependencies.setProperty("TreeInfoEntered", "TransactionError");
        dependencies.setProperty("CancelTreeList", "CancelTransaction");
        dependencies.setProperty("UpdateTreeInfo", "UpdateScout");
        dependencies.setProperty("CancelTreeUpdate", "CancelTransaction");
        dependencies.setProperty("Done", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("TreeSearchView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("TreeSearchView", this);
            currentScene = new Scene(newView);
            myViews.put("TreeSearchView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }

    public Object getState(String key) {
        if (key.equals("TreeList")) {
            return trees;
        } else if (key.equals("SelectedTree")) {
            return selectedTree;
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
        } else if (key.equals("TreeInfoEntered")) {
            Properties scoutInfo = (Properties) value;
            try {
                trees = new TreeCollection(scoutInfo);
                createAndShowScoutListView();
            } catch (Exception ex) {
                transactionErrorMessage = "Error getting scout list";
            }
        } else if (key.equals("TreeSelected")) {
            String scoutID = (String) value;
            try {
                selectedTree = trees.retrieve(scoutID);
                createAndShowUpdateScoutView();
            } catch (Exception ex) {
                transactionErrorMessage = "Scout ID not in database";
            }
        } else if (key.equals("UpdateTreeInfo")) {
            updateScoutInDatabase((Properties) value);
        } else if (key.equals("CancelTreeUpdate")) {
            createAndShowScoutListView();
        }
        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    private void updateScoutInDatabase(Properties props) {
        if (selectedTree != null) {
            // Make sure we preserve the Scout ID
            props.setProperty("Barcode", (String) selectedTree.getState("Barcode"));

            // Update the scout's persistent state with new values
            Enumeration allKeys = props.propertyNames();
            while (allKeys.hasMoreElements()) {
                String nextKey = (String) allKeys.nextElement();
                String nextValue = props.getProperty(nextKey);

                // Update the scout object's state
                if (nextValue != null) {
                    selectedTree.stateChangeRequest(nextKey, nextValue);
                }
            }

            // Save the updated scout to the database
            try {
                selectedTree.save();
                updateStatusMessage = "tree successfully updated in the database!";
            } catch (Exception e) {
                updateStatusMessage = "Error updating tree: " + e.getMessage();
            }
        }
    }

    //------------------------------------------------------
    protected void createAndShowScoutListView() {
        View newView = ViewFactory.createView("TreeCollectionView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the stage
        swapToView(newScene);
    }

    //------------------------------------------------------
    protected void createAndShowUpdateScoutView() {
        // create our new view
        View newView = ViewFactory.createView("UpdateTreeView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
}

