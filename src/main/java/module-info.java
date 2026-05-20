module edu.masanz.da.en {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens edu.masanz.da.en to javafx.fxml;

    exports edu.masanz.da.en;
}
