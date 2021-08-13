module com.amanvishnani.sim8085 {
    requires java.desktop;
    requires io.reactivex.rxjava3;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires java.logging;
    opens com.amanvishnani.sim8085.domain.Impl;
}