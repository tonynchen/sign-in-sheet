package Main;

import DataBaseUtil.DBOrder;
import DataBaseUtil.DatabaseUtil;
import DataBaseUtil.SerialNum;
import Util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main extends Application {

    // CSS path
    public static String styleSheetPath =
            Paths.get("resources/stylesheet.css").toUri().toString().replace("file:///", "");
    public static String fxmlPath = "resources/";

    /**
     * Function that initialize before stage shows
     * @param args N/A
     */
    public static void main(String[] args) {

        // Clear error log
        HandleError.clear();

        // Initialize Database
        if (!DatabaseUtil.ConnectionInitAndCreate()) {
            AlertBox.display("Error", "Data Base does not exists, and creation failure");
            System.exit(0);
        }

        // Initialize Serial Num
        SerialNum.initSerialNum();

        // start the screen
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = Main.loadFXML("MainScreen.fxml");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            Path source = Paths.get("signInEntries.db");
            Path target = Paths.get(System.getProperty("user.home") + "/signInEntries.db");
            try {
                if (Files.exists(target)) Files.delete(target);
                Files.copy(source, target);
            } catch (IOException e) {
                new HandleError(Main.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName(),
                        e.getMessage(), e.getStackTrace(), false);
            }
            stage.close();
        });

        stage.show();
    }

    public static Scene loadFXML(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fileInputStream = new FileInputStream(new File(fxmlPath + fxmlName));
            Parent parent = loader.load(fileInputStream);
            Scene scene = new Scene(parent);
            scene.getStylesheets().add("file:///" + styleSheetPath);
            return scene;
        } catch (Exception e) {
            AlertBox.display("错误", "窗口错误！");
            new HandleError(Main.class.getName(), Thread.currentThread().getStackTrace()[1].getMethodName(),
                    e.getMessage(), e.getStackTrace(), false);
            return new Scene(new VBox());
        }
    }
}
