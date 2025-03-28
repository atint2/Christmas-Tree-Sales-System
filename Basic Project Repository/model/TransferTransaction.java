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

/** The class containing the TransferTransaction for the ATM application */
//==============================================================
public class TransferTransaction extends Transaction
{
	private Account source;
	private Account dest;
	private String transferAmount;
	// GUI Components

	private String transactionErrorMessage = "";
	private String accountUpdateStatusMessage = "";

	/**
	 * Constructor for this class.
	 *
	 *
	 */
	//----------------------------------------------------------
	public TransferTransaction(AccountHolder cust)
		throws Exception
	{
		super(cust);
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("DoTransfer", "TransactionError");
		dependencies.setProperty("CancelTransfer", "CancelTransaction");
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

		String sourceAccountNumber = props.getProperty("SourceAccountNumber");
		String destAccountNumber = props.getProperty("DestAccountNumber");
		String amount = props.getProperty("Amount");

		// DEBUG System.out.println("here: sa: "+ sourceAccountNumber + "; da: " + destAccountNumber + "; amt: " +  amount);
		transferAmount = amount;

		try
		{
			source = createAccount(sourceAccountNumber);
			
			boolean isOwner = source.verifyOwnership(myCust);
			if (isOwner == false)
			{
				transactionErrorMessage = "ERROR: Transfer Transaction: Not owner of selected source account!!";
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Failed to verify ownership of source account number : " + sourceAccountNumber + ".",
					Event.ERROR);
			}
			else
			{
				dest= createAccount(destAccountNumber);

				isOwner = dest.verifyOwnership(myCust);
				if (isOwner == false)
				{
					transactionErrorMessage = "ERROR: Transfer Transaction: Not owner of selected dest account!!";
					new Event(Event.getLeafLevelClassName(this), "processTransaction",
						"Failed to verify ownership of dest account number : " + destAccountNumber + ".",
						Event.ERROR);
				}
				else
				{
					boolean ok = source.checkBalance(amount);

					if (ok == true)
					{
						source.debit(amount);
						dest.credit(amount);

						source.update();
						accountUpdateStatusMessage = (String)source.getState("UpdateStatusMessage");
						transactionErrorMessage = accountUpdateStatusMessage;
						
						if ((transactionErrorMessage != null) && (transactionErrorMessage.startsWith("Error") == false))
						{
							dest.update();
							accountUpdateStatusMessage = (String)dest.getState("UpdateStatusMessage");
							transactionErrorMessage = accountUpdateStatusMessage;
						}
						
						createAndShowReceiptView();
					}
					else
					{
						transactionErrorMessage = "Not enough money in account to Transfer $ " + amount;
					}
				}


			}
		}
		catch (InvalidPrimaryKeyException ex)
		{
			transactionErrorMessage = "ACCOUNT FAILURE: Contact bank immediately!!";
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
				"Failed to create account for either number : " + sourceAccountNumber + " or " + destAccountNumber +
					". Reason: " + ex.toString(),
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
		if (key.equals("UpdateStatusMessage") == true)
		{
			return accountUpdateStatusMessage;
		}
		else
		if (key.equals("AccountNumberList") == true)
		{
			return myAccountIDs;
		}
		else
		if (key.equals("SourceAccount") == true)
		{
			return source;
		}
		else
		if (key.equals("DestAccount") == true)
		{
			return dest;
		}
		else
		if (key.equals("TransferAmount") == true)
		{
			return transferAmount;
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
		if (key.equals("DoTransfer") == true)
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

		Scene currentScene = myViews.get("TransferTransactionView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("TransferTransactionView", this);
			currentScene = new Scene(newView);
			myViews.put("TransferTransactionView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}

	//------------------------------------------------------
	protected void createAndShowReceiptView()
	{
		View newView = ViewFactory.createView("TransferReceipt", this);
		Scene newScene = new Scene(newView);

		myViews.put("TransferReceipt", newScene);

		// make the view visible by installing it into the stage
		swapToView(newScene);
	}

}

