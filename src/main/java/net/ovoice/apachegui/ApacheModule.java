package net.ovoice.apachegui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class ApacheModule {
    private final SimpleStringProperty path;
    public StringProperty name;
    public BooleanProperty enabled = new SimpleBooleanProperty(false);

    public String getPath() {
        return path.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public final StringProperty nameProperty() {
        return name;
    }

    public final BooleanProperty enabledProperty() {
        return enabled;
    }

    public ApacheModule(String name, Path path, Boolean isEnabled) {
        this.name = new SimpleStringProperty(name);
        this.path = new SimpleStringProperty(String.valueOf(path));
        this.enabled = new SimpleBooleanProperty(isEnabled);
    }


}
