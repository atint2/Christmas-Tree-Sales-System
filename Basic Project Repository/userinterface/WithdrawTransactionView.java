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

/** The class containing the Withdraw Transaction View  for the ATM application */
//==============================================================
public class WithdrawTransactionView extends View
{

	// GUI components
	private ComboBox<String> accountNumbers;
	private TextField amount;
	private Button submitButton;
	private Button cancelButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public WithdrawTransactionView(IModel trans)
	{
		super(trans, "WithdrawTransactionView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                          "));

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

		// data entry fields
		Text accountLabel = new Text("Withdrawal account : ");
		accountLabel.setWrappingWidth(150);
		accountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(accountLabel, 0, 0);

		accountNumbers = new ComboBox<String>();
		accountNumbers.setMinSize(100, 20);
		grid.add(accountNumbers, 1, 0);

		Text amountLabel = new Text("Amount : ");
		amountLabel.setWrappingWidth(150);
		amountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(amountLabel, 0, 1);

		amount = new TextField();
		amount.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
			processAction(e);
		     }
		});
		grid.add(amount, 1, 1);

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
				accountNumbers.getItems().add(nextAccountID);
			}

			if (accountNumbers.getItems().size() > 0)
			{
				accountNumbers.setValue(accountNumbers.getItems().get(0));
			}
		}

		amount.setText("");
	}

	// process events generated from our GUI components
	//-------------------------------------------------------------
	public void processAction(Event evt)
	{
		// DEBUG: System.out.println("WithdrawTransactionView.processAction()");

		clearErrorMessage();

		// do the withdraw

		String selectedAccountNumber = accountNumbers.getValue();

		String amountEntered = amount.getText();

		if ((amountEntered == null) || (amountEntered.length() == 0))
		{
			displayErrorMessage("Please enter an amount to withdraw");
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
					processAccountnumberAndAmount(selectedAccountNumber, amountEntered);
				}
			}
			catch (Exception ex)
			{
				displayErrorMessage("Invalid amount: Please re-enter");
			}

		}
	}


	/**
	 * Process account number and amount selected by user.
	 * Action is to pass this info on to the transaction object
	 */
	//----------------------------------------------------------
	private void processAccountnumberAndAmount(String accountNumber,
		String amount)
	{
		Properties props = new Properties();
		props.setProperty("AccountNumber", accountNumber);
		props.setProperty("Amount", amount);
		myModel.stateChangeRequest("DoWithdraw", props);
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
