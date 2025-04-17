package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.EntityBase;

import java.util.Properties;
import java.util.Vector;

public class EnterTreeInfoView extends View {
    private TextField barcode;
    private TextField treeType;
    private TextField notes;
    private TextField status;
    private Button submitButton;
    private Button cancelButton;

    private MessageView statusLog;

    public EnterTreeInfoView(IModel model) {
        super(model, "EnterTreeInfoView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // how do you add white space?
        container.getChildren().add(new Label(" "));

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContents());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("TransactionError", this);

    }

    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Tree Information ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }
    private VBox createFormContents()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // data entry fields
        Text barcodeLabel = new Text("Barcode : ");
        barcodeLabel.setWrappingWidth(150);
        barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(barcodeLabel, 0, 0);

        barcode = new TextField();
        barcode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        });
        grid.add(barcode, 1, 0);

        // data entry fields
        Text treeTypeLabel = new Text("Tree Type : ");
        treeTypeLabel.setWrappingWidth(150);
        treeTypeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(treeTypeLabel, 0, 1);

        treeType = new TextField();
        treeType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        });
        grid.add(treeType, 1, 1);

        // data entry fields
        Text notesLabel = new Text("Notes : ");
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 2);

        notes = new TextField();
        notes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        });
        grid.add(notes, 1, 2);

        // data entry fields
        Text statusLabel = new Text("Status : ");
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(statusLabel, 0, 3);

        status = new TextField();
        status.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        });
        grid.add(status, 1, 3);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the deposit
                processAction(e);
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the withdraw transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelWithdraw", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }
    public void populateFields()
    {
        barcode.setText("");
        treeType.setText("");
        notes.setText("");
        status.setText("");
    }
    public void processAction(Event evt)
    {
        // DEBUG: System.out.println("WithdrawTransactionView.processAction()");

        clearErrorMessage();

        String barcode_text = barcode.getText();
        String treeType_text = treeType.getText();
        String notes_text = notes.getText();
        String status_text = status.getText();

        processTree(barcode_text, treeType_text, notes_text, status_text);

        // do the withdraw

//        String selectedAccountNumber = accountNumbers.getValue();
//
//        String amountEntered = amount.getText();
//
//        if ((amountEntered == null) || (amountEntered.length() == 0))
//        {
//            displayErrorMessage("Please enter an amount to withdraw");
//        }
//        else
//        {
//            try
//            {
//                double amountVal = Double.parseDouble(amountEntered);
//                if (amountVal <= 0)
//                {
//                    displayErrorMessage("Invalid amount: Please re-enter");
//                }
//                else
//                {
//                    processTree(selectedAccountNumber, amountEntered);
//                }
//            }
//            catch (Exception ex)
//            {
//                displayErrorMessage("Invalid amount: Please re-enter");
//            }
//
//        }
    }
    private void processTree(String barcode_text, String treeType_text, String notes_text, String status_text)
    {
        Properties props = new Properties();
        props.setProperty("Barcode", barcode_text);
        props.setProperty("TreeType", treeType_text);
        props.setProperty("Notes", notes_text);
        props.setProperty("Status", status_text);
        myModel.stateChangeRequest("DoWithdraw", props);
    }
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }
    private MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
    public void updateState(String key, Object value)
    {
        if (key.equals("TransactionError") == true)
        {
            String val = (String)value;
            displayErrorMessage(val);
        }
    }
}
