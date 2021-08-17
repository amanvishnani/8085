open module com.amanvishnani.simulator {
    requires java.desktop;
    requires io.reactivex.rxjava3;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires java.logging;
    exports com.amanvishnani.sim8085.domain;
}