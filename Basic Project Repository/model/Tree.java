package model;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;
import exception.InvalidPrimaryKeyException;
import database.*;
import impresario.IView;
import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Account for the ATM application */
//==============================================================
public class Tree extends EntityBase implements IView {
    private static final String myTableName = "Tree";

    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // constructor to retrieve existing Tree
    public Tree(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = '" + barcode + "')";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple trees matching barcode: " + barcode + " found.");
            } else {
                Properties retrievedTreeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No tree matching barcode: " + barcode + " found.");
        }
    }

    // Constructor for creating a new Tree
    public Tree() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    // Constructor for updating a Tree
    public Tree(Properties props) {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();

        Enumeration<?> allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    // Removes a tree from the system if not sold
    public String remove() {
        String currentStatus = persistentState.getProperty("status");

        if ("Sold".equalsIgnoreCase(currentStatus)) {
            return "Error: Cannot remove tree. Tree is already sold.";
        }

        try {
            Properties whereClause = new Properties();
            whereClause.setProperty("barcode", persistentState.getProperty("barcode"));

            deletePersistentState(mySchema, whereClause);
            return "Success: Tree with barcode " + persistentState.getProperty("barcode") + " has been removed.";
        } catch (SQLException e) {
            return "Error: Could not remove tree from the database.";
        }
    }

    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    public static int compare(Tree a, Tree b) {
        String aNum = (String) a.getState("barcode");
        String bNum = (String) b.getState("barcode");

        return aNum.compareTo(bNum);
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    public void save() {
        updateStateInDatabase();
    }

    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("barcode") != null) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode", persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree with barcode: " + persistentState.getProperty("barcode") +
                        " updated successfully in database!";
            } else {
                // insert
                insertPersistentState(mySchema, persistentState);
                updateStatusMessage = "New tree inserted into database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding/updating Tree in database!";
        }
    }

    // Returns a view-friendly list of tree data
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("treeType"));
        v.addElement(persistentState.getProperty("notes"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));

        return v;
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }

    public void display() {
        System.out.println(toString());
    }

    //@Override
    //public String toString() {
    //    return "Barcode: " + persistentState.getProperty("barcode") +
    //            "; Tree Type: " + persistentState.getProperty("treeType") +
    //            "; Notes: " + persistentState.getProperty("notes") +
    //            "; Status: " + persistentState.getProperty("status") +
    //            "; Updated: " + persistentState.getProperty("dateStatusUpdated");
    //}
}