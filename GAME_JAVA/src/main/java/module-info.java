module org.example.game_java {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.logging;

    opens game to javafx.fxml;
    exports game;
}