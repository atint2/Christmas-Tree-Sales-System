import event.Event;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import model.TreeLotCoordinator;
import javafx.application.Application;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;

/**
 * The class containing the main program  for the Tree Sales System application
 */
//==============================================================
public class TreeSalesSystem extends Application {

    private TreeLotCoordinator myTLC;        // the main behavior for the application

    /**
     * Main frame of the application
     */
    private Stage mainStage;

    // start method for this class, the main application object
    //----------------------------------------------------------
    public void start(Stage primaryStage) {
        System.out.println("TreeSalesSystem Version 3.00");
        System.out.println("Copyright 2025 Alison Tintera, Jayleen Ramos, Mason Dear, Cam Engert, Stanley Valdez");

        // Create the top-level container (main frame) and add contents to it.
        MainStageContainer.setStage(primaryStage, "Christmas Tree Sales System Version 1.00");
        mainStage = MainStageContainer.getInstance();

        // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
        // 'X' IN THE WINDOW), and show it.
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
        });

        try {
            myTLC = new TreeLotCoordinator();
        } catch (Exception exc) {
            System.err.println("TreeSalesSystem.TreeSalesSystem - could not create TreeLotCoordinator!");
            new Event(Event.getLeafLevelClassName(this), "TreeSalesSystem.<init>", "Unable to create TreeLotCoordinator object", Event.ERROR);
            exc.printStackTrace();
        }


        WindowPosition.placeCenter(mainStage);

        mainStage.show();
    }


    /**
     * The "main" entry point for the application. Carries out actions to
     * set up the application
     */
    //----------------------------------------------------------
    public static void main(String[] args) {

        launch(args);
    }
}
