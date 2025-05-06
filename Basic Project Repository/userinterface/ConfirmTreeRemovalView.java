package userinterface;

// system imports

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Scout;

public class ConfirmTreeRemovalView extends View {

    // GUI components
    protected Button confirmButton;
    protected Button cancelButton;
    protected Button doneButton;

    // For showing error message
    protected MessageView statusLog;

    public ConfirmTreeRemovalView(IModel treeLotCoordinator) {
        super(treeLotCoordinator, "ConfirmTreeRemovalView");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContents());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        myModel.subscribe("TransactionError", this);
        myModel.subscribe("UpdateStatusMessage", this);
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

        return container;
    }

    // Create the navigation buttons
    //-------------------------------------------------------------
    private VBox createFormContents() {

        // Create container which holds all content
        VBox vbox = new VBox(8);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Create font
        Font myFont = Font.font("Garamond", FontWeight.BOLD, 15);

        // Add TextFields
        Text prompt = new Text("CONFIRM TREE REMOVAL");
        prompt.setWrappingWidth(400);
        prompt.setFont(Font.font("Garamond", FontWeight.BOLD, 17));
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        // Get the selected scout's information
        Scout selectedTree = (Scout) myModel.getState("SelectedTree");
        String barcode = (String) selectedTree.getState("Barcode");
        String treeType = (String) selectedTree.getState("TreeType");

        Text confirmTreeRemovalLabel = new Text("Would you like to remove Tree:");
        confirmTreeRemovalLabel.setFont(myFont);
        confirmTreeRemovalLabel.setWrappingWidth(150);
        confirmTreeRemovalLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(confirmTreeRemovalLabel, 0, 1, 2, 1);

        // Display Tree information
        Text treeInfoLabel = new Text("Barcode: " + barcode + " - " + treeType);
        treeInfoLabel.setFont(myFont);
        treeInfoLabel.setFill(Color.RED);
        treeInfoLabel.setTextAlignment(TextAlignment.CENTER);
        grid.add(treeInfoLabel, 0, 2, 2, 1);

        // Create HBox for buttons
        HBox btnContainer = new HBox(15);
        btnContainer.setAlignment(Pos.CENTER);

        confirmButton = new Button("Confirm Removal");
        confirmButton.setFont(myFont);
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("RemoveTree", null);

                // Display update status message
                displayMessage((String) myModel.getState("UpdateStatusMessage"));
            }
        });

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("CancelTreeRemove", null);
            }
        });

        doneButton = new Button("Done");
        doneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearErrorMessage();
                myModel.stateChangeRequest("Done", null);
            }
        });

        btnContainer.getChildren().addAll(confirmButton, cancelButton, doneButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String) value);
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
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }
}