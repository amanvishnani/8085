open module com.amanvishnani.simulator {
    requires java.desktop;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires java.logging;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.amanvishnani.sim8085.domain;
    exports com.amanvishnani.sim8085.domain.Impl;
    exports com.amanvishnani.sim8085;
}