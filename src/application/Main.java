package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Main class which extends from Application Class
public class Main extends Application {

    public static ObservableList<String> Ranks;

    //This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    //This is the BorderPane of RootLayout
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        //1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        //Optional: Set a title for primary stage
        this.primaryStage.setTitle("KKC");

        //2) Initialize RootLayout
        initRootLayout();

        //3) Display the StudentOperations View
        //showStudentOperationsView();

        //showFinanceOperationsView();

    }

    //Initializes the root layout.
    public void initRootLayout() {
        try {
            //First, load root layout from RootLayout.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            //Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, 1000, 800); //We are sending rootLayout to the Scene.
            primaryStage.setScene(scene); //Set the scene in primary stage.

            //Third, show the primary stage
            primaryStage.show(); //Display the primary stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Shows the student operations view inside the root layout.
    public void showStudentOperationsView() {
        try {
            //First, load StudentOperationsView from StudentOperations.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/StudentOperations.fxml"));
            AnchorPane studentOperationsView = (AnchorPane) loader.load();

            // Set Student Operations view into the center of root layout.
            rootLayout.setCenter(studentOperationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFinanceOperationsView() {
        try {
            //First, load StudentOperationsView from StudentOperations.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/FinanceOperations.fxml"));
            AnchorPane financeOperationsView = (AnchorPane) loader.load();

            // Set Student Operations view into the center of root layout.
            rootLayout.setCenter(financeOperationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Ranks = RankFill();

        Ranks.add("White Belt");
        Ranks.add("Gold Stripe");
        Ranks.add("Gold Belt");
        Ranks.add("Green Stripe");
        Ranks.add("Green Belt");
        Ranks.add("Purple Stripe");
        Ranks.add("Purple Belt");
        Ranks.add("Brown Stripe");
        Ranks.add("Brown Belt");
        Ranks.add("Red Stripe");
        Ranks.add("Red Belt");
        Ranks.add("1st Degree");
        Ranks.add("1st of 2nd");
        Ranks.add("2nd Degree");
        Ranks.add("1st of 3rd");
        Ranks.add("2nd of 3rd");
        Ranks.add("3rd Degree");
        Ranks.add("1st of 4th");
        Ranks.add("2nd of 4th");
        Ranks.add("3rd of 4th");
        Ranks.add("4th Degree");
        Ranks.add("1st of 5th");
        Ranks.add("2nd of 5th");
        Ranks.add("3rd of 5th");
        Ranks.add("4th of 5th");
        Ranks.add("5th Degree");

        launch(args);
    }

    public static ObservableList<String> RankFill() {

        ObservableList<String> rank = FXCollections.observableArrayList();

        return rank;

    }
}