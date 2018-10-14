package application;

import application.model.User;
import application.util.DAO.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

//Main class which extends from Application Class
public class Main extends Application {

    public static User userLoggedIn;
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
        initDatabase();
        createRankArray();

        launch(args);
    }

    private static void initDatabase(){
        DBUtil.createDatabase();

        Test_StudentDAOImpl test_studentDAO = new Test_StudentDAOImpl();
        StudentDAOImpl studentDAO = new StudentDAOImpl();
        ItemDAOImpl itemDAO = new ItemDAOImpl();
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();
        ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
        ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();
        TestDAOImpl testDAO = new TestDAOImpl();
        LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();
        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();

        UserDAO userDAO = new UserDAO();
        userDAO.createUserTable();

        User user = new User("admin", "password");

        User tempUser = userDAO.getUserByCredential("admin", "password");
        if (tempUser.getId() == -1){
            userDAO.insert(user);
        }

        itemDAO.createItemTable();
        orderDAO.createOrderTable();
        attendanceDAO.createAttendanceTable();
        studentDAO.createStudentTable();
        test_studentDAO.createTest_StudentTable();
        classSessionDAO.createClassSessionTable();
        classDateDAO.createClassTable();
        testDAO.createTestTable();
        lineItemDAO.createLineItemTable();
        inventoryDAO.createInventoryTable();
    }

    private static void createRankArray(){
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
    }

    private static ObservableList<String> RankFill() {

        ObservableList<String> rank = FXCollections.observableArrayList();

        return rank;

    }
}