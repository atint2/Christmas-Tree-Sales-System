// specify the package
package model;

// system imports

import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/**
 * The class containing the TreeLotCoordinator  for the TREE_SALES application
 */
//==============================================================
public class TreeLotCoordinator implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
    // For Impresario
    private Properties dependencies;
    private ModelRegistry myRegistry;

    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    private String transactionErrorMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public TreeLotCoordinator() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // Create the Registry object
        myRegistry = new ModelRegistry("TreeLotCoordinator");
        if (myRegistry == null) {
            new Event(Event.getLeafLevelClassName(this), "TreeLotCoordinator",
                    "Could not instantiate Registry", Event.ERROR);
        }

        // Set dependencies
        setDependencies();

        // Set up the initial view
        createAndShowTransactionChoiceView();
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("StartShift", "TransactionError");
        dependencies.setProperty("EndShift", "TransactionError");
        dependencies.setProperty("AddScout", "TransactionError");
        dependencies.setProperty("UpdateScout", "TransactionError");
        dependencies.setProperty("RemoveScout", "TransactionError");
        dependencies.setProperty("AddTreeType", "TransactionError");
        dependencies.setProperty("AddTree", "TransactionError");
        dependencies.setProperty("UpdateTreeType", "TransactionError");
        dependencies.setProperty("UpdateTree", "TransactionError");
        dependencies.setProperty("RemoveTree", "TransactionError");
        dependencies.setProperty("SellTree", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * Method called from client to get the value of a particular field
     * held by the objects encapsulated by this object
     *
     * @return Value associated with the field
     */
    //----------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TransactionError")) {
            return transactionErrorMessage;
        } else
            return "";
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("StartShift") || key.equals("EndShift") || key.equals("AddScout")
                || key.equals("UpdateScout") || key.equals("RemoveScout") || key.equals("AddTreeType")
                || key.equals("AddTree") || key.equals("UpdateTreeType") || key.equals("UpdateTree")
                || key.equals("RemoveTree") || key.equals("SellTree")) {
            String transType = key;
            doTransaction(transType);
        }
        myRegistry.updateSubscribers(key, this);

    }

    /**
     * Called via the IView relationship
     */
    //----------------------------------------------------------
    public void updateState(String key, Object value) {
        // DEBUG System.out.println("Teller.updateState: key: " + key);

        stateChangeRequest(key, value);
    }

    /**
     * Create a Transaction depending on the Transaction type
     */
    //----------------------------------------------------------
    public void doTransaction(String transactionType) {
        try {
            Transaction trans = TransactionFactory.createTransaction(
                    transactionType);

            trans.subscribe("CancelTransaction", this);
            trans.stateChangeRequest("DoYourJob", "");
        } catch (Exception ex) {
            transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
            new Event(Event.getLeafLevelClassName(this), "createTransaction",
                    "Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
                    Event.ERROR);
        }
    }

    //----------------------------------------------------------
    private void createAndShowTransactionChoiceView() {
        Scene currentScene = (Scene) myViews.get("TransactionChoiceView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("TransactionChoiceView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("TransactionChoiceView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }

    /**
     * Register objects to receive state updates.
     */
    //----------------------------------------------------------
    public void subscribe(String key, IView subscriber) {
        // DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
        // forward to our registry
        myRegistry.subscribe(key, subscriber);
    }

    /**
     * Unregister previously registered objects.
     */
    //----------------------------------------------------------
    public void unSubscribe(String key, IView subscriber) {
        // DEBUG: System.out.println("Cager.unSubscribe");
        // forward to our registry
        myRegistry.unSubscribe(key, subscriber);
    }

    //-----------------------------------------------------------------------------
    public void swapToView(Scene newScene) {


        if (newScene == null) {
            System.out.println("TreeLotCoordinator.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();


        //Place in center
        WindowPosition.placeCenter(myStage);

    }

}
