package application;

import application.model.User;
import application.util.ItemDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

//Main class which extends from Application Class
public class Main extends Application {

    public static User userLoggedIn;
    public static ObservableList<String> Ranks;
    private static Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();
    private static ItemDAOImpl idi = new ItemDAOImpl();

    //This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    //This is the BorderPane of RootLayout
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        //1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        //Optional: Set a title for primary stage
        this.primaryStage.setTitle("Kroells Karate Club");
        this.primaryStage.getIcons().add(new Image("file:duncan.png"));
        //2) Initialize RootLayout
        initRootLayout();
    }

    //Initializes the root layout.
    public void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Login.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Login");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        stdi.createTest_StudentTable();
        idi.createItemTable();

        Ranks = RankFill();

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

    private static ObservableList<String> RankFill() {

        ObservableList<String> rank = FXCollections.observableArrayList();

        return rank;

    }
}