// COPYRIGHT (c) 2025; Ryan Fanning, Collin Fanning, Steven Polvino, Sandeep Mitra
// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Account for the ATM application */
//==============================================================
public class Scout extends EntityBase implements IView
{
    private static final String myTableName = "Scout";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public Scout(String scoutId)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (scoutId = " + scoutId + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple scouts matching id : "
                        + scoutId + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedScoutData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedScoutData.propertyNames();
                while (allKeys.hasMoreElements())
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedScoutData.getProperty(nextKey);
                    // accountNumber = Integer.parseInt(retrievedAccountData.getProperty("accountNumber"));

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no Scout found for this Scout id, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No Scout matching id : "
                    + scoutId + " found.");
        }
    }

    // Creates a new empty Scout object
    //----------------------------------------------------------
    public Scout() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    // Can also be used to create a NEW Scout (if the system it is part of
    // allows for a new Scout to be added)
    //----------------------------------------------------------
    public Scout(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    //-----------------------------------------------------------------------------------
    public void processNewScout(Properties p) {
        // Store the properties in the persistent state
        persistentState = new Properties();
        Enumeration<?> allKeys = p.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = p.getProperty(nextKey);
            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }

        // Try inserting the new Scout into the database
        updateStateInDatabase(); // This method will handle the DB insertion
        updateStatusMessage = "New Scout successfully added to the database!";

        // Notify the view of the success message
        stateChangeRequest("ProcessNewScoutResponse", updateStatusMessage);
    }



    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //----------------------------------------------------------------
    public static int compare(Scout a, Scout b)
    {
        String aNum = (String)a.getState("scoutId");
        String bNum = (String)b.getState("scoutId");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //-----------------------------------------------------------
    public void save() //
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("scoutId") != null)
            {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("scoutId",
                        persistentState.getProperty("scoutId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Scout data for Scout id : " + persistentState.getProperty("scoutId") + " updated successfully in database!";
            }
            else
            {
                // insert
                Integer scoutId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("scoutId", "" + scoutId.intValue());
                updateStatusMessage = "Scout data for new Scout : " +  persistentState.getProperty("scoutId")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in adding Scout data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }


    /**
     * This method is needed solely to enable the Scout information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();


        v.addElement(persistentState.getProperty("firstName"));
        v.addElement(persistentState.getProperty("lastName"));
        v.addElement(persistentState.getProperty("middleName"));
        v.addElement(persistentState.getProperty("birthday"));
        v.addElement(persistentState.getProperty("phone"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("scoutId"));

        return v;
    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    //@Override
    //public String toString() {
    //    return "Title: " + persistentState.getProperty("scoutTitle") +
    //            "; Author: " + persistentState.getProperty("author") +
    //            "; Year: " + persistentState.getProperty("pubYear");
    //}

    public void display() {
        System.out.println(toString());
    }
}
