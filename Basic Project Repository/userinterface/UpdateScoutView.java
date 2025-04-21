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

public class UpdateScoutView extends View {

    // Properties object to hold updated Scout info
    Properties scoutInfo;

    // GUI components
    protected TextField firstName;
    protected TextField middleName;
    protected TextField lastName;
    protected TextField dateOfBirth;
    protected TextField phoneNumber;
    protected TextField email;
    protected TextField troopID;

    protected Button submitButton;
    protected Button doneButton;

    // For showing error message
    protected MessageView statusLog;

    public UpdateScoutView(IModel treeLotCoordinator) {
        super(treeLotCoordinator, "UpdateScoutView");
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
        Text titleText = new Text("       CHRISTMAS TREE SALES SYSTEM          ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
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
        Text prompt = new Text("SCOUT INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setFont(Font.font("Garamond", FontWeight.BOLD, 17));
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text firstNameLabel = new Text(" First Name : ");
        firstNameLabel.setFont(myFont);
        firstNameLabel.setWrappingWidth(150);
        firstNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(firstNameLabel, 0, 1);

        firstName = new TextField();
        grid.add(firstName, 1, 1);

        Text middleNameLabel = new Text(" Middle Name : ");
        middleNameLabel.setFont(myFont);
        middleNameLabel.setWrappingWidth(150);
        middleNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(middleNameLabel, 0, 2);

        middleName = new TextField();
        grid.add(middleName, 1, 2);

        Text lastNameLabel = new Text(" Last Name : ");
        lastNameLabel.setFont(myFont);
        lastNameLabel.setWrappingWidth(150);
        lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lastNameLabel, 0, 3);

        lastName = new TextField();
        grid.add(lastName, 1, 3);

        Text dateOfBirthLabel = new Text(" Date of Birth : ");
        dateOfBirthLabel.setFont(myFont);
        dateOfBirthLabel.setWrappingWidth(150);
        dateOfBirthLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(dateOfBirthLabel, 0, 4);

        dateOfBirth = new TextField();
        grid.add(dateOfBirth, 1, 4);

        Text phoneNumberLabel = new Text(" Phone Number : ");
        phoneNumberLabel.setFont(myFont);
        phoneNumberLabel.setWrappingWidth(150);
        phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(phoneNumberLabel, 0, 5);

        phoneNumber = new TextField();
        grid.add(phoneNumber, 1, 5);

        Text emailLabel = new Text(" Email : ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(emailLabel, 0, 6);

        email = new TextField();
        grid.add(email, 1, 6);

        Text troopIDLabel = new Text(" Troop ID : ");
        troopIDLabel.setFont(myFont);
        troopIDLabel.setWrappingWidth(150);
        troopIDLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(troopIDLabel, 0, 7);

        troopID = new TextField();
        grid.add(troopID, 1, 7);

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
                displayMessage((String)myModel.getState("UpdateStatusMessage"));
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
        if (!firstName.getText().trim().isEmpty())
            scoutInfo.setProperty("FirstName", firstName.getText());

        if (!middleName.getText().trim().isEmpty())
            scoutInfo.setProperty("MiddleName", middleName.getText());

        if (!lastName.getText().trim().isEmpty())
            scoutInfo.setProperty("LastName", lastName.getText());

        if (!dateOfBirth.getText().trim().isEmpty())
            scoutInfo.setProperty("DateOfBirth", dateOfBirth.getText());

        if (!phoneNumber.getText().trim().isEmpty())
            scoutInfo.setProperty("PhoneNumber", phoneNumber.getText());

        if (!email.getText().trim().isEmpty())
            scoutInfo.setProperty("Email", email.getText());

        if (!troopID.getText().trim().isEmpty())
            scoutInfo.setProperty("TroopID", troopID.getText());
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
        Scout scout = (Scout)myModel.getState("Scout");

        if (scout != null) {
            firstName.setText((String)scout.getState("FirstName"));
            middleName.setText((String)scout.getState("MiddleName"));
            lastName.setText((String)scout.getState("LastName"));
            dateOfBirth.setText((String)scout.getState("DateOfBirth"));
            phoneNumber.setText((String)scout.getState("PhoneNumber"));
            email.setText((String)scout.getState("Email"));
            troopID.setText((String)scout.getState("TroopID"));
        }
    }

    // Clear fields
    //-------------------------------------------------------------
    private void clearFields() {
        firstName.clear();
        middleName.clear();
        lastName.clear();
        dateOfBirth.clear();
        phoneNumber.clear();
        email.clear();
        troopID.clear();
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)value);
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
