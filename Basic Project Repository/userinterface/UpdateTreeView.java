package userinterface;

// system imports

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Scout;
import model.ScoutCollection;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class UpdateTreeView extends View {

    // Properties object to hold updated Scout info
    Properties scoutInfo;

    // GUI components
    protected TextField treeType;
    protected TextField notes;
    protected TextField status;

    protected Button submitButton;
    protected Button doneButton;

    // For showing error message
    protected MessageView statusLog;

    public UpdateTreeView(IModel treeLotCoordinator) {
        super(treeLotCoordinator, "UpdateTreeView");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContents());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();

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
        Text prompt = new Text("UPDATE SCOUT INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setFont(Font.font("Garamond", FontWeight.BOLD, 17));
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text middleNameLabel = new Text(" tree type : ");
        middleNameLabel.setFont(myFont);
        middleNameLabel.setWrappingWidth(150);
        middleNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(middleNameLabel, 0, 2);

        treeType = new TextField();
        grid.add(treeType, 1, 2);

        Text lastNameLabel = new Text(" Notes : ");
        lastNameLabel.setFont(myFont);
        lastNameLabel.setWrappingWidth(150);
        lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lastNameLabel, 0, 3);

        notes = new TextField();
        grid.add(notes, 1, 3);

        Text dateOfBirthLabel = new Text(" Status : ");
        dateOfBirthLabel.setFont(myFont);
        dateOfBirthLabel.setWrappingWidth(150);
        dateOfBirthLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateOfBirthLabel, 0, 4);

        status = new TextField();
        grid.add(status, 1, 4);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.BOTTOM_RIGHT);

        submitButton = new Button("Submit");
        submitButton.setFont(myFont);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processScoutInfo();
                myModel.stateChangeRequest("UpdateScoutInfo", scoutInfo);

                // Clear fields after submission
                clearFields();

                // Display update status message
                displayMessage((String) myModel.getState("UpdateStatusMessage"));
            }
        });

        doneButton = new Button("Done");
        doneButton.setFont(myFont);
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("Done", null);
            }
        });

        doneCont.getChildren().add(submitButton);
        doneCont.getChildren().add(doneButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }

    // Process scout info entered into Properties object scoutInfo
    public void processScoutInfo() {
        scoutInfo = new Properties();
        if (!treeType.getText().trim().isEmpty())
            scoutInfo.setProperty("FirstName", treeType.getText());

        if (!notes.getText().trim().isEmpty())
            scoutInfo.setProperty("MiddleName", notes.getText());

        if (!status.getText().trim().isEmpty())
            scoutInfo.setProperty("LastName", status.getText());
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    // Populate fields with current scout data
    //-------------------------------------------------------------
    public void populateFields() {
        Scout scout = (Scout) myModel.getState("Scout");

        if (scout != null) {
            treeType.setText((String) scout.getState("TreeType"));
            notes.setText((String) scout.getState("Notes"));
            status.setText((String) scout.getState("Status"));
        }
    }

    // Clear fields
    //-------------------------------------------------------------
    private void clearFields() {
        treeType.clear();
        notes.clear();
        status.clear();
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
