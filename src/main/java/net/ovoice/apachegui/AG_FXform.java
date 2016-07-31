package net.ovoice.apachegui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AG_FXform implements Initializable {
    private final ApacheServer apacheServer;
    @FXML
    public TextFlow debugConsole;

    @FXML
    public Button restartButton;

    @FXML
    public Button statusButton;

    @FXML
    public Button startButton;

    @FXML
    public Button stopButton;

    @FXML
    public javafx.scene.control.ScrollPane debugConsoleScrollPane;

    @FXML
    public TableView<ApacheModule> modulesTable;

    public void d(String s, int newLine) {
        if (newLine == 1) {
            Text ts = new Text("\n" + LocalDateTime.now() + " >> \n");
            ts.setFill(Color.RED);
            Text t = new Text(String.valueOf(s));
            debugConsole.getChildren().addAll(ts, t);
        } else {
            Text ts = new Text("\n" + LocalDateTime.now() + " >> ");
            ts.setFill(Color.RED);
            Text t = new Text(String.valueOf(s));
            debugConsole.getChildren().addAll(ts, t);
        }
        debugConsoleScrollPane.setVvalue(1.0d);
    }

    public void d(String s) {
        d(s, 0);
    }

    public AG_FXform() {
        apacheServer = new ApacheServer();
        apacheServer.setName("Apache Server X");
    }

    public void restartButtonClicked() {
        String output = apacheServer.restart();
        d(output);
    }

    public void startButtonClicked() {
        String output = apacheServer.start();
        d(output);
    }

    public void stopButtonClicked() {
        String output = apacheServer.stop();
        d(output);
    }

    public void statusButtonClicked() {
        String output = apacheServer.status();
        d(output, 1);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        debugConsole.setMaxHeight(200);
        debugConsole.maxHeight(200);
        setupModulesTable();


    }

    private void setupModulesTable() {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<ApacheModule, String>("name"));

        TableColumn enabledCol = new TableColumn("Enabled");
        enabledCol.setMinWidth(200);
        enabledCol.setCellValueFactory(
                new PropertyValueFactory<ApacheModule, Boolean>("enabled"));

        try {
            modulesTable.setItems(ApacheServer.getApacheModules());
        } catch (IOException e) {
            e.printStackTrace();
        }
        modulesTable.getColumns().addAll(nameCol, enabledCol);
    }
}
