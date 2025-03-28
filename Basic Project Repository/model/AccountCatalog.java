// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

/** The class containing the AccountCatalog for the ATM application */
//==============================================================
public class AccountCatalog  extends EntityBase implements IView
{
	private static final String myTableName = "Account";

	private Vector accountIDs;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public AccountCatalog( AccountHolder cust) throws
		Exception
	{
		super(myTableName);

		if (cust == null)
		{
			new Event(Event.getLeafLevelClassName(this), "<init>",
				"Missing account holder information", Event.FATAL);
			throw new Exception
				("UNEXPECTED ERROR: AccountCatalog.<init>: account holder information is null");
		}

		String accountHolderId = (String)cust.getState("ID");

		if (accountHolderId == null)
		{
			new Event(Event.getLeafLevelClassName(this), "<init>",
				"Data corrupted: Account Holder has no id in database", Event.FATAL);
			throw new Exception
			 ("UNEXPECTED ERROR: AccountCatalog.<init>: Data corrupted: account holder has no id in repository");
		}

		String query = "SELECT AccountNumber FROM " + myTableName + " WHERE (OwnerID = " + accountHolderId + ")";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			accountIDs = new Vector();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextAccountData = (Properties)allDataRetrieved.elementAt(cnt);

				String accountID = nextAccountData.getProperty("AccountNumber");

				if (accountID != null)
				{
					accountIDs.addElement(accountID);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No accounts for customer : "
				+ accountHolderId + ". Name : " + cust.getState("Name"));
		}

	}

	/**
	 *
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("AccountNumberList"))
			return accountIDs;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// Class is invariant, so this method does not change any attributes

		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
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
