module at.htl.fxglprojection {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires annotations;
    requires com.almasb.fxgl.entity;
    requires javafx.graphics;
    requires javafx.base;
    requires jdk.jdi;

    opens at.htl.fxglprojection to javafx.fxml;
    exports at.htl.fxglprojection;
    exports at.htl.fxglprojection.renderer;
    opens at.htl.fxglprojection.renderer to javafx.fxml;
}