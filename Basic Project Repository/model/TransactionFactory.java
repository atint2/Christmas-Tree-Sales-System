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
		Transaction retValue = null;

		if (transType.equals("StartShift"))
		{
			retValue = new StartShiftTransaction();
		}
		else
		if (transType.equals("EndShift"))
		{
			retValue = new EndShiftTransaction();
		}
		else
		if (transType.equals("AddScout"))
		{
			retValue = new AddScoutTransaction();
		}
		else
		if (transType.equals("UpdateScout"))
		{
			retValue = new UpdateScoutTransaction();
		}
		else
		if (transType.equals("RemoveScout"))
		{
			retValue = new RemoveScoutTransaction();
		}
		else
		if (transType.equals("AddTreeType"))
		{
			retValue = new AddTreeTypeTransaction();
		}
		else
		if (transType.equals("AddTree"))
		{
			retValue = new AddTreeTransaction();
		}
		else
		if (transType.equals("UpdateTreeType"))
		{
			retValue = new UpdateTreeTypeTransaction();
		}
		else
		if (transType.equals("UpdateTree"))
		{
			retValue = new UpdateTreeTransaction();
		}
		else
		if (transType.equals("RemoveTree"))
		{
			retValue = new RemoveTreeTransaction();
		}
		else
		if (transType.equals("SellTree"))
		{
			retValue = new SellTreeTransaction();
		}

		return retValue;
	}
}
