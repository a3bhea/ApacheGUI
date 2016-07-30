package net.ovoice.apachegui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ApacheModule {
    public StringProperty name;
    public BooleanProperty enabled = new SimpleBooleanProperty(false);
    private String[] lastCmd;
    private AG_Runtime agRuntime = new AG_Runtime();

    public ApacheModule(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public final StringProperty nameProperty() {
        return name;
    }

    public final BooleanProperty enabledProperty() {
        // Read apache.conf

        return enabled;
    }
}
