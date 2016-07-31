package net.ovoice.apachegui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApacheServer {

    public String name;
    public HashMap<String, Boolean> enabledModules = new HashMap<String, Boolean>();
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


    public ObservableList<ApacheModule> getApacheModules() throws IOException {
        ObservableList<ApacheModule> list = FXCollections.observableArrayList();
        getEnabledModules();

        Files.walk(Paths.get("/usr/lib/apache2/modules/")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                if (filePath.toString().endsWith(".so")) {
                    String fileName = filePath.getFileName().toString();
                    list.add(new ApacheModule(fileName, filePath, moduleIsEnabled(filePath)));
                }
            }
        });
        return list;
    }

    private Boolean moduleIsEnabled(Path modulePath) {
        Boolean isEnabled = false;
        String sModulePath = String.valueOf(modulePath);
        if (enabledModules.containsKey(sModulePath)) {
            if (enabledModules.get(sModulePath) == true)
                isEnabled = true;
        }
        return isEnabled;
    }

    private void getEnabledModules() {
        // Read /etc/apache2/mods-enabled/
        String apacheModsEnabledDirectory = "/etc/apache2/mods-enabled";
        try {
            Files.walk(Paths.get(apacheModsEnabledDirectory)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    if (filePath.toString().endsWith(".load")) {
                        readModuleDotLoad(filePath);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readModuleDotLoad(Path filePath) {
        String pattern = "^(?!#).*LoadModule\\s+([a-zA-Z0-9_\\-]*)\\s+([a-zA-Z0-9_\\.\\-\\/]*).*$";
        Pattern r = Pattern.compile(pattern);

        // Read file and check if there is enabled module
        try {
            Files.lines(filePath).forEach(line -> {
                Matcher m = r.matcher(line);
                if (m.find()) {
                    String moduleName = m.group(1); // example: mod_php5
                    String modulePath = m.group(2); // example: /usr/lib/apache2/modules/libphp5.so
                    enabledModules.put(String.valueOf(modulePath), true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
