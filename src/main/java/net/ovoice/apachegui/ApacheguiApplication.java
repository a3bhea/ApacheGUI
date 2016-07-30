package net.ovoice.apachegui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApacheguiApplication extends Application {

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/form.fxml"));
        primaryStage.setTitle("Apache GUI");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }
}
