package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

public class ScoutTableModel {
    private final SimpleStringProperty scoutID;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty email;
    private final SimpleStringProperty troopID;

    //----------------------------------------------------------------------------
    public ScoutTableModel(Vector<String> scoutData)
    {
        scoutID =  new SimpleStringProperty(scoutData.elementAt(0));
        firstName =  new SimpleStringProperty(scoutData.elementAt(1));
        middleName =  new SimpleStringProperty(scoutData.elementAt(2));
        lastName =  new SimpleStringProperty(scoutData.elementAt(3));
        dateOfBirth =  new SimpleStringProperty(scoutData.elementAt(4));
        phoneNumber =  new SimpleStringProperty(scoutData.elementAt(5));
        email =  new SimpleStringProperty(scoutData.elementAt(6));
        troopID =  new SimpleStringProperty(scoutData.elementAt(7));
    }

    //----------------------------------------------------------------------------
    public String getScoutID() {
        return scoutID.get();
    }

    //----------------------------------------------------------------------------
    public String getFirstName() {
        return firstName.get();
    }

    //----------------------------------------------------------------------------
    public void setFirstName(String name) {
        firstName.set(name);
    }

    //----------------------------------------------------------------------------
    public String getMiddleName() {
        return middleName.get();
    }

    //----------------------------------------------------------------------------
    public void setMiddleName(String name) {
        middleName.set(name);
    }

    //----------------------------------------------------------------------------
    public String getLastName() {
        return lastName.get();
    }

    //----------------------------------------------------------------------------
    public void setLastName(String name) {
        lastName.set(name);
    }

    //----------------------------------------------------------------------------
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    //----------------------------------------------------------------------------
    public void setDateOfBirth(String dob) {
        dateOfBirth.set(dob);
    }

    //----------------------------------------------------------------------------
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    //----------------------------------------------------------------------------
    public void setPhoneNumber(String phone) {
        phoneNumber.set(phone);
    }

    //----------------------------------------------------------------------------
    public String getEmail() {
        return email.get();
    }

    //----------------------------------------------------------------------------
    public void setEmail(String eAddress) {
        email.set(eAddress);
    }

    //----------------------------------------------------------------------------
    public String getTroopID() {
        return troopID.get();
    }

    //----------------------------------------------------------------------------
    public void setTroopID(String number) {
        troopID.set(number);
    }
}
