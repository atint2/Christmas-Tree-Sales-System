// specify the package
package userinterface;

// system imports

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * The class containing the Transaction Choice View  for the ATM application
 */
//==============================================================
public class TransactionChoiceView extends View {

    // other private data
    private final int labelWidth = 120;
    private final int labelHeight = 25;

    // GUI components
    private Button startShiftButton;
    private Button endShiftButton;
    private Button addScoutButton;
    private Button updateScoutButton;
    private Button removeScoutButton;
    private Button addTreeTypeButton;
    private Button addTreeButton;
    private Button updateTreeTypeButton;
    private Button updateTreeButton;
    private Button removeTreeButton;
    private Button sellTreeButton;

    private Button doneButton;    // "dismiss" in state diagram

    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public TransactionChoiceView(IModel treeLotCoordinator) {
        super(treeLotCoordinator, "TransactionChoiceView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContents());

        container.getChildren().add(createStatusLog("                    "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("TransactionError", this);
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle() {
        VBox container = new VBox(10);
        Text titleText = new Text("CHRISTMAS TREE SALES SYSTEM");
        titleText.setFont(Font.font("Garamond", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(400);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        Text inquiryText = new Text("What do you wish to do today?");
        inquiryText.setFont(Font.font("Garamond", FontWeight.BOLD, 17));
        inquiryText.setWrappingWidth(400);
        inquiryText.setTextAlignment(TextAlignment.CENTER);
        inquiryText.setFill(Color.BLACK);
        container.getChildren().add(inquiryText);

        return container;
    }


    // Create the navigation buttons
    //-------------------------------------------------------------
    private VBox createFormContents() {

        // Create container which holds all content
        VBox container = new VBox(8);

        // Create font
        Font myFont = Font.font("Garamond", FontWeight.BOLD, 15);

        // Add Buttons
        // Shift container
        // Start Shift Button
        HBox shiftCont = new HBox(10);
        shiftCont.setAlignment(Pos.CENTER);
        startShiftButton = new Button("Start Shift");
        startShiftButton.setFont(myFont);
        startShiftButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("StartShift", null);
            }
        });
        shiftCont.getChildren().add(startShiftButton);

        // End Shift Button
        endShiftButton = new Button("End Shift");
        endShiftButton.setFont(myFont);
        endShiftButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("EndShift", null);
            }
        });
        shiftCont.getChildren().add(endShiftButton);

        container.getChildren().add(shiftCont);

        // Add whitespace
        container.getChildren().add(new Label(" "));

        // Scout container
        // Add Scout Button
        HBox scoutCont = new HBox(10);
        scoutCont.setAlignment(Pos.CENTER);
        addScoutButton = new Button("Add Scout");
        addScoutButton.setFont(myFont);
        addScoutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("AddScout", null);
            }
        });
        scoutCont.getChildren().add(addScoutButton);

        // Update Scout Button
        updateScoutButton = new Button("Update Scout");
        updateScoutButton.setFont(myFont);
        updateScoutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("UpdateScout", null);
            }
        });
        scoutCont.getChildren().add(updateScoutButton);

        // Remove Scout Button
        removeScoutButton = new Button("Remove Scout");
        removeScoutButton.setFont(myFont);
        removeScoutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("RemoveScout", null);
            }
        });
        scoutCont.getChildren().add(removeScoutButton);

        container.getChildren().add(scoutCont);

        // Add whitespace
        container.getChildren().add(new Label(" "));

        // Tree Type container
        // Add Tree Type Button
        HBox treeTypeCont = new HBox(10);
        treeTypeCont.setAlignment(Pos.CENTER);
        addTreeTypeButton = new Button("Add Tree Type");
        addTreeTypeButton.setFont(myFont);
        addTreeTypeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddTreeType", null);
            }
        });
        treeTypeCont.getChildren().add(addTreeTypeButton);

        // Update Tree Type Button
        updateTreeTypeButton = new Button("Update Tree Type");
        updateTreeTypeButton.setFont(myFont);
        updateTreeTypeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("UpdateTreeType", null);
            }
        });
        treeTypeCont.getChildren().add(updateTreeTypeButton);

        container.getChildren().add(treeTypeCont);

        // Add whitespace
        container.getChildren().add(new Label(" "));

        // Tree container
        // Add Tree Button
        HBox treeCont = new HBox(10);
        treeCont.setAlignment(Pos.CENTER);
        addTreeButton = new Button("Add Tree");
        addTreeButton.setFont(myFont);
        addTreeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddTree", null);
            }
        });
        treeCont.getChildren().add(addTreeButton);

        // Update Tree Button
        updateTreeButton = new Button("Update Tree");
        updateTreeButton.setFont(myFont);
        updateTreeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("UpdateTree", null);
            }
        });
        treeCont.getChildren().add(updateTreeButton);

        // Remove Tree Button
        removeTreeButton = new Button("Remove Tree");
        removeTreeButton.setFont(myFont);
        removeTreeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("RemoveTree", null);
            }
        });
        treeCont.getChildren().add(removeTreeButton);

        // Sell Tree Button
        sellTreeButton = new Button("Sell Tree");
        sellTreeButton.setFont(myFont);
        sellTreeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("SellTree", null);
            }
        });
        treeCont.getChildren().add(sellTreeButton);

        container.getChildren().add(treeCont);

        // Add whitespace
        container.getChildren().add(new Label(" "));

        // Done Button
        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneButton = new Button("Done");
        doneButton.setFont(myFont);
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
        doneCont.getChildren().add(doneButton);

        container.getChildren().add(doneCont);

        return container;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {

    }


    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        if (key.equals("TransactionError")) {
            // display the passed text
            displayErrorMessage((String) value);
        }
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }
}


