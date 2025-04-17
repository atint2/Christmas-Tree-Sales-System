// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing the Scout Search View for the 'Update Scout' and 'Remove Scout' functionality of the TreeSalesSystem application */
//==============================================================
public class ScoutSearchView extends View {

    // Properties object to hold Scout search info
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
    protected Button cancelButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class
    //----------------------------------------------------------
    public ScoutSearchView(IModel treeLotCoordinator) {
        super(treeLotCoordinator, "ScoutSearchView");
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
        Text prompt = new Text("SEARCH SCOUTS");
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
                myModel.stateChangeRequest("ScoutInfoEntered", scoutInfo);
            }
        });
        doneCont.getChildren().add(submitButton);

        cancelButton = new Button("Cancel");
        cancelButton.setFont(myFont);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelScoutSearch", null);
            }
        });
        doneCont.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;

    }

    // Process scout info entered into Properties object scoutInfo
    public void processScoutInfo() {
        scoutInfo = new Properties();
        scoutInfo.setProperty("FirstName", firstName.getText());
        scoutInfo.setProperty("MiddleName", middleName.getText());
        scoutInfo.setProperty("LastName", lastName.getText());
        scoutInfo.setProperty("DateOfBirth", dateOfBirth.getText());
        scoutInfo.setProperty("PhoneNumber", phoneNumber.getText());
        scoutInfo.setProperty("Email", email.getText());
        scoutInfo.setProperty("TroopID", troopID.getText());
        System.out.println(scoutInfo);
    }
    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
        firstName.setText("Jane");
        middleName.setText("Elizabeth");
        lastName.setText("Doe");
        dateOfBirth.setText("2004-07-27");
        phoneNumber.setText("585-395-2222");
        email.setText("jdoe1@brockport.edu");
        troopID.setText("112");
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

}
