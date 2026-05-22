module edu.masanz.da.en {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;

    opens edu.masanz.da.en to javafx.fxml;

    exports edu.masanz.da.en;
}
