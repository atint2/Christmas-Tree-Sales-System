// specify the package
package model;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the BalanceInquiryTransaction for the ATM application */
//==============================================================
public class BalanceInquiryTransaction extends Transaction
{
	private Account myAccount; // needed for GUI only
	private String balanceAmount; // needed for GUI only

	// GUI Components

	private String transactionErrorMessage = "";

	/**
	 * Constructor for this class.
	 *
	 *
	 */
	//----------------------------------------------------------
	public BalanceInquiryTransaction(AccountHolder cust)
		throws Exception
	{
		super(cust);
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("DoBalanceInquiry", "TransactionError");
		dependencies.setProperty("CancelBalanceInquiry", "CancelTransaction");
		dependencies.setProperty("OK", "CancelTransaction");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props)
	{
		String accountNumber = props.getProperty("AccountNumber");

		try
		{
			myAccount = createAccount(accountNumber);

			boolean isOwner = myAccount.verifyOwnership(myCust);
			if (isOwner == false)
			{
				transactionErrorMessage = "ERROR: BalanceInquiry Transaction: Not owner of selected account!!";
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Failed to verify ownership of account number : " + accountNumber + ".",
					Event.ERROR);
			}
			else
			{
				balanceAmount = (String)myAccount.getState("Balance");

				createAndShowReceiptView();

			}
		}
		catch (InvalidPrimaryKeyException ex)
		{
			transactionErrorMessage = "ACCOUNT FAILURE: Contact bank immediately!!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
				"Failed to create account for number : " + accountNumber + ". Reason: " + ex.toString(),
				Event.ERROR);

		}
	}

	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}
		else
		if (key.equals("AccountNumberList") == true)
		{
			return myAccountIDs;
		}
		else
		if (key.equals("Account") == true)
		{
			return myAccount;
		}
		else
		if (key.equals("BalanceAmount") == true)
		{
			return balanceAmount;
		}
		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		if (key.equals("DoYourJob") == true)
		{
			doYourJob();
		}
		else
		if (key.equals("DoBalanceInquiry") == true)
		{
			processTransaction((Properties)value);
		}

		myRegistry.updateSubscribers(key, this);
	}

	/**
	 * Create the view of this class. And then the super-class calls
	 * swapToView() to display the view in the stage
	 */
	//------------------------------------------------------
	protected Scene createView()
	{

		Scene currentScene = myViews.get("BalanceInquiryTransactionView");

		if (currentScene == null)
		{
			// create our new view
			View newView = ViewFactory.createView("BalanceInquiryTransactionView", this);
			currentScene = new Scene(newView);
			myViews.put("BalanceInquiryTransactionView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}

	//------------------------------------------------------
	protected  void createAndShowReceiptView()
	{
		// create our new view
		View newView = ViewFactory.createView("BalanceInquiryReceipt", this);
		Scene newScene = new Scene(newView);

		myViews.put("BalanceInquiryReceiptView", newScene);

		// make the view visible by installing it into the frame
		swapToView(newScene);
	}

}

