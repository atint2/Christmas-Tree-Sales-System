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

public class AddATreeTransaction extends Transaction {
    private TreeCollection trees;
    private Tree selectedTrees;

    // GUI Components

    private String transactionErrorMessage = "";
    private String treeUpdateStatusMessage = "";

    protected AddATreeTransaction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("TreeInfoEntered", "TransactionError");
        dependencies.setProperty("OK", "CancelTransaction");
        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("EnterTreeInfoView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("EnterTreeInfoView", this);
            currentScene = new Scene(newView);
            myViews.put("TreeSearchView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }

    public Object getState(String key) {
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if (key.equals("TreeInfoEntered")) {
            Properties treeInfo = (Properties)value;
            try {
                trees = new TreeCollection(treeInfo);
                System.out.println("adding tree");
                createAndShowAddTreeView();
            } catch (Exception ex) {
                transactionErrorMessage = "Error adding tree";
            }
        }
        myRegistry.updateSubscribers(key, this);
    }

    //------------------------------------------------------
    protected void createAndShowAddTreeView()
    {
        System.out.println("Create and show scout list view");
        View newView = ViewFactory.createView("EnterTreeInfoView", this);
        Scene newScene = new Scene(newView);

        // make the view visible by installing it into the stage
        swapToView(newScene);
    }
}