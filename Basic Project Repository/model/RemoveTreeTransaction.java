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

public class RemoveTreeTransaction extends Transaction {
    private TreeCollection trees;
    private Tree selectedTree;

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";

    protected RemoveTreeTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelTreeSearch", "CancelTransaction");
        dependencies.setProperty("TreeInfoEntered", "TransactionError");
        dependencies.setProperty("CancelTreeList", "CancelTransaction");
        dependencies.setProperty("RemoveTree", "RemoveScout");
        dependencies.setProperty("CancelTreeRemove", "CancelTransaction");
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
        } else if (key.equals("TreeSelected")) {
            String barcode = (String) value;
            try {
                selectedTree = trees.retrieve(barcode);
                createAndShowConfirmRemoveTreeView();
            } catch (Exception ex) {
                transactionErrorMessage = "Tree ID not in database";
            }
        } else if (key.equals("RemoveTree")) {
            removeTreeFromDatabase();
        }
        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    private void removeTreeFromDatabase() {
        if (selectedTree != null) {
            // Remove the scout from the database
            try {
                selectedTree.deleteTree();
                updateStatusMessage = "Tree successfully removed from the database!";
            } catch (Exception e) {
                updateStatusMessage = "Error removing tree: " + e.getMessage();
            }
        }
    }

    //------------------------------------------------------
    protected void createAndShowConfirmRemoveTreeView() {
        // create our new view
        View newView = ViewFactory.createView("ConfirmTreeRemovalView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
}