package net.ovoice.apachegui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApacheServer {

    public String name;
    private AG_Runtime agRuntime;
    private String[] lastCmd;

    public ApacheServer() {
        agRuntime = new AG_Runtime();
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String restart() {
        String[] agCmd = {"service", "apache2", "restart"};
        lastCmd = agCmd;
        return agRuntime.exec(agCmd);
    }

    public String start() {
        String[] agCmd = {"service", "apache2", "start"};
        lastCmd = agCmd;
        return agRuntime.exec(agCmd);
    }

    public String stop() {
        String[] agCmd = {"service", "apache2", "stop"};
        lastCmd = agCmd;
        return agRuntime.exec(agCmd);
    }

    public String status() {
        String[] agCmd = {"service", "apache2", "status"};
        lastCmd = agCmd;
        return agRuntime.exec(agCmd);
    }


    public static ObservableList<ApacheModule> getApacheModules() throws IOException {
        ObservableList<ApacheModule> list = FXCollections.observableArrayList();

        Files.walk(Paths.get("/usr/lib/apache2/modules/")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                if(filePath.toString().endsWith(".so")){
                    String fileName = filePath.getFileName().toString();
                    list.add(new ApacheModule(fileName));
                }
            }
        });
        return list;
    }
}
