// specify the package
package userinterface;

// system imports
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

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

/** The class containing the Transfer Receipt  for the ATM application */
//==============================================================
public class TransferReceipt extends View
{

	// Model
	private	String				todaysDateAndTimeString;

	// GUI controls
	private Text sourceAccountNumber;
	private Text destAccountNumber;
	private Text amountTransferred;
	private Text todaysDateAndTime;
	private Text sourceCurrentBalance;
	private Text destCurrentBalance;

	private Button okButton;

	// constructor for this class
	//----------------------------------------------------------
	public TransferReceipt(IModel trans)
	{
		super(trans, "TransferReceipt");

		Calendar todaysCalendar = Calendar.getInstance();	// creation date and time
	    	Date todaysDateAndTime = todaysCalendar.getTime();

	    	DateFormat theFormatter = DateFormat.getDateTimeInstance();
	    	todaysDateAndTimeString = theFormatter.format(todaysDateAndTime);

	    	// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		getChildren().add(container);
	
		populateFields();
	}


	// Create the Node (HBox) for the title
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

		Text sourceAccountLabel = new Text("FROM Account : ");
		sourceAccountLabel.setWrappingWidth(175);
		sourceAccountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(sourceAccountLabel, 0, 0);

		sourceAccountNumber = new Text("                       ");
		grid.add(sourceAccountNumber, 1, 0);

		Text destAccountLabel = new Text("TO Account : ");
		destAccountLabel.setWrappingWidth(175);
		destAccountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(destAccountLabel, 0, 1);

		destAccountNumber = new Text("                       ");
		grid.add(destAccountNumber, 1, 1);

		Text amountLabel = new Text("Amount Transferred : ");
		amountLabel.setWrappingWidth(175);
		amountLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(amountLabel, 0, 2);

		amountTransferred = new Text("                       ");
		grid.add(amountTransferred, 1, 2);

		Text dateAndTimeLabel = new Text("Date/Time : ");
		dateAndTimeLabel.setWrappingWidth(175);
		dateAndTimeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dateAndTimeLabel, 0, 3);

		todaysDateAndTime = new Text("                       ");
		grid.add(todaysDateAndTime, 1, 3);

		Text sourceCurrentBalanceLabel = new Text("New Balance (FROM account) : ");
		sourceCurrentBalanceLabel.setWrappingWidth(175);
		sourceCurrentBalanceLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(sourceCurrentBalanceLabel, 0, 4);

		sourceCurrentBalance = new Text("                       ");
		grid.add(sourceCurrentBalance, 1, 4);

		Text destCurrentBalanceLabel = new Text("New Balance (TO account) : ");
		destCurrentBalanceLabel.setWrappingWidth(175);
		destCurrentBalanceLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(destCurrentBalanceLabel, 0, 5);

		destCurrentBalance = new Text("                       ");
		grid.add(destCurrentBalance, 1, 5);

		okButton = new Button("OK");
 		okButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
			/**
			 * Process the Cancel button.
			 * The ultimate result of this action is that the transaction will tell the teller to
			 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
			 * It simply tells its model (controller) that the transfer receipt was seen, and leaves it
			 * to the model to decide to tell the teller to do the switch back.
	 		*/
			//----------------------------------------------------------
			myModel.stateChangeRequest("OK", null);   
            	     }
        	});

		HBox btnContainer = new HBox(100);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(okButton);

		vbox.getChildren().add(grid);
		vbox.getChildren().add(btnContainer);

		return vbox;
	}


	//-------------------------------------------------------------
	public void populateFields()
	{
		String sourceAccountID = (String)((IModel)myModel.getState("SourceAccount")).getState("AccountNumber");
		sourceAccountNumber.setText(sourceAccountID);

		String destAccountID = (String)((IModel)myModel.getState("DestAccount")).getState("AccountNumber");
		destAccountNumber.setText(destAccountID);

		todaysDateAndTime.setText(todaysDateAndTimeString);

		String sourceCurrentBalanceString = (String)((IModel)myModel.getState("SourceAccount")).getState("Balance");
		String destCurrentBalanceString = (String)((IModel)myModel.getState("DestAccount")).getState("Balance");

		double sourceCurrentBalanceVal = Double.parseDouble(sourceCurrentBalanceString);
		double destCurrentBalanceVal = Double.parseDouble(destCurrentBalanceString);
		double amountTransferredVal = Double.parseDouble((String)myModel.getState("TransferAmount"));

		DecimalFormat df2 = new DecimalFormat("0.00");
		sourceCurrentBalance.setText("$ " + df2.format(sourceCurrentBalanceVal));
		destCurrentBalance.setText("$ " + df2.format(destCurrentBalanceVal));
		amountTransferred.setText("$ " + df2.format(amountTransferredVal));

	}

	
	/**
	 * Required by interface, but has no role here
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{

	}

}

//---------------------------------------------------------------
//	Revision History:
