module at.htl.fxglprojection {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires annotations;
    requires com.almasb.fxgl.entity;
    requires at.htl.fxglprojection;
    requires javafx.graphics;
    requires javafx.base;

    opens at.htl.fxglprojection to javafx.fxml;
    exports at.htl.fxglprojection;
}