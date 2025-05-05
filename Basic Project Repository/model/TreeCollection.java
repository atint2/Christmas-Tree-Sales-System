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
public class TreeCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Tree";

    private Vector<Tree> trees;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public TreeCollection(Properties props) throws
            Exception
    {
        super(myTableName);
        System.out.println("TreeCollection(Properties props)");
        // Build a query string with non-null properties
        String query = "SELECT * FROM " + myTableName + " WHERE 1=1";

        if (props.getProperty("Barcode") != null && !props.getProperty("Barcode").isEmpty()) {
            query += " AND (Barcode = '" + props.getProperty("Barcode") + "')";
        }
        if (props.getProperty("TreeType") != null && !props.getProperty("MiddleName").isEmpty()) {
            query += " AND (TreeType = '" + props.getProperty("MiddleName") + "')";
        }
        if (props.getProperty("Notes") != null && !props.getProperty("LastName").isEmpty()) {
            query += " AND (Notes = '" + props.getProperty("LastName") + "')";
        }
        if (props.getProperty("Notes") != null && !props.getProperty("DateOfBirth").isEmpty()) {
            query += " AND (Notes = '" + props.getProperty("DateOfBirth") + "')";
        }
        if (props.getProperty("Status") != null && !props.getProperty("PhoneNumber").isEmpty()) {
            query += " AND (Status = '" + props.getProperty("PhoneNumber") + "')";
        }
        if (props.getProperty("DateStatusUpdated") != null && !props.getProperty("Email").isEmpty()) {
            query += " AND (DateStatusUpdated = '" + props.getProperty("Email") + "')";
        }

        System.out.println("Query: " + query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null && allDataRetrieved.size() > 0) {
            trees = new Vector<Tree>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
                Properties nextTreeData = (Properties)allDataRetrieved.elementAt(cnt);
                Tree tree = new Tree(nextTreeData);
                addTree(tree);
            }
        } else {
            trees = new Vector<Tree>(); // Return empty collection instead of throwing exception
        }

    }

    protected TreeCollection(String tablename) {
        super(tablename);
    }

    //----------------------------------------------------------------------------------
    private void addTree(Tree a)
    {
        //accounts.add(a);
        int index = findIndexToAdd(a);
        trees.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Tree a)
    {
        //users.add(u);
        int low=0;
        int high = trees.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Tree midSession = trees.elementAt(middle);

            int result = Tree.compare(a,midSession);

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
        if (key.equals("Tree"))
            return trees;
        else
        if (key.equals("TreeList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Tree retrieve(String accountNumber)
    {
        Tree retValue = null;
        for (int cnt = 0; cnt < trees.size(); cnt++)
        {
            Tree nextAcct = trees.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("barcode");
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

        Scene localScene = myViews.get("TreeCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("TreeCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("TreeCollectionView", localScene);
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
