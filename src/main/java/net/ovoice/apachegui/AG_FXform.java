package net.ovoice.apachegui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
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
        /* File */
        TableColumn nameCol = new TableColumn("File");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<ApacheModule, String>("name"));

        /* Enable/Disable */
        TableColumn enabledCol = new TableColumn("Enabled");
        enabledCol.setMinWidth(100);
        enabledCol.setCellValueFactory(new PropertyValueFactory<ApacheModule, Boolean>("enabled"));
        renderModuleEnabledCell(enabledCol);

        /* Path to module file */
        TableColumn pathCol = new TableColumn("Path");
        pathCol.setMinWidth(400);
        pathCol.setCellValueFactory(new PropertyValueFactory<ApacheModule, String>("path"));

        /* Module name */
        TableColumn modNameCol = new TableColumn("Module");
        modNameCol.setMinWidth(200);
        modNameCol.setCellValueFactory(new PropertyValueFactory<ApacheModule, String>("moduleName"));

        /* .load file location */
        TableColumn modDotLoadFilePath = new TableColumn(".load file");
        modDotLoadFilePath.setMinWidth(400);
        modDotLoadFilePath.setCellValueFactory(new PropertyValueFactory<ApacheModule, String>("dotLoadFilePath"));


        /* .load file location */
        TableColumn modDotConfFilePath = new TableColumn(".conf file");
        modDotConfFilePath.setMinWidth(400);
        modDotConfFilePath.setCellValueFactory(new PropertyValueFactory<ApacheModule, String>("dotConfFilePath"));

        try {
            modulesTable.setItems(apacheServer.getApacheModules());
        } catch (IOException e) {
            e.printStackTrace();
        }
        modulesTable.getColumns().addAll(nameCol, modNameCol, enabledCol, pathCol, modDotLoadFilePath, modDotConfFilePath);
    }

    public void resetTableItems(){
        try {
            modulesTable.setItems(apacheServer.getApacheModules());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderModuleEnabledCell(TableColumn enabledCol) {
        enabledCol.setCellFactory(column -> new TableCell<ApacheModule, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    ApacheModule apacheModule = (ApacheModule) this.getTableRow().getItem();

                    /* */
                    Button btn = new Button();
                    String val = item ? "enabled" : "disabled";
                    btn.setText(val);

                    setGraphic(btn);
                    if (!item) {
                        btn.setStyle("-fx-base: salmon");
                        btn.setTextFill(Color.WHITE);
                    } else {
                        btn.setStyle("-fx-base: #009900");
                        btn.setTextFill(Color.WHITE);
                    }
                    setupEnableButtonAction(apacheModule, btn);
                }
            }
        });
        /* */
    }

    /* */
    private void setupEnableButtonAction(ApacheModule apacheModule, Button btn) {
    /* On click -> enable/disable module */
        btn.setOnAction(event -> {
            if (apacheModule.isEnabled())
                apacheModule.disable(this);
            else
                apacheModule.enable(this);
        });
    }
}
