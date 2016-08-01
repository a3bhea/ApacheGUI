package net.ovoice.apachegui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class ApacheModule {
    private final SimpleStringProperty path;
    private final SimpleStringProperty dotLoadFilePath;
    public StringProperty name;

    public String getPath() {
        return path.get();
    }

    public String getDotLoadFilePath() {
        return dotLoadFilePath.get();
    }

    public String getName() {
        return name.get();
    }

    public String getModuleName() {
        return moduleName.get();
    }

    public StringProperty moduleName;
    public BooleanProperty enabled = new SimpleBooleanProperty(false);

    public boolean isEnabled() {
        return enabled.get();
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

    public final StringProperty pathProperty() {
        return path;
    }

    public ApacheModule(String name, Path path, Boolean isEnabled, String moduleName, String dotLoadFilePath) {
        this.name = new SimpleStringProperty(name);
        this.moduleName = new SimpleStringProperty(moduleName);
        this.path = new SimpleStringProperty(String.valueOf(path));
        this.enabled = new SimpleBooleanProperty(isEnabled);
        this.dotLoadFilePath = new SimpleStringProperty(dotLoadFilePath);
    }

    public void disable() {
        System.out.println(">>> Disabling " + moduleName);
    }

    public void enable() {
        System.out.println(">>> Enabling " + moduleName);
    }
}
