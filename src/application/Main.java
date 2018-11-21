package application;

import application.model.DemoPoint;
import application.model.Item;
import application.model.Student;
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
import java.math.BigDecimal;
import java.time.LocalDate;

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

        initRootLayout();
    }

    //Initializes the root layout.
    public static void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/Login.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Login");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createRankArray();
        initDatabase();

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
        DemoPointDAO demoPointDAO = new DemoPointDAO();
        DemoPointAwardedDAO demoPointAwardedDAO = new DemoPointAwardedDAO();
        UserDAO userDAO = new UserDAO();

        studentDAO.createStudentTable();
        testDAO.createTestTable();
        itemDAO.createItemTable();
        orderDAO.createOrderTable();
        test_studentDAO.createTest_StudentTable();
        classSessionDAO.createClassSessionTable();
        classDateDAO.createClassTable();
        attendanceDAO.createAttendanceTable();
        lineItemDAO.createLineItemTable();
        inventoryDAO.createInventoryTable();
        demoPointDAO.createDemoPointTable();
        demoPointAwardedDAO.createDemoPointAwardedTable();
        userDAO.createUserTable();

        User admin = new User("admin", "password", 1, "admin");
        User tempUser = userDAO.getUserByCredential("admin", "password");
        if (tempUser.getId() == -1){
            Student teacher = new Student("Tessa", "Gullings", "2nd of 4th", LOCATION.Waconia.toString(), "tessagullings@gmail.com", "612-816-6580", LocalDate.of(1993, 4, 6));
            studentDAO.insert(teacher);
            userDAO.insert(admin);

            Item item = new Item("Misc Item", BigDecimal.ZERO, BigDecimal.ZERO, "for all misc items - cannot be deleted and has no inventory quantity");
            itemDAO.insert(item);
        }

        createInitDemoPoints();
    }

    private static void createInitDemoPoints(){
        DemoPointDAO demoPointDAO = new DemoPointDAO();

        DemoPoint demoPoint = demoPointDAO.selectById(1);

        if (demoPoint.getId() >= 0){
            return;
        }

        if (demoPoint.getId() == -1){
            demoPoint.setName("Attend 80% of Classes a Month (avg 5x week)");
            demoPoint.setValue(3);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(2);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Attend 65% of Classes a Month (avg 4x week)");
            demoPoint.setValue(2);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(3);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Have the Most Classes of the Month");
            demoPoint.setValue(2);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(4);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Have the Most Classes of the Year");
            demoPoint.setValue(10);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(5);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Have the 2nd Most Classes of the Year");
            demoPoint.setValue(5);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(6);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Have the 3rd Most Classes of the Year");
            demoPoint.setValue(3);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(7);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Perfect Attendance for the Month");
            demoPoint.setValue(1);
            demoPoint.setCategory(1);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(8);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Testing");
            demoPoint.setValue(1);
            demoPoint.setCategory(3);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }

        demoPoint = demoPointDAO.selectById(9);

        if (demoPoint.getId() == -1){
            demoPoint.setName("Exceptional Test");
            demoPoint.setValue(1);
            demoPoint.setCategory(3);
            demoPoint.setModifiable(false);
            demoPointDAO.insert(demoPoint);
        }
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