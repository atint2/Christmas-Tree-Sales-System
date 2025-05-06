package userinterface;

// system imports

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Scout;
import model.ScoutCollection;
import model.TreeCollection;

import java.util.Enumeration;
import java.util.Vector;

public class TreeCollectionView extends View {

    protected TableView<TreeTableModel> tableOfTrees;
    protected TextField treeBarcode;
    protected Button cancelButton;
    protected Button submitButton;

    protected MessageView statusLog;

    //--------------------------------------------------------------------------
    public TreeCollectionView(IModel treeLotCoordinator) {
        super(treeLotCoordinator, "TreeCollectionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("TransactionError", this);
    }

    //--------------------------------------------------------------------------
    protected void populateFields() {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues() {
        ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();
        try {
            TreeCollection scoutCollection = (TreeCollection) myModel.getState("TreeList");

            Vector entryList = (Vector) scoutCollection.getState("Trees");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true) {
                Scout nextScout = (Scout) entries.nextElement();
                Vector<String> view = nextScout.getEntryListView();

                // add this list entry to the list
                TreeTableModel nextTableRowData = new TreeTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfTrees.setItems(tableData);
        } catch (Exception e) {//SQLException e) {
            System.out.println(e);
        }
    }

    // Create the labels and fields
    //-------------------------------------------------------------
    private VBox createTitle() {
        VBox container = new VBox(10);
        Text titleText = new Text("CHRISTMAS TREE SALES SYSTEM");
        titleText.setFont(Font.font("Garamond", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(400);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        // Create font
        Font myFont = Font.font("Garamond", FontWeight.BOLD, 15);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("LIST OF SCOUTS");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        prompt.setFont(Font.font("Garamond", FontWeight.BOLD, 17));
        grid.add(prompt, 0, 0, 2, 1);

        tableOfTrees = new TableView<TreeTableModel>();
        tableOfTrees.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn scoutIDColumn = new TableColumn("Barcode");
        scoutIDColumn.setMinWidth(100);
        scoutIDColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("Barcode"));

        TableColumn firstNameColumn = new TableColumn("TreeType");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("TreeType"));

        TableColumn middleNameColumn = new TableColumn("Notes");
        middleNameColumn.setMinWidth(100);
        middleNameColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("Notes"));

        TableColumn lastNameColumn = new TableColumn("Status");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("Status"));



        // Add columns to table
        tableOfTrees.getColumns().addAll(scoutIDColumn, firstNameColumn, middleNameColumn,
                lastNameColumn);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfTrees);

        // Enter scout ID to select scout
        Text scoutIDLabel = new Text(" Scout ID : ");
        scoutIDLabel.setFont(myFont);
        scoutIDLabel.setWrappingWidth(150);

        treeBarcode = new TextField();
        treeBarcode.setEditable(true);

        submitButton = new Button("Submit");
        submitButton.setFont(myFont);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processScoutSelected();
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setFont(myFont);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelScoutList", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(scoutIDLabel);
        btnContainer.getChildren().add(treeBarcode);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    public void updateState(String key, Object value) {
    }

    //--------------------------------------------------------------------------
    protected void processScoutSelected() {
        String selectedScoutID = treeBarcode.getText();

        myModel.stateChangeRequest("ScoutSelected", selectedScoutID);
    }

    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

}
