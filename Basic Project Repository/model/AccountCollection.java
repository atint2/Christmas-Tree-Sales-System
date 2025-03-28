// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the AccountCollection for the ATM application */
//==============================================================
public class AccountCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Account";

	private Vector<Account> accounts;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public AccountCollection( AccountHolder cust) throws
		Exception
	{
		super(myTableName);

		if (cust == null)
		{
			new Event(Event.getLeafLevelClassName(this), "<init>",
				"Missing account holder information", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: AccountCollection.<init>: account holder information is null");
		}

		String accountHolderId = (String)cust.getState("ID");

		if (accountHolderId == null)
		{
			new Event(Event.getLeafLevelClassName(this), "<init>",
				"Data corrupted: Account Holder has no id in database", Event.FATAL);
			throw new Exception
			 ("UNEXPECTED ERROR: AccountCollection.<init>: Data corrupted: account holder has no id in repository");
		}

		String query = "SELECT * FROM " + myTableName + " WHERE (OwnerID = " + accountHolderId + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			accounts = new Vector<Account>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextAccountData = (Properties)allDataRetrieved.elementAt(cnt);

				Account account = new Account(nextAccountData);

				if (account != null)
				{
					addAccount(account);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No accounts for customer : "
				+ accountHolderId + ". Name : " + cust.getState("Name"));
		}

	}

	//----------------------------------------------------------------------------------
	private void addAccount(Account a)
	{
		//accounts.add(a);
		int index = findIndexToAdd(a);
		accounts.insertElementAt(a,index); // To build up a collection sorted on some key
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Account a)
	{
		//users.add(u);
		int low=0;
		int high = accounts.size()-1;
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Account midSession = accounts.elementAt(middle);

			int result = Account.compare(a,midSession);

			if (result ==0)
			{
				return middle;
			}
			else if (result<0)
			{
				high=middle-1;
			}
			else
			{
				low=middle+1;
			}


		}
		return low;
	}


	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Accounts"))
			return accounts;
		else
		if (key.equals("AccountList"))
			return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public Account retrieve(String accountNumber)
	{
		Account retValue = null;
		for (int cnt = 0; cnt < accounts.size(); cnt++)
		{
			Account nextAcct = accounts.elementAt(cnt);
			String nextAccNum = (String)nextAcct.getState("AccountNumber");
			if (nextAccNum.equals(accountNumber) == true)
			{
				retValue = nextAcct;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	//------------------------------------------------------
	protected void createAndShowView()
	{

		Scene localScene = myViews.get("AccountCollectionView");

		if (localScene == null)
		{
				// create our new view
				View newView = ViewFactory.createView("AccountCollectionView", this);
				localScene = new Scene(newView);
				myViews.put("AccountCollectionView", localScene);
		}
		// make the view visible by installing it into the frame
		swapToView(localScene);
		
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}
