package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class AccountTableModel
{
	private final SimpleStringProperty accountNumber;
	private final SimpleStringProperty accountType;
	private final SimpleStringProperty balance;
	private final SimpleStringProperty serviceCharge;

	//----------------------------------------------------------------------------
	public AccountTableModel(Vector<String> accountData)
	{
		accountNumber =  new SimpleStringProperty(accountData.elementAt(0));
		accountType =  new SimpleStringProperty(accountData.elementAt(1));
		balance =  new SimpleStringProperty(accountData.elementAt(2));
		serviceCharge =  new SimpleStringProperty(accountData.elementAt(3));
	}

	//----------------------------------------------------------------------------
	public String getAccountNumber() {
        return accountNumber.get();
    }

	//----------------------------------------------------------------------------
    public void setAccountNumber(String number) {
        accountNumber.set(number);
    }

    //----------------------------------------------------------------------------
    public String getAccountType() {
        return accountType.get();
    }

    //----------------------------------------------------------------------------
    public void setAccountType(String aType) {
        accountType.set(aType);
    }

    //----------------------------------------------------------------------------
    public String getBalance() {
        return balance.get();
    }

    //----------------------------------------------------------------------------
    public void setBalance(String bal) {
        balance.set(bal);
    }
    
    //----------------------------------------------------------------------------
    public String getServiceCharge() {
        return serviceCharge.get();
    }

    //----------------------------------------------------------------------------
    public void setServiceCharge(String charge)
    {
    	serviceCharge.set(charge);
    }
}
