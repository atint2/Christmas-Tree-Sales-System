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
public class ScoutCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Scout";

    private Vector<Scout> scouts;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public ScoutCollection(Scout scoutId) throws
            Exception
    {
        super(myTableName);

        if (scoutId == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Missing Scout information", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: ScoutCollection.<init>: Scout information is null");
        }

        String ScoutId = (String)scoutId.getState("ID");

        if (ScoutId == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Data corrupted: Scout has no id in database", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: ScoutCollection.<init>: Data corrupted: Scout has no id in repository");
        }

        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + ScoutId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            scouts = new Vector<Scout>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextScoutData = (Properties)allDataRetrieved.elementAt(cnt);

                Scout scout = new Scout(nextScoutData);

                if (scouts != null)
                {
                    addScout(scout);
                }
            }

        }
        else
        {
            throw new InvalidPrimaryKeyException("No Scout for Id : "
                    + ScoutId + ". Name : " + scoutId.getState("Name"));
        }

    }

    // ScoutCollection constructor that uses Properties object
    public ScoutCollection(Properties props) throws Exception {
        super(myTableName);
        System.out.println("ScoutCollection(Properties props)");
        // Build a query string with non-null properties
        String query = "SELECT * FROM " + myTableName + " WHERE 1=1";

        if (props.getProperty("FirstName") != null && !props.getProperty("FirstName").isEmpty()) {
            query += " AND (FirstName = '" + props.getProperty("FirstName") + "')";
        }
        if (props.getProperty("MiddleName") != null && !props.getProperty("MiddleName").isEmpty()) {
            query += " AND (MiddleName = '" + props.getProperty("MiddleName") + "')";
        }
        if (props.getProperty("LastName") != null && !props.getProperty("LastName").isEmpty()) {
            query += " AND (LastName = '" + props.getProperty("LastName") + "')";
        }
        if (props.getProperty("DateOfBirth") != null && !props.getProperty("DateOfBirth").isEmpty()) {
            query += " AND (DateOfBirth = '" + props.getProperty("DateOfBirth") + "')";
        }
        if (props.getProperty("PhoneNumber") != null && !props.getProperty("PhoneNumber").isEmpty()) {
            query += " AND (PhoneNumber = '" + props.getProperty("PhoneNumber") + "')";
        }
        if (props.getProperty("Email") != null && !props.getProperty("Email").isEmpty()) {
            query += " AND (Email = '" + props.getProperty("Email") + "')";
        }
        if (props.getProperty("TroopID") != null && !props.getProperty("TroopID").isEmpty()) {
            query += " AND (TroopID = '" + props.getProperty("TroopID") + "')";
        }

        System.out.println("Query: " + query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null && allDataRetrieved.size() > 0) {
            scouts = new Vector<Scout>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextScoutData = (Properties)allDataRetrieved.elementAt(cnt);
                Scout scout = new Scout(nextScoutData);
                addScout(scout);
            }
        } else {
            scouts = new Vector<Scout>(); // Return empty collection instead of throwing exception
        }
    }

    //----------------------------------------------------------------------------------
    private void addScout(Scout s)
    {
        //accounts.add(s);
        int index = findIndexToAdd(s);
        scouts.insertElementAt(s,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Scout s)
    {
        //users.add(u);
        int low=0;
        int high = scouts.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Scout midSession = scouts.elementAt(middle);

            int result = Scout.compare(s,midSession);

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
        if (key.equals("Scout"))
            return scouts;
        else
        if (key.equals("ScoutList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Scout retrieve(String scoutId)
    {
        Scout retValue = null;
        for (int cnt = 0; cnt < scouts.size(); cnt++)
        {
            Scout nextAcct = scouts.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("ID");
            if (nextAccNum.equals(scoutId))
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

        Scene localScene = myViews.get("ScoutCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("ScoutCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("ScoutCollectionView", localScene);
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