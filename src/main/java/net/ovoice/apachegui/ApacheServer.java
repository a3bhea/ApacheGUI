package net.ovoice.apachegui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class ApacheServer {

    public String name;
    public HashMap<String, HashMap<String, String>> enabledModules = new HashMap<String, HashMap<String, String>>();
    private AG_Runtime agRuntime;
    private String[] lastCmd;
    public static final String MODULES_ENABLED_DIR = "/etc/apache2/mods-enabled";
    public static final String MODULES_AVAILABLE_DIR = "/etc/apache2/mods-available";

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
        enabledModules.clear();
        getEnabledModules();

        Files.walk(Paths.get("/usr/lib/apache2/modules/")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                if (filePath.toString().endsWith(".so")) {
                    String fileName = filePath.getFileName().toString();
                    list.add(new ApacheModule(fileName, filePath, moduleIsEnabled(filePath), getModuleName(filePath), getModuleDotLoadFilePath(filePath), getModuleDotConfFilePath(filePath)));
                }
            }
        });
        return list;
    }

    private String getModuleDotConfFilePath(Path modulePath) {
        String sModulePath = String.valueOf(modulePath);
        if (enabledModules.containsKey(sModulePath)) {
            if (enabledModules.get(sModulePath).containsKey("dotConfFilePath")) {
                return enabledModules.get(sModulePath).get("dotConfFilePath");
            }
        }
        return "";
    }

    private String getModuleName(Path modulePath) {
        String sModulePath = String.valueOf(modulePath);
        if (enabledModules.containsKey(sModulePath)) {
            return enabledModules.get(sModulePath).get("moduleName");
        }
        return "";
    }


    private String getModuleDotLoadFilePath(Path modulePath) {
        String sModulePath = String.valueOf(modulePath);
        if (enabledModules.containsKey(sModulePath)) {
            return enabledModules.get(sModulePath).get("dotLoadFilePath");
        }
        return "";
    }

    private Boolean moduleIsEnabled(Path modulePath) {
        Boolean isEnabled = false;
        String sModulePath = String.valueOf(modulePath);
        if (enabledModules.containsKey(sModulePath)) {
            if (enabledModules.get(sModulePath).get("isEnabled") == "true")
                isEnabled = true;
        }
        return isEnabled;
    }

    private void getEnabledModules() {
        // Read /etc/apache2/mods-enabled/
        buildModulesList(MODULES_ENABLED_DIR);

        // Read /etc/apache2/mods-available/
        buildModulesList(MODULES_AVAILABLE_DIR);
    }

    private void buildModulesList(String dir) {
        try {
            Files.walk(Paths.get(dir)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    if (filePath.toString().endsWith(".load")) {
                        readModuleDotLoad(filePath, dir);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Check for LoadModule directive */
    private void readModuleDotLoad(Path filePath, String dir) {
//        String pattern = "^#?.*LoadModule\\s+([a-zA-Z0-9_\\-]*)\\s+([a-zA-Z0-9_\\.\\-\\/]*).*$";
        String pattern = "^(?!#).*LoadModule\\s+([a-zA-Z0-9_\\-]*)\\s+([a-zA-Z0-9_\\.\\-\\/]*).*$";
        Pattern r = Pattern.compile(pattern);

        // Read file and check if there is enabled module
        try {
            Files.lines(filePath).forEach(line -> {
                Matcher m = r.matcher(line);
                if (m.find()) {
                    String moduleName = m.group(1); // example: mod_php5
                    String modulePath = m.group(2); // example: /usr/lib/apache2/modules/libphp5.so
                    HashMap<String, String> moduleData = new HashMap<>();
                    if (dir == MODULES_ENABLED_DIR) {
                        moduleData.put("isEnabled", String.valueOf(true));
                        moduleData.put("moduleName", moduleName);
                        moduleData.put("dotLoadFilePath", String.valueOf(filePath));

                        /* Check if there is .conf file */
                        String fileNameWithoutExtension = FilenameUtils.removeExtension(String.valueOf(filePath.getFileName()));
                        String dotConfFilePath = dir + "/" + fileNameWithoutExtension + ".conf";
                        File dotConfFile = new File(dotConfFilePath);
                        if (dotConfFile.exists() && !dotConfFile.isDirectory()) {
                            System.out.println(dotConfFilePath);
                            moduleData.put("dotConfFilePath", dotConfFilePath);
                        }
                        enabledModules.put(String.valueOf(modulePath), moduleData);
                    }
                    if ((dir == MODULES_AVAILABLE_DIR) && (!enabledModules.containsKey(String.valueOf(modulePath)))) {
                        moduleData.put("isEnabled", String.valueOf(false));
                        moduleData.put("moduleName", moduleName);
                        moduleData.put("dotLoadFilePath", String.valueOf(filePath));
                        /* Check if there is .conf file */
                        String fileNameWithoutExtension = FilenameUtils.removeExtension(String.valueOf(filePath.getFileName()));
                        String dotConfFilePath = dir + "/" + fileNameWithoutExtension + ".conf";
                        File dotConfFile = new File(dotConfFilePath);
                        if (dotConfFile.exists() && !dotConfFile.isDirectory()) {
                            moduleData.put("dotConfFilePath", dotConfFilePath);
                        }
                        enabledModules.put(String.valueOf(modulePath), moduleData);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
