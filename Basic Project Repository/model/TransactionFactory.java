// specify the package
package model;

// system imports
import java.util.Vector;
import javax.swing.JFrame;

// project imports

/** The class containing the TransactionFactory for the ATM application */
//==============================================================
public class TransactionFactory
{

	/**
	 *
	 */
	//----------------------------------------------------------
	public static Transaction createTransaction(String transType)
			throws Exception
	{
		Transaction retValue;

		switch (transType) {
			case "UpdateScout":
				retValue = new UpdateScoutTransaction();
				break;
			case "RemoveScout":
				retValue = new RemoveScoutTransaction();
				break;
			case "RemoveTree":
				retValue = new RemoveTreeTransaction();
				break;
			default: retValue = null;
		}

		return retValue;
	}
}