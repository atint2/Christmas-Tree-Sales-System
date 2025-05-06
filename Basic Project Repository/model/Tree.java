package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Tree extends EntityBase implements IView {
    private static final String myTableName = "Tree";
    protected Properties dependencies;

    private String updateStatusMessage = "";

    public Tree(String barcode)  throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + barcode + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one tree at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one tree. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple trees matching barcode : "
                        + barcode + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements())
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        else
        {
            throw new InvalidPrimaryKeyException("No tree matching barcode : "
                    + barcode + " found.");
        }
    }

    public Tree(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }


    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;

        return persistentState.getProperty(key);
    };
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
    public static int compare(Tree a, Tree b)
    {
        String aNum = (String)a.getState("Barcode");
        String bNum = (String)b.getState("Barcode");

        return aNum.compareTo(bNum);
    }

    public void save() //
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("Barcode") != null) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("Barcode",
                        persistentState.getProperty("Barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree data for Scout barcode : " + persistentState.getProperty("Barcode") + " updated successfully in database!";
            } else {
                // insert
                Integer ID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("Barcode", "" + ID.intValue());
                updateStatusMessage = "Tree data for new Tree : " + persistentState.getProperty("Barcode")
                        + "installed successfully in database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding Tree data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    public void deleteTree() throws SQLException {
        if (persistentState.getProperty("Barcode") != null) {
            Properties whereClause = new Properties();
            whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
            deletePersistentState(mySchema, whereClause);
            updateStatusMessage = "Tree with barcode: " + persistentState.getProperty("Barcode") + " deleted from database!";
        } else {
            throw new SQLException("Cannot delete Tree: Barcode is missing.");
        }
    }
    public void addTree() throws SQLException {
        if (persistentState.getProperty("Barcode") == null) {
            Properties whereClause = new Properties();
            whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
            insertPersistentState(mySchema, whereClause);
            updateStatusMessage = "Tree with barcode: " + persistentState.getProperty("Barcode") + " added to database!";
        } else {
            throw new SQLException("Tree barcode already exists");
        }
    }
}