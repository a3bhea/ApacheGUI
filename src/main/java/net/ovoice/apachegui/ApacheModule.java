package net.ovoice.apachegui;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.nio.file.Path;

public class ApacheModule {
    private final SimpleStringProperty path;
    private final SimpleStringProperty dotLoadFilePath;
    private final SimpleStringProperty dotConfFilePath;
    public StringProperty moduleName;
    public BooleanProperty enabled = new SimpleBooleanProperty(false);

    public StringProperty name;

    public String getPath() {
        return path.get();
    }

    public String getDotLoadFilePath() {
        return dotLoadFilePath.get();
    }

    public String getDotConfFilePath() {
        return dotConfFilePath.get();
    }

    public String getName() {
        return name.get();
    }

    public String getModuleName() {
        return moduleName.get();
    }

    public final StringProperty nameProperty() {
        return name;
    }

    public final BooleanProperty enabledProperty() {
        return enabled;
    }

    public final StringProperty moduleNameProperty() {
        return moduleName;
    }

    public final StringProperty dotLoadFilePathProperty() {
        return dotLoadFilePath;
    }

    public final StringProperty dotConfFilePathProperty() {
        return dotConfFilePath;
    }

    public final StringProperty pathProperty() {
        return path;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public ApacheModule(String name, Path path, Boolean isEnabled, String moduleName, String dotLoadFilePath, String dotConfFilePath) {
        this.name = new SimpleStringProperty(name);
        this.moduleName = new SimpleStringProperty(moduleName);
        this.path = new SimpleStringProperty(String.valueOf(path));
        this.enabled = new SimpleBooleanProperty(isEnabled);
        this.dotLoadFilePath = new SimpleStringProperty(dotLoadFilePath);
        this.dotConfFilePath = new SimpleStringProperty(dotConfFilePath);
    }

    public boolean moveModuleFile(String filePath, String dir) {
        try {
            File afile = new File(filePath);
            if (afile.renameTo(new File(dir + "/" + afile.getName()))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disable(AG_FXform agFXform) {
        Boolean isMovedDotLoadFile = moveModuleFile(dotLoadFilePath.get(), ApacheServer.MODULES_AVAILABLE_DIR);
        Boolean isMovedDotConfFile = true;
        if (isMovedDotLoadFile) {
            if (getDotConfFilePath() != "") {
                isMovedDotConfFile = moveModuleFile(getDotConfFilePath(), ApacheServer.MODULES_AVAILABLE_DIR);
            }
        }
        /* Verify that both files have been moved successfully */
        if (isMovedDotLoadFile && isMovedDotConfFile) {
            setEnabled(false);
            agFXform.restartButtonClicked();
            agFXform.resetTableItems();
        }else{
            System.out.println(">>> Error moving files (disable) "+getDotLoadFilePath()+" "+getDotConfFilePath());
        }
    }

    public void enable(AG_FXform agFXform) {
        Boolean isMovedDotLoadFile = moveModuleFile(dotLoadFilePath.get(), ApacheServer.MODULES_ENABLED_DIR);
        Boolean isMovedDotConfFile = true;
        if (isMovedDotLoadFile) {
            if (getDotConfFilePath() != "") {
                isMovedDotConfFile = moveModuleFile(getDotConfFilePath(), ApacheServer.MODULES_ENABLED_DIR);
            }
        }
        /* Verify that both files have been moved successfully */
        if (isMovedDotLoadFile && isMovedDotConfFile) {
            setEnabled(true);
            agFXform.restartButtonClicked();
            agFXform.resetTableItems();
        }else{
            System.out.println(">>> Error moving files (disable) "+getDotLoadFilePath()+" "+getDotConfFilePath());
        }
    }
}
