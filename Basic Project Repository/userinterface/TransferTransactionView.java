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
import java.util.Vector;

// project imports
import impresario.IModel;

/** The class containing the Transfer Transaction View  for the ATM application */
//==============================================================
public class TransferTransactionView extends View
{

	// GUI components
	private ComboBox<String> sourceAccountNumbers;
	private ComboBox<String> destAccountNumbers;
	private TextField amount;
	private Button submitButton;
	private Button cancelButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public TransferTransactionView(IModel trans)
	{
		super(trans, "TransferTransactionView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                                                      "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("TransactionError", this);
	}	

	
	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Brockport Bank ATM ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}


	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        	grid.setAlignment(Pos.CENTER);
       		grid.setHgap(10);
        	grid.setVgap(10);
        	grid.setPadding(new Insets(25, 25, 25, 25));

		Text sourceAccountLabel = new Text("FROM account : ");
		sourceAccountLabel.setWrappingWidth(150);
		sourceAccountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(sourceAccountLabel, 0, 0);

		sourceAccountNumbers = new ComboBox<String>();
		sourceAccountNumbers.setMinSize(100, 20);
		grid.add(sourceAccountNumbers, 1, 0);

		Text destAccountLabel = new Text("TO account     : ");
		destAccountLabel.setWrappingWidth(150);
		destAccountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(destAccountLabel, 0, 1);

		destAccountNumbers = new ComboBox<String>();
		destAccountNumbers.setMinSize(100, 20);
		grid.add(destAccountNumbers, 1, 1);

		Text amountLabel = new Text("Amount : ");
		amountLabel.setWrappingWidth(150);
		amountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(amountLabel, 0, 2);

		amount = new TextField();
		amount.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
			processAction(e);
		     }
		});
		grid.add(amount, 1, 2);

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
			 * It simply tells its model (controller) that the transfer transaction was canceled, and leaves it
			 * to the model to decide to tell the teller to do the switch back.
	 		*/
			//----------------------------------------------------------
       		     	clearErrorMessage();
			myModel.stateChangeRequest("CancelTransfer", null);   
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

	
	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		Vector allAccountIDs = (Vector)myModel.getState("AccountNumberList");

		if (allAccountIDs != null)
		{
			for (int cnt = 0; cnt < allAccountIDs.size(); cnt++)
			{
				String nextAccountID = (String)allAccountIDs.elementAt(cnt);
				sourceAccountNumbers.getItems().add(nextAccountID);
				destAccountNumbers.getItems().add(nextAccountID);
			}

			if (sourceAccountNumbers.getItems().size() > 0)
			{
				sourceAccountNumbers.setValue(
					sourceAccountNumbers.getItems().get(0));
				destAccountNumbers.setValue(
					destAccountNumbers.getItems().get(0));
			}
		}

		amount.setText("");
	}

	// process events generated from our GUI components
	//-------------------------------------------------------------
	public void processAction(Event evt)
	{
		// DEBUG: System.out.println("TransferTransactionView.actionPerformed()");

		clearErrorMessage();
		// do the transfer

		String selectedSourceAccountNumber = sourceAccountNumbers.getValue();
		String selectedDestAccountNumber = destAccountNumbers.getValue();

		if (selectedSourceAccountNumber.equals(selectedDestAccountNumber) == true)
		{
			displayErrorMessage("ERROR: FROM and TO accounts must be different");
			return;
		}

		String amountEntered = amount.getText();

		if ((amountEntered == null) || (amountEntered.length() == 0))
		{
			displayErrorMessage("Please enter an amount to transfer");
		}
		else
		{
			try
			{
				double amountVal = Double.parseDouble(amountEntered);
				if (amountVal <= 0)
				{
					displayErrorMessage("Invalid amount: Please re-enter");
				}
				else
				{
					processAccountnumbersAndAmount(selectedSourceAccountNumber,
							selectedDestAccountNumber, amountEntered);
				}
			}
			catch (Exception ex)
			{
				displayErrorMessage("Error in processing transfer");
			}

		}

	}

	/**
	 * Process account number and amount selected by user.
	 * Action is to pass this info on to the transaction object by
	 * calling the processTransaction method of the transaction.
	 */
	//----------------------------------------------------------
	private void processAccountnumbersAndAmount(String sourceAccountNumber,
		String destAccountNumber, String amount)
	{

		Properties props = new Properties();
		props.setProperty("SourceAccountNumber", sourceAccountNumber);
		props.setProperty("DestAccountNumber", destAccountNumber);
		props.setProperty("Amount", amount);
		myModel.stateChangeRequest("DoTransfer", props);
	}


	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		if (key.equals("TransactionError") == true)
		{
			String val = (String)value;
			displayErrorMessage(val);
		}
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
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}


